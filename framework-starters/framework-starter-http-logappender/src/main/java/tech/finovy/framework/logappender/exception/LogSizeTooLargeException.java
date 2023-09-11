package tech.finovy.framework.logappender.exception;

public class LogSizeTooLargeException extends ProducerException {
    private static final long serialVersionUID = -6378852704099849741L;

    public LogSizeTooLargeException() {
        super();
    }

    public LogSizeTooLargeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogSizeTooLargeException(String message) {
        super(message);
    }

    public LogSizeTooLargeException(Throwable cause) {
        super(cause);
    }
}
