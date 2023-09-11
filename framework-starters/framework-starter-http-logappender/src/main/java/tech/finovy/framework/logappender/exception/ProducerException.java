package tech.finovy.framework.logappender.exception;

public class ProducerException extends Exception {
    private static final long serialVersionUID = 7634733936355215455L;

    public ProducerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProducerException(String message) {
        super(message);
    }

    public ProducerException(Throwable cause) {
        super(cause);
    }

    public ProducerException() {
        super();
    }
}
