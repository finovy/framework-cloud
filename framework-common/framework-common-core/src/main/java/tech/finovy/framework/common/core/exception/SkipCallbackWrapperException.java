package tech.finovy.framework.common.core.exception;

public class SkipCallbackWrapperException extends RuntimeException {

    public SkipCallbackWrapperException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        // do nothing
        return null;
    }
}
