package tech.finovy.framework.core.common.executor;

public interface Callback<T> {

    /**
     * Execute t.
     *
     * @return the t
     * @throws Throwable the throwable
     */
    T execute() throws Throwable;
}

