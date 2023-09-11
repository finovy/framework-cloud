package tech.finovy.framework.core.common.exception;

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
