package tech.finovy.framework.logappender.push.internals;


import com.google.common.math.LongMath;
import tech.finovy.framework.logappender.conf.ProducerConfig;
import tech.finovy.framework.logappender.entry.*;
import tech.finovy.framework.logappender.exception.ErrorCodes;
import tech.finovy.framework.logappender.exception.LogException;
import tech.finovy.framework.logappender.push.http.ClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SendProducerBatchTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendProducerBatchTask.class);

    private static final String TAG_PACK_ID = "pack_id";
    private final PushBatch batch;
    private final ProducerConfig producerConfig;
    private final Map<String, ClientImpl> clientPool;
    private final RetryQueue retryQueue;
    private final BlockingQueue<PushBatch> successQueue;
    private final BlockingQueue<PushBatch> failureQueue;
    private final AtomicInteger batchCount;

    public SendProducerBatchTask(PushBatch batch, ProducerConfig producerConfig, Map<String, ClientImpl> clientPool, RetryQueue retryQueue, BlockingQueue<PushBatch> successQueue, BlockingQueue<PushBatch> failureQueue, AtomicInteger batchCount) {
        this.batch = batch;
        this.producerConfig = producerConfig;
        this.clientPool = clientPool;
        this.retryQueue = retryQueue;
        this.successQueue = successQueue;
        this.failureQueue = failureQueue;
        this.batchCount = batchCount;
    }

    @Override
    public void run() {
        try {
            sendProducerBatch(System.currentTimeMillis());
        } catch (InterruptedException t) {
            LOGGER.error("Uncaught error in send producer batch task, project=" + batch.getProject() + ", logStore=" + batch.getLogStore() + ", e=", t);
            Thread.currentThread().interrupt();
        }
    }

    private void sendProducerBatch(long nowMs) throws InterruptedException {
        LOGGER.trace("Prepare to send producer batch, batch={}", batch);
        String project = batch.getProject();
        ClientImpl client = getClient(project);
        if (client == null) {
            LOGGER.error("Failed to get client, project={}", project);
            Attempt attempt = new Attempt(false, "", ErrorCodes.Errors.PROJECT_CONFIG_NOT_EXIST, "Cannot get the projectConfig for project " + project, nowMs);
            batch.appendAttempt(attempt);
            failureQueue.put(batch);
        } else {
            PutLogsResponse response;
            try {
                PutLogsRequest request = buildPutLogsRequest(batch);
                response = client.putLogs(request);
            } catch (Exception e) {
                LOGGER.error("Failed to put logs, project={}, logStore={}, e={}", batch.getProject(), batch.getLogStore(), e.getMessage());
                Attempt attempt = buildAttempt(e, nowMs);
                batch.appendAttempt(attempt);
                if (meetFailureCondition(e)) {
                    LOGGER.debug("Prepare to put batch to the failure queue");
                    failureQueue.put(batch);
                } else {
                    LOGGER.debug("Prepare to put batch to the retry queue");
                    long retryBackoffMs = calculateRetryBackoffMs();
                    LOGGER.debug("Calculate the retryBackoffMs successfully, retryBackoffMs={}", retryBackoffMs);
                    batch.setNextRetryMs(System.currentTimeMillis() + retryBackoffMs);
                    try {
                        retryQueue.put(batch);
                    } catch (IllegalStateException e1) {
                        LOGGER.error("Failed to put batch to the retry queue, project={}, logStore={}, e={}", batch.getProject(), batch.getLogStore(), e);
                        if (retryQueue.isClosed()) {
                            LOGGER.info("Prepare to put batch to the failure queue since the retry queue was closed");
                            failureQueue.put(batch);
                        }
                    }
                }
                return;
            }
            Attempt attempt = new Attempt(true, response.getRequestId(), "", "", nowMs);
            batch.appendAttempt(attempt);
            successQueue.put(batch);
            LOGGER.trace("Send producer batch successfully, batch={}", batch);
        }
    }

    private ClientImpl getClient(String project) {
        return clientPool.get(project);
    }

    private PutLogsRequest buildPutLogsRequest(PushBatch batch) {
        PutLogsRequest request;
        if (batch.getShardHash() != null && !batch.getShardHash().isEmpty()) {
            request = new PutLogsRequest(batch.getProject(), batch.getLogStore(), batch.getTopic(), batch.getSource(), batch.getLogItems(), batch.getShardHash());
        } else {
            request = new PutLogsRequest(batch.getProject(), batch.getLogStore(), batch.getTopic(), batch.getSource(), batch.getLogItems());
        }
        List<TagContent> tags = new ArrayList<>();
        tags.add(new TagContent(TAG_PACK_ID, batch.getPackageId()));
        request.setTags(tags);
        if (producerConfig.getLogFormat() == ProducerConfig.LogFormat.PROTOBUF) {
            request.setContentType(Consts.CONST_PROTO_BUF);
        } else {
            request.setContentType(Consts.CONST_SLS_JSON);
        }
        return request;
    }

    private Attempt buildAttempt(Exception e, long nowMs) {
        if (e instanceof LogException) {
            LogException logException = (LogException) e;
            return new Attempt(false, logException.getRequestId(), logException.getErrorCode(), logException.getErrorMessage(), nowMs);
        } else {
            return new Attempt(false, "", ErrorCodes.Errors.PRODUCER_EXCEPTION, e.getMessage(), nowMs);
        }
    }

    private boolean meetFailureCondition(Exception e) {
        if (!isRetriableException(e)) {
            return true;
        }
        if (retryQueue.isClosed()) {
            return true;
        }
        return (batch.getRetries() >= producerConfig.getRetries()
                && failureQueue.size() <= batchCount.get() / 2);
    }

    private boolean isRetriableException(Exception e) {
        if (e instanceof LogException) {
            LogException logException = (LogException) e;
            return (logException.getErrorCode().equals(ErrorCodes.RetriableErrors.REQUEST_ERROR)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.UNAUTHORIZED)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.WRITE_QUOTA_EXCEED)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.SHARD_WRITE_QUOTA_EXCEED)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.EXCEED_QUOTA)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.INTERNAL_SERVER_ERROR)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.SERVER_BUSY)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.BAD_RESPONSE)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.PROJECT_NOT_EXISTS)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.LOGSTORE_NOT_EXISTS)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.SOCKET_TIMEOUT)
                    || logException.getErrorCode().equals(ErrorCodes.RetriableErrors.SIGNATURE_NOT_MATCH));
        }
        return false;
    }

    private long calculateRetryBackoffMs() {
        int retry = batch.getRetries();
        long retryBackoffMs = producerConfig.getBaseRetryBackoffMs() * LongMath.pow(2, retry);
        if (retryBackoffMs <= 0) {
            retryBackoffMs = producerConfig.getMaxRetryBackoffMs();
        }
        return Math.min(retryBackoffMs, producerConfig.getMaxRetryBackoffMs());
    }
}
