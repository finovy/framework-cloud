package tech.finovy.framework.core.common.exception;


public class NotSupportYetException extends RuntimeException {

    /**
     * Instantiates a new Not support yet exception.
     */
    public NotSupportYetException() {
        this("currently not supported, may be supported in future");
    }

    /**
     * Instantiates a new Not support yet exception.
     *
     * @param message the message
     */
    public NotSupportYetException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Not support yet exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public NotSupportYetException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Not support yet exception.
     *
     * @param cause the cause
     */
    public NotSupportYetException(Throwable cause) {
        super(cause);
    }
}
