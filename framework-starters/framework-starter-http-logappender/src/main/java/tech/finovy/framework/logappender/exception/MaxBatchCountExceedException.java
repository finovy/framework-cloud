package tech.finovy.framework.logappender.exception;

public class MaxBatchCountExceedException extends ProducerException {
    private static final long serialVersionUID = 4144838718677603095L;

    public MaxBatchCountExceedException() {
        super();
    }

    public MaxBatchCountExceedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxBatchCountExceedException(String message) {
        super(message);
    }

    public MaxBatchCountExceedException(Throwable cause) {
        super(cause);
    }
}
