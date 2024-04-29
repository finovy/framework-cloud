package tech.finovy.framework.logappender.exception;

public class ClientException extends RuntimeException {
    private static final long serialVersionUID = -4731383529577750679L;
    private String errorMessage;
    private String requestId;
    private String errorCode;

    public ClientException() {
        super();
    }

    public ClientException(String errorMessage) {
        this(errorMessage, null);
    }

    public ClientException(Throwable cause) {
        this(null, cause);
    }

    public ClientException(String errorMessage, Throwable cause) {
        super(null, cause);
        this.errorMessage = errorMessage;
        this.errorCode = ErrorCodes.ClientErrorCode.UNKNOWN;
        this.requestId = "Unknown";
    }

    public ClientException(String errorMessage, String errorCode, String requestId) {
        this(errorMessage, errorCode, requestId, null);
    }

    public ClientException(String errorMessage, String errorCode, String requestId, Throwable cause) {
        this(errorMessage, cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String getMessage() {
        return getErrorMessage() + "\n[ErrorCode]: " + (errorCode != null ? errorCode
                : "") + "\n[RequestId]: " + (requestId != null ? requestId : "");
    }
}
