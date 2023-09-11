package tech.finovy.framework.logappender.conf;

import tech.finovy.framework.logappender.utils.ShardHashAdjuster;

public class ProducerConfig {

    public static final int DEFAULT_TOTAL_SIZE_IN_BYTES = 100 * 1024 * 1024;
    public static final long DEFAULT_MAX_BLOCK_MS = 30 * 1000L;
    public static final int DEFAULT_IO_THREAD_COUNT = Math.max(Runtime.getRuntime().availableProcessors(), 1);
    public static final int DEFAULT_BATCH_SIZE_THRESHOLD_IN_BYTES = 512 * 1024;
    public static final int MAX_BATCH_SIZE_IN_BYTES = 8 * 1024 * 1024;
    public static final int DEFAULT_BATCH_COUNT_THRESHOLD = 4096;
    public static final int MAX_BATCH_COUNT = 40960;
    public static final int DEFAULT_LINGER_MS = 2000;
    public static final int LINGER_MS_LOWER_LIMIT = 100;
    public static final int DEFAULT_RETRIES = 10;
    public static final long DEFAULT_BASE_RETRY_BACKOFF_MS = 100L;
    public static final long DEFAULT_MAX_RETRY_BACKOFF_MS = 50 * 1000L;
    public static final int DEFAULT_BUCKETS = 64;
    public static final int BUCKETS_LOWER_LIMIT = 1;
    public static final int BUCKETS_UPPER_LIMIT = 256;
    public static final LogFormat DEFAULT_LOG_FORMAT = LogFormat.PROTOBUF;
    private int totalSizeInBytes = DEFAULT_TOTAL_SIZE_IN_BYTES;
    private long maxBlockMs = DEFAULT_MAX_BLOCK_MS;
    private int ioThreadCount = DEFAULT_IO_THREAD_COUNT;
    private int batchSizeThresholdInBytes = DEFAULT_BATCH_SIZE_THRESHOLD_IN_BYTES;
    private int batchCountThreshold = DEFAULT_BATCH_COUNT_THRESHOLD;
    private int lingerMs = DEFAULT_LINGER_MS;
    private int retries = DEFAULT_RETRIES;
    private int maxReservedAttempts = DEFAULT_RETRIES + 1;
    private long baseRetryBackoffMs = DEFAULT_BASE_RETRY_BACKOFF_MS;
    private long maxRetryBackoffMs = DEFAULT_MAX_RETRY_BACKOFF_MS;
    private boolean adjustShardHash = true;
    private int buckets = DEFAULT_BUCKETS;
    private LogFormat logFormat = DEFAULT_LOG_FORMAT;

    public int getTotalSizeInBytes() {
        return totalSizeInBytes;
    }

    public void setTotalSizeInBytes(int totalSizeInBytes) {
        if (totalSizeInBytes <= 0) {
            throw new IllegalArgumentException("totalSizeInBytes must be greater than 0, got " + totalSizeInBytes);
        }
        this.totalSizeInBytes = totalSizeInBytes;
    }

    public long getMaxBlockMs() {
        return maxBlockMs;
    }

    public void setMaxBlockMs(long maxBlockMs) {
        this.maxBlockMs = maxBlockMs;
    }

    public int getIoThreadCount() {
        return ioThreadCount;
    }

    public void setIoThreadCount(int ioThreadCount) {
        if (ioThreadCount <= 0) {
            throw new IllegalArgumentException("ioThreadCount must be greater than 0, got " + ioThreadCount);
        }
        this.ioThreadCount = ioThreadCount;
    }

    public int getBatchSizeThresholdInBytes() {
        return batchSizeThresholdInBytes;
    }

    public void setBatchSizeThresholdInBytes(int batchSizeThresholdInBytes) {
        if (batchSizeThresholdInBytes < 1 || batchSizeThresholdInBytes > MAX_BATCH_SIZE_IN_BYTES) {
            throw new IllegalArgumentException(String.format("batchSizeThresholdInBytes must be between 1 and %d, got %d", MAX_BATCH_SIZE_IN_BYTES, batchSizeThresholdInBytes));
        }
        this.batchSizeThresholdInBytes = batchSizeThresholdInBytes;
    }

    public int getBatchCountThreshold() {
        return batchCountThreshold;
    }

    public void setBatchCountThreshold(int batchCountThreshold) {
        if (batchCountThreshold < 1 || batchCountThreshold > MAX_BATCH_COUNT) {
            throw new IllegalArgumentException(String.format("batchCountThreshold must be between 1 and %d, got %d", MAX_BATCH_COUNT, batchCountThreshold));
        }
        this.batchCountThreshold = batchCountThreshold;
    }

    public int getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(int lingerMs) {
        if (lingerMs < LINGER_MS_LOWER_LIMIT) {
            throw new IllegalArgumentException(String.format("lingerMs must be greater than or equal to %d, got %d", LINGER_MS_LOWER_LIMIT, lingerMs));
        }
        this.lingerMs = lingerMs;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getMaxReservedAttempts() {
        return maxReservedAttempts;
    }

    public void setMaxReservedAttempts(int maxReservedAttempts) {
        if (maxReservedAttempts <= 0) {
            throw new IllegalArgumentException("maxReservedAttempts must be greater than 0, got " + maxReservedAttempts);
        }
        this.maxReservedAttempts = maxReservedAttempts;
    }

    public long getBaseRetryBackoffMs() {
        return baseRetryBackoffMs;
    }

    public void setBaseRetryBackoffMs(long baseRetryBackoffMs) {
        if (baseRetryBackoffMs <= 0) {
            throw new IllegalArgumentException("baseRetryBackoffMs must be greater than 0, got " + baseRetryBackoffMs);
        }
        this.baseRetryBackoffMs = baseRetryBackoffMs;
    }

    public long getMaxRetryBackoffMs() {
        return maxRetryBackoffMs;
    }

    public void setMaxRetryBackoffMs(long maxRetryBackoffMs) {
        if (maxRetryBackoffMs <= 0) {
            throw new IllegalArgumentException("maxRetryBackoffMs must be greater than 0, got " + maxRetryBackoffMs);
        }
        this.maxRetryBackoffMs = maxRetryBackoffMs;
    }

    public boolean isAdjustShardHash() {
        return adjustShardHash;
    }

    public void setAdjustShardHash(boolean adjustShardHash) {
        this.adjustShardHash = adjustShardHash;
    }

    public int getBuckets() {
        return buckets;
    }

    public void setBuckets(int buckets) {
        if (buckets < BUCKETS_LOWER_LIMIT || buckets > BUCKETS_UPPER_LIMIT) {
            throw new IllegalArgumentException(String.format("buckets must be between %d and %d, got %d", BUCKETS_LOWER_LIMIT, BUCKETS_UPPER_LIMIT, buckets));
        }
        if (!ShardHashAdjuster.isPowerOfTwo(buckets)) {
            throw new IllegalArgumentException("buckets must be a power of 2, got " + buckets);
        }
        this.buckets = buckets;
    }

    public LogFormat getLogFormat() {
        return logFormat;
    }

    public void setLogFormat(LogFormat logFormat) {
        this.logFormat = logFormat;
    }

    public enum LogFormat {
        PROTOBUF,
        JSON
    }
}
