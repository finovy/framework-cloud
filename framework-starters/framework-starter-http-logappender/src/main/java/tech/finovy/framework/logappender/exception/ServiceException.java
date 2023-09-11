package tech.finovy.framework.logappender.exception;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 5088575207106396308L;
    private String errorCode;
    private String requestId;
    private String hostId;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, Throwable cause, String errorCode, String requestId, String hostId) {
        this(message, cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
        this.hostId = hostId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getHostId() {
        return hostId;
    }

    @Override
    public String toString() {
        return "[Error Code]:" + errorCode + ", [Message]:" + getMessage();
    }
}
