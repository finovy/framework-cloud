package tech.finovy.framework.distributed.event.entity;

import tech.finovy.framework.distributed.event.EventConstant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EventTransaction implements Serializable {
    private static final long serialVersionUID = -8463422913579964950L;
    private final Map<String, String> transactionHeaders = new HashMap<>();
    private String transactionId;
    private String application;
    private long timestamp;
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Map<String, String> getTransactionHeaders() {
        return transactionHeaders;
    }
    public void clearTransactionHeaders() {
         transactionHeaders.clear();
    }

    public void setTransactionHeaders(Map<String, String> transactionHeaders) {
        this.transactionHeaders.putAll(transactionHeaders);
    }

    public void setTransactionHeaders(String key, String value) {
        this.transactionHeaders.put(key, value);
    }

    public EventTransaction() {
    }

    public void setMessageQueueCompressor(String acceptCompressor) {
        transactionHeaders.put(EventConstant.MESSAGE_QUEUE_COMPRESSOR, acceptCompressor);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageQueueCompressor() {
        if (transactionHeaders == null) {
            return null;
        }
        return transactionHeaders.get(EventConstant.MESSAGE_QUEUE_COMPRESSOR);
    }
}
