package tech.finovy.framework.common.core.exception;

public class ShardingEngineDataStoreException extends RuntimeException {
    public ShardingEngineDataStoreException() {
    }

    public ShardingEngineDataStoreException(String message) {
        super(message);
    }

    public ShardingEngineDataStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShardingEngineDataStoreException(Throwable cause) {
        super(cause);
    }

    public ShardingEngineDataStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
