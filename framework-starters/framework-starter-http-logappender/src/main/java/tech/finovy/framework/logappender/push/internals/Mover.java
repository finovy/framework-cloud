package tech.finovy.framework.logappender.push.internals;

import tech.finovy.framework.logappender.conf.ProducerConfig;
import tech.finovy.framework.logappender.entry.PushBatch;
import tech.finovy.framework.logappender.push.http.ClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Mover extends LogThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mover.class);
    private final ProducerConfig producerConfig;
    private final Map<String, ClientImpl> clientPool;
    private final LogAccumulator accumulator;
    private final RetryQueue retryQueue;
    private final BlockingQueue<PushBatch> successQueue;
    private final BlockingQueue<PushBatch> failureQueue;
    private final IOThreadPool ioThreadPool;
    private final AtomicInteger batchCount;
    private volatile boolean closed;

    public Mover(String name, ProducerConfig producerConfig, Map<String, ClientImpl> clientPool, LogAccumulator accumulator, RetryQueue retryQueue, BlockingQueue<PushBatch> successQueue, BlockingQueue<PushBatch> failureQueue, IOThreadPool ioThreadPool, AtomicInteger batchCount) {
        super(name, true);
        this.producerConfig = producerConfig;
        this.clientPool = clientPool;
        this.accumulator = accumulator;
        this.retryQueue = retryQueue;
        this.successQueue = successQueue;
        this.failureQueue = failureQueue;
        this.ioThreadPool = ioThreadPool;
        this.batchCount = batchCount;
        this.closed = false;
    }

    @Override
    public void run() {
        loopMoveBatches();
        LOGGER.debug("Beginning shutdown of mover thread");
        List<PushBatch> incompleteBatches = incompleteBatches();
        LOGGER.debug("Submit incomplete batches, size={}", incompleteBatches.size());
        submitIncompleteBatches(incompleteBatches);
        LOGGER.debug("Shutdown of mover thread has completed");
    }

    private void loopMoveBatches() {
        while (!closed) {
            try {
                moveBatches();
            } catch (Exception e) {
                LOGGER.error("Uncaught exception in mover, e=", e);
            }
        }
    }

    private void moveBatches() {
//        LOGGER.debug("Prepare to move expired batches from accumulator and retry queue to ioThreadPool");
        doMoveBatches();
//        LOGGER.debug("Move expired batches successfully");
    }

    private void doMoveBatches() {
        ExpiredBatches expiredBatches = accumulator.expiredBatches();
//        LOGGER.debug("Expired batches from accumulator, size={}, remainingMs={}", expiredBatches.getBatches().size(), expiredBatches.getRemainingMs());
        for (PushBatch b : expiredBatches.getBatches()) {
            ioThreadPool.submit(createSendProducerBatchTask(b));
        }
        List<PushBatch> expiredRetryBatches = retryQueue.expiredBatches(expiredBatches.getRemainingMs());
//        LOGGER.debug("Expired batches from retry queue, size={}", expiredRetryBatches.size());
        for (PushBatch b : expiredRetryBatches) {
            ioThreadPool.submit(createSendProducerBatchTask(b));
        }
    }

    private List<PushBatch> incompleteBatches() {
        List<PushBatch> incompleteBatches = accumulator.remainingBatches();
        incompleteBatches.addAll(retryQueue.remainingBatches());
        return incompleteBatches;
    }

    private void submitIncompleteBatches(List<PushBatch> incompleteBatches) {
        for (PushBatch b : incompleteBatches) {
            ioThreadPool.submit(createSendProducerBatchTask(b));
        }
    }

    private SendProducerBatchTask createSendProducerBatchTask(PushBatch batch) {
        return new SendProducerBatchTask(batch, producerConfig, clientPool, retryQueue, successQueue, failureQueue, batchCount);
    }

    public void close() {
        this.closed = true;
        interrupt();
    }
}
