package tech.finovy.framework.logappender.push.internals;

import com.google.common.util.concurrent.ListenableFuture;
import tech.finovy.framework.logappender.conf.ProducerConfig;
import tech.finovy.framework.logappender.entry.GroupKey;
import tech.finovy.framework.logappender.entry.LogItem;
import tech.finovy.framework.logappender.entry.PushBatch;
import tech.finovy.framework.logappender.entry.Result;
import tech.finovy.framework.logappender.exception.LogSizeTooLargeException;
import tech.finovy.framework.logappender.exception.ProducerException;
import tech.finovy.framework.logappender.exception.TimeoutException;
import tech.finovy.framework.logappender.push.Callback;
import tech.finovy.framework.logappender.push.http.ClientImpl;
import tech.finovy.framework.logappender.utils.AbstractUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class LogAccumulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAccumulator.class);
    private static final AtomicLong BATCH_ID = new AtomicLong(0);
    private final String producerHash;
    private final ProducerConfig producerConfig;
    private final Map<String, ClientImpl> clientPool;
    private final Semaphore memoryController;
    private final RetryQueue retryQueue;
    private final BlockingQueue<PushBatch> successQueue;
    private final BlockingQueue<PushBatch> failureQueue;
    private final IOThreadPool ioThreadPool;
    private final AtomicInteger batchCount;
    private final ConcurrentMap<GroupKey, ProducerBatchHolder> batches;
    private final AtomicInteger appendsInProgress;
    private volatile boolean closed;

    public LogAccumulator(String producerHash, ProducerConfig producerConfig, Map<String, ClientImpl> clientPool, Semaphore memoryController, RetryQueue retryQueue, BlockingQueue<PushBatch> successQueue, BlockingQueue<PushBatch> failureQueue, IOThreadPool ioThreadPool, AtomicInteger batchCount) {
        this.producerHash = producerHash;
        this.producerConfig = producerConfig;
        this.clientPool = clientPool;
        this.memoryController = memoryController;
        this.retryQueue = retryQueue;
        this.successQueue = successQueue;
        this.failureQueue = failureQueue;
        this.ioThreadPool = ioThreadPool;
        this.batchCount = batchCount;
        this.batches = new ConcurrentHashMap<>();
        this.appendsInProgress = new AtomicInteger(0);
        this.closed = false;
    }

    public ListenableFuture<Result> append(String project, String logStore, String topic, String source, String shardHash, List<LogItem> logItems, Callback callback) throws InterruptedException, ProducerException {
        appendsInProgress.incrementAndGet();
        try {
            return doAppend(project, logStore, topic, source, shardHash, logItems, callback);
        } finally {
            appendsInProgress.decrementAndGet();
        }
    }

    private ListenableFuture<Result> doAppend(String project, String logStore, String topic, String source, String shardHash, List<LogItem> logItems, Callback callback) throws InterruptedException, ProducerException {
        if (closed) {
            throw new IllegalStateException("cannot append after the log accumulator was closed");
        }
        int sizeInBytes = AbstractLogSizeCalculator.calculate(logItems);
        ensureValidLogSize(sizeInBytes);
        long maxBlockMs = producerConfig.getMaxBlockMs();
        LOGGER.trace("Prepare to acquire bytes, sizeInBytes={}, maxBlockMs={}, project={}, logStore={}", sizeInBytes, maxBlockMs, project, logStore);
        if (maxBlockMs >= 0) {
            boolean acquired = memoryController.tryAcquire(sizeInBytes, maxBlockMs, TimeUnit.MILLISECONDS);
            if (!acquired) {
                LOGGER.warn("Failed to acquire memory within the configured max blocking time {} ms, requiredSizeInBytes={}, availableSizeInBytes={}", producerConfig.getMaxBlockMs(), sizeInBytes, memoryController.availablePermits());
                throw new TimeoutException("failed to acquire memory within the configured max blocking time " + producerConfig.getMaxBlockMs() + " ms");
            }
        } else {
            memoryController.acquire(sizeInBytes);
        }
        try {
            GroupKey groupKey = new GroupKey(project, logStore, topic, source, shardHash);
            ProducerBatchHolder holder = getOrCreateProducerBatchHolder(groupKey);
            synchronized (holder) {
                return appendToHolder(groupKey, logItems, callback, sizeInBytes, holder);
            }
        } catch (Exception e) {
            memoryController.release(sizeInBytes);
            throw new ProducerException(e);
        }
    }

    private ListenableFuture<Result> appendToHolder(GroupKey groupKey, List<LogItem> logItems, Callback callback, int sizeInBytes, ProducerBatchHolder holder) {
        if (holder.pushBatch != null) {
            ListenableFuture<Result> f = holder.pushBatch.tryAppend(logItems, sizeInBytes, callback);
            if (f != null) {
                if (holder.pushBatch.isMeetSendCondition()) {
                    holder.transferProducerBatch(ioThreadPool, producerConfig, clientPool, retryQueue, successQueue, failureQueue, batchCount);
                }
                return f;
            } else {
                holder.transferProducerBatch(ioThreadPool, producerConfig, clientPool, retryQueue, successQueue, failureQueue, batchCount);
            }
        }
        holder.pushBatch = new PushBatch(groupKey, AbstractUtils.generatePackageId(producerHash, BATCH_ID), producerConfig.getBatchSizeThresholdInBytes(), producerConfig.getBatchCountThreshold(), producerConfig.getMaxReservedAttempts(), System.currentTimeMillis());
        ListenableFuture<Result> f = holder.pushBatch.tryAppend(logItems, sizeInBytes, callback);
        batchCount.incrementAndGet();
        if (holder.pushBatch.isMeetSendCondition()) {
            holder.transferProducerBatch(ioThreadPool, producerConfig, clientPool, retryQueue, successQueue, failureQueue, batchCount);
        }
        return f;
    }

    public ExpiredBatches expiredBatches() {
        long nowMs = System.currentTimeMillis();
        ExpiredBatches expiredBatches = new ExpiredBatches();
        long remainingMs = producerConfig.getLingerMs();
        for (Map.Entry<GroupKey, ProducerBatchHolder> entry : batches.entrySet()) {
            ProducerBatchHolder holder = entry.getValue();
            synchronized (holder) {
                if (holder.pushBatch == null) {
                    continue;
                }
                long curRemainingMs = holder.pushBatch.remainingMs(nowMs, producerConfig.getLingerMs());
                if (curRemainingMs <= 0) {
                    holder.transferProducerBatch(expiredBatches);
                } else {
                    remainingMs = Math.min(remainingMs, curRemainingMs);
                }
            }
        }
        expiredBatches.setRemainingMs(remainingMs);
        return expiredBatches;
    }

    public List<PushBatch> remainingBatches() {
        if (!closed) {
            throw new IllegalStateException("cannot get the remaining batches before the log accumulator closed");
        }
        List<PushBatch> remainingBatches = new ArrayList<>();
        while (appendsInProgress()) {
            drainTo(remainingBatches);
        }
        drainTo(remainingBatches);
        batches.clear();
        return remainingBatches;
    }

    private int drainTo(List<PushBatch> c) {
        int n = 0;
        for (Map.Entry<GroupKey, ProducerBatchHolder> entry : batches.entrySet()) {
            ProducerBatchHolder holder = entry.getValue();
            synchronized (holder) {
                if (holder.pushBatch == null) {
                    continue;
                }
                c.add(holder.pushBatch);
                ++n;
                holder.pushBatch = null;
            }
        }
        return n;
    }

    private void ensureValidLogSize(int sizeInBytes) throws LogSizeTooLargeException {
        if (sizeInBytes > ProducerConfig.MAX_BATCH_SIZE_IN_BYTES) {
            throw new LogSizeTooLargeException("the logs is " + sizeInBytes + " bytes which is larger than MAX_BATCH_SIZE_IN_BYTES " + ProducerConfig.MAX_BATCH_SIZE_IN_BYTES);
        }
        if (sizeInBytes > producerConfig.getTotalSizeInBytes()) {
            throw new LogSizeTooLargeException("the logs is " + sizeInBytes + " bytes which is larger than the totalSizeInBytes you specified");
        }
    }

    private ProducerBatchHolder getOrCreateProducerBatchHolder(GroupKey groupKey) {
        ProducerBatchHolder holder = batches.get(groupKey);
        if (holder != null) {
            return holder;
        }
        holder = new ProducerBatchHolder();
        ProducerBatchHolder previous = batches.putIfAbsent(groupKey, holder);
        if (previous == null) {
            return holder;
        } else {
            return previous;
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public void close() {
        this.closed = true;
    }

    private boolean appendsInProgress() {
        return appendsInProgress.get() > 0;
    }

    private static final class ProducerBatchHolder {

        PushBatch pushBatch;

        void transferProducerBatch(IOThreadPool ioThreadPool, ProducerConfig producerConfig, Map<String, ClientImpl> clientPool, RetryQueue retryQueue, BlockingQueue<PushBatch> successQueue, BlockingQueue<PushBatch> failureQueue, AtomicInteger batchCount) {
            if (pushBatch == null) {
                return;
            }
            ioThreadPool.submit(new SendProducerBatchTask(pushBatch, producerConfig, clientPool, retryQueue, successQueue, failureQueue, batchCount));
            pushBatch = null;
        }

        void transferProducerBatch(ExpiredBatches expiredBatches) {
            if (pushBatch == null) {
                return;
            }
            expiredBatches.add(pushBatch);
            pushBatch = null;
        }
    }
}
