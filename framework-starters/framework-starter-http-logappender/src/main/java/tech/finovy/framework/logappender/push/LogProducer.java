package tech.finovy.framework.logappender.push;


import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import tech.finovy.framework.logappender.conf.ClientConfiguration;
import tech.finovy.framework.logappender.conf.ProducerConfig;
import tech.finovy.framework.logappender.conf.ProjectConfig;
import tech.finovy.framework.logappender.entry.LogItem;
import tech.finovy.framework.logappender.entry.PushBatch;
import tech.finovy.framework.logappender.entry.Result;
import tech.finovy.framework.logappender.exception.MaxBatchCountExceedException;
import tech.finovy.framework.logappender.exception.ProducerException;
import tech.finovy.framework.logappender.push.http.AbstractServiceClient;
import tech.finovy.framework.logappender.push.http.ClientImpl;
import tech.finovy.framework.logappender.push.http.TimeoutServiceClient;
import tech.finovy.framework.logappender.push.internals.*;
import tech.finovy.framework.logappender.utils.AbstractUtils;
import tech.finovy.framework.logappender.utils.ShardHashAdjuster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dtype.huang
 */
public class LogProducer implements Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogProducer.class);
    private static final AtomicInteger INSTANCE_ID_GENERATOR = new AtomicInteger(0);
    private static final String LOG_PRODUCER_PREFIX = "Log-agent-";
    private static final String MOVER_SUFFIX = "-mover";
    private static final String SUCCESS_BATCH_HANDLER_SUFFIX = "-success-batch-handler";
    private static final String FAILURE_BATCH_HANDLER_SUFFIX = "-failure-batch-handler";
    private static final String TIMEOUT_THREAD_SUFFIX_FORMAT = "-timeout-thread-%d";
    private final int instanceId;
    private final String name;
    private final String producerHash;
    private final ProducerConfig producerConfig;
    private final Map<String, ClientImpl> clientPool = new ConcurrentHashMap<>();
    private final AbstractServiceClient serviceClient;
    private final Semaphore memoryController;
    private final RetryQueue retryQueue;
    private final IOThreadPool ioThreadPool;
    private final ThreadPoolExecutor timeoutThreadPool;
    private final LogAccumulator accumulator;
    private final Mover mover;
    private final BatchHandler successBatchHandler;
    private final BatchHandler failureBatchHandler;
    private final AtomicInteger batchCount = new AtomicInteger(0);
    private final ShardHashAdjuster adjuster;

    public LogProducer(ProducerConfig producerConfig) {
        this.instanceId = INSTANCE_ID_GENERATOR.getAndIncrement();
        this.name = LOG_PRODUCER_PREFIX + this.instanceId;
        this.producerHash = AbstractUtils.generateProducerHash(this.instanceId);
        this.producerConfig = producerConfig;
        this.memoryController = new Semaphore(producerConfig.getTotalSizeInBytes());
        this.retryQueue = new RetryQueue();
        BlockingQueue<PushBatch> successQueue = new LinkedBlockingQueue<>();
        BlockingQueue<PushBatch> failureQueue = new LinkedBlockingQueue<>();
        this.ioThreadPool = new IOThreadPool(producerConfig.getIoThreadCount(), this.name);
        this.timeoutThreadPool =
                new ThreadPoolExecutor(producerConfig.getIoThreadCount(), producerConfig.getIoThreadCount(), 0L,
                        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat(this.name + TIMEOUT_THREAD_SUFFIX_FORMAT)
                        .build());
        this.serviceClient = new TimeoutServiceClient(new ClientConfiguration(), this.timeoutThreadPool);
        this.accumulator = new LogAccumulator(this.producerHash, producerConfig, this.clientPool, this.memoryController, this.retryQueue, successQueue, failureQueue, this.ioThreadPool, this.batchCount);
        this.mover = new Mover(this.name + MOVER_SUFFIX, producerConfig, this.clientPool, this.accumulator, this.retryQueue, successQueue, failureQueue, this.ioThreadPool, this.batchCount);
        this.successBatchHandler = new BatchHandler(this.name + SUCCESS_BATCH_HANDLER_SUFFIX, successQueue, this.batchCount, this.memoryController);
        this.failureBatchHandler = new BatchHandler(this.name + FAILURE_BATCH_HANDLER_SUFFIX, failureQueue, this.batchCount, this.memoryController);
        this.mover.start();
        this.successBatchHandler.start();
        this.failureBatchHandler.start();
        this.adjuster = new ShardHashAdjuster(producerConfig.getBuckets());
    }


    @Override
    public ListenableFuture<Result> send(String project, String logStore, LogItem logItem) throws InterruptedException, ProducerException {
        return send(project, logStore, "", "", null, logItem, null);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, List<LogItem> logItems) throws InterruptedException, ProducerException {
        return send(project, logStore, "", "", null, logItems, null);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, String topic, String source, LogItem logItem) throws InterruptedException, ProducerException {
        return send(project, logStore, topic, source, null, logItem, null);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, String topic, String source, List<LogItem> logItems) throws InterruptedException, ProducerException {
        return send(project, logStore, topic, source, null, logItems, null);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, String topic, String source, String shardHash, LogItem logItem) throws InterruptedException, ProducerException {
        return send(project, logStore, topic, source, shardHash, logItem, null);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, String topic, String source, String shardHash, List<LogItem> logItems) throws InterruptedException, ProducerException {
        return send(project, logStore, topic, source, shardHash, logItems, null);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, LogItem logItem, Callback callback) throws InterruptedException, ProducerException {
        return send(project, logStore, "", "", null, logItem, callback);
    }


    @Override
    public ListenableFuture<Result> send(String project, String logStore, List<LogItem> logItems, Callback callback) throws InterruptedException, ProducerException {
        return send(project, logStore, "", "", null, logItems, callback);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, String topic, String source, LogItem logItem, Callback callback) throws InterruptedException, ProducerException {
        return send(project, logStore, topic, source, null, logItem, callback);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, String topic, String source, List<LogItem> logItems, Callback callback) throws InterruptedException, ProducerException {
        return send(project, logStore, topic, source, null, logItems, callback);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, String topic, String source, String shardHash, LogItem logItem, Callback callback) throws InterruptedException, ProducerException {
        AbstractUtils.assertArgumentNotNull(logItem, "logItem");
        List<LogItem> logItems = new ArrayList<>();
        logItems.add(logItem);
        return send(project, logStore, topic, source, shardHash, logItems, callback);
    }

    @Override
    public ListenableFuture<Result> send(String project, String logStore, String topic, String source, String shardHash, List<LogItem> logItems, Callback callback) throws InterruptedException, ProducerException {
        AbstractUtils.assertArgumentNotNullOrEmpty(project, "project");
        AbstractUtils.assertArgumentNotNullOrEmpty(logStore, "logStore");
        if (topic == null) {
            topic = "";
        }
        AbstractUtils.assertArgumentNotNull(logItems, "logItems");
        if (logItems.isEmpty()) {
            throw new IllegalArgumentException("logItems cannot be empty");
        }
        int count = logItems.size();
        if (count > ProducerConfig.MAX_BATCH_COUNT) {
            throw new MaxBatchCountExceedException("the log list size is " + count + " which exceeds the MAX_BATCH_COUNT " + ProducerConfig.MAX_BATCH_COUNT);
        }
        if (shardHash != null && producerConfig.isAdjustShardHash()) {
            shardHash = adjuster.adjust(shardHash);
        }
        return accumulator.append(project, logStore, topic, source, shardHash, logItems, callback);
    }

    @Override
    public void close() throws InterruptedException, ProducerException {
        close(Long.MAX_VALUE);
    }

    @Override
    public void close(long timeoutMs) throws InterruptedException, ProducerException {
        if (timeoutMs < 0) {
            throw new IllegalArgumentException("timeoutMs must be greater than or equal to 0, got " + timeoutMs);
        }
        ProducerException firstException = null;
        LOGGER.info("Closing the log producer, timeoutMs={}", timeoutMs);
        try {
            timeoutMs = closeMover(timeoutMs);
        } catch (ProducerException e) {
            firstException = e;
        }
        LOGGER.debug("After close mover, timeoutMs={}", timeoutMs);
        try {
            timeoutMs = closeIOThreadPool(timeoutMs);
        } catch (ProducerException e) {
            if (firstException == null) {
                firstException = e;
            }
        }
        LOGGER.debug("After close ioThreadPool, timeoutMs={}", timeoutMs);
        try {
            timeoutMs = closeTimeoutThreadPool(timeoutMs);
        } catch (ProducerException e) {
            if (firstException == null) {
                firstException = e;
            }
        }
        LOGGER.debug("After close timeoutThreadPool, timeoutMs={}", timeoutMs);
        try {
            timeoutMs = closeSuccessBatchHandler(timeoutMs);
        } catch (ProducerException e) {
            if (firstException == null) {
                firstException = e;
            }
        }
        LOGGER.debug("After close success batch handler, timeoutMs={}", timeoutMs);
        try {
            timeoutMs = closeFailureBatchHandler(timeoutMs);
        } catch (ProducerException e) {
            if (firstException == null) {
                firstException = e;
            }
        }
        LOGGER.debug("After close failure batch handler, timeoutMs={}", timeoutMs);
        if (firstException != null) {
            throw firstException;
        }
        LOGGER.info("The log producer has been closed");
    }

    private long closeMover(long timeoutMs) throws InterruptedException, ProducerException {
        long startMs = System.currentTimeMillis();
        accumulator.close();
        retryQueue.close();
        mover.close();
        mover.join(timeoutMs);
        if (mover.isAlive()) {
            LOGGER.warn("The mover thread is still alive");
            throw new ProducerException("the mover thread is still alive");
        }
        long nowMs = System.currentTimeMillis();
        return Math.max(0, timeoutMs - nowMs + startMs);
    }

    private long closeIOThreadPool(long timeoutMs) throws InterruptedException, ProducerException {
        long startMs = System.currentTimeMillis();
        ioThreadPool.shutdown();
        if (ioThreadPool.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS)) {
            LOGGER.debug("The ioThreadPool is terminated");
        } else {
            LOGGER.warn("The ioThreadPool is not fully terminated");
            throw new ProducerException("the ioThreadPool is not fully terminated");
        }
        long nowMs = System.currentTimeMillis();
        return Math.max(0, timeoutMs - nowMs + startMs);
    }

    private long closeTimeoutThreadPool(long timeoutMs) throws InterruptedException, ProducerException {
        long startMs = System.currentTimeMillis();
        timeoutThreadPool.shutdown();
        if (timeoutThreadPool.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS)) {
            LOGGER.debug("The timeoutThreadPool is terminated");
        } else {
            LOGGER.warn("The timeoutThreadPool is not fully terminated");
            throw new ProducerException("the timeoutThreadPool is not fully terminated");
        }
        long nowMs = System.currentTimeMillis();
        return Math.max(0, timeoutMs - nowMs + startMs);
    }

    private long closeSuccessBatchHandler(long timeoutMs) throws InterruptedException, ProducerException {
        long startMs = System.currentTimeMillis();
        successBatchHandler.close();
        boolean invokedFromCallback = Thread.currentThread() == this.successBatchHandler;
        if (invokedFromCallback) {
            LOGGER.warn("Skip join success batch handler since you have incorrectly invoked close from the producer call-back");
            return timeoutMs;
        }
        successBatchHandler.join(timeoutMs);
        if (successBatchHandler.isAlive()) {
            LOGGER.warn("The success batch handler thread is still alive");
            throw new ProducerException("the success batch handler thread is still alive");
        }
        long nowMs = System.currentTimeMillis();
        return Math.max(0, timeoutMs - nowMs + startMs);
    }

    private long closeFailureBatchHandler(long timeoutMs) throws InterruptedException, ProducerException {
        long startMs = System.currentTimeMillis();
        failureBatchHandler.close();
        boolean invokedFromCallback = Thread.currentThread() == this.successBatchHandler || Thread.currentThread() == this.failureBatchHandler;
        if (invokedFromCallback) {
            LOGGER.warn("Skip join failure batch handler since you have incorrectly invoked close from the producer call-back");
            return timeoutMs;
        }
        failureBatchHandler.join(timeoutMs);
        if (failureBatchHandler.isAlive()) {
            LOGGER.warn("The failure batch handler thread is still alive");
            throw new ProducerException("the failure batch handler thread is still alive");
        }
        long nowMs = System.currentTimeMillis();
        return Math.max(0, timeoutMs - nowMs + startMs);
    }

    @Override
    public ProducerConfig getProducerConfig() {
        return producerConfig;
    }

    @Override
    public int getBatchCount() {
        return batchCount.get();
    }

    @Override
    public int availableMemoryInBytes() {
        return memoryController.availablePermits();
    }

    @Override
    public void putProjectConfig(ProjectConfig projectConfig) {
        ClientImpl client = buildClient(projectConfig);
        clientPool.put(projectConfig.getProject(), client);
    }

    @Override
    public void removeProjectConfig(ProjectConfig projectConfig) {
        clientPool.remove(projectConfig.getProject());
    }

    private ClientImpl buildClient(ProjectConfig projectConfig) {
        ClientImpl client = new ClientImpl(projectConfig.getEndpoint(), projectConfig.getAccessKeyId(), projectConfig.getAccessKeySecret(), serviceClient);
        String userAgent = projectConfig.getUserAgent();
        if (userAgent != null) {
            client.setUserAgent(userAgent);
        }
        String stsToken = projectConfig.getStsToken();
        if (stsToken != null) {
            client.setSecurityToken(stsToken);
        }
        return client;
    }
}
