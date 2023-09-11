package tech.finovy.framework.logappender.exception;

public class TimeoutException extends ProducerException {
    private static final long serialVersionUID = -3017093254252993308L;

    public TimeoutException() {
        super();
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(Throwable cause) {
        super(cause);
    }
}
