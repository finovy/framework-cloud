package tech.finovy.framework.disruptor.core.exception;

import java.io.Serial;

public class DisruptorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4902033891210380143L;

    public DisruptorException(Throwable cause) {
        super(cause);
    }

    public DisruptorException(String message) {
        super(message);
    }
}
