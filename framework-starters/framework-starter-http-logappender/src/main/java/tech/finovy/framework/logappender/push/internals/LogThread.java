package tech.finovy.framework.logappender.push.internals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dtype.huang
 */
public class LogThread extends Thread {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public LogThread(final String name, boolean daemon) {
        super(name);
        configureThread(name, daemon);
    }

    public LogThread(final String name, Runnable runnable, boolean daemon) {
        super(runnable, name);
        configureThread(name, daemon);
    }

    public static LogThread daemon(final String name, Runnable runnable) {
        return new LogThread(name, runnable, true);
    }

    public static LogThread nonDaemon(final String name, Runnable runnable) {
        return new LogThread(name, runnable, false);
    }

    private void configureThread(final String name, boolean daemon) {
        setDaemon(daemon);
        setUncaughtExceptionHandler(
                (t, e) -> LOGGER.error("Uncaught error in thread, name={}, e=", name, e));
    }
}
