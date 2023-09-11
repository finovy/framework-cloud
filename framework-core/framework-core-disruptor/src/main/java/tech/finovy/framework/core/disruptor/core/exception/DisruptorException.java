package tech.finovy.framework.core.disruptor.core.exception;

/**
 * @author: Dtype.Huang
 * @date: 2020/4/27 17:43
 */
public class DisruptorException extends RuntimeException {
    private static final long serialVersionUID = 4902033891210380143L;

    public DisruptorException(Throwable cause) {
        super(cause);
    }

    public DisruptorException(String message) {
        super(message);
    }
}
