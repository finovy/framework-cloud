package tech.finovy.framework.logappender.exception;

public class LogException extends Exception {
    private static final long serialVersionUID = 5007158359762451772L;
    private final String errorCode;
    private final String requestId;
    private int httpCode = -1;

    public LogException(String code, String message, String requestId) {
        super(message);
        this.errorCode = code;
        this.requestId = requestId;
    }

    public LogException(String code, String message, Throwable cause, String requestId) {
        super(message, cause);
        this.errorCode = code;
        this.requestId = requestId;
    }

    public LogException(int httpCode, String code, String message, String requestId) {
        super(message);
        this.httpCode = httpCode;
        this.errorCode = code;
        this.requestId = requestId;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return super.getMessage();
    }

    public String getRequestId() {
        return this.requestId;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
}
