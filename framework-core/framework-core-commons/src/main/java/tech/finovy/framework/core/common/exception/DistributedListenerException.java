package tech.finovy.framework.core.common.exception;

/**
 * @author: Dtype.Huang
 * @date: 2020/4/27 17:43
 */
public class DistributedListenerException extends RuntimeException {

    private static final long serialVersionUID = 4707127779619100807L;

    public DistributedListenerException(Throwable cause) {
        super(cause);
    }

    public DistributedListenerException(String message) {
        super(message);
    }
}
