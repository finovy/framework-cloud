package tech.finovy.framework.common.core.executor;

public interface Callback<T> {

    /**
     * Execute t.
     *
     * @return the t
     * @throws Throwable the throwable
     */
    T execute() throws Throwable;
}

