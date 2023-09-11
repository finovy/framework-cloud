package tech.finovy.framework.logappender.push.internals;

import org.apache.http.conn.HttpClientConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class IdleConnectionReaper extends Thread {
    private static final int REAP_INTERVAL_MILLISECONDS = 5 * 1000;
    private static final ArrayList<HttpClientConnectionManager> CONNECTION_MANAGERS = new ArrayList<>();

    private static IdleConnectionReaper instance;
    private static long idleConnectionTime = 60 * 1000L;
    private volatile boolean shuttingDown = false;

    private IdleConnectionReaper() {
        super("idle_connection_reaper");
        setDaemon(true);
    }

    public static synchronized void registerConnectionManager(HttpClientConnectionManager connectionManager) {
        if (instance == null) {
            instance = new IdleConnectionReaper();
            instance.start();
        }
        CONNECTION_MANAGERS.add(connectionManager);
    }

    public static synchronized void removeConnectionManager(HttpClientConnectionManager connectionManager) {
        CONNECTION_MANAGERS.remove(connectionManager);
        if (CONNECTION_MANAGERS.isEmpty()) {
            shutdown();
        }
    }

    public static synchronized void shutdown() {
        if (instance != null) {
            instance.markShuttingDown();
            instance.interrupt();
            CONNECTION_MANAGERS.clear();
            instance = null;
        }
    }

    public static synchronized void setIdleConnectionTime(long idletime) {
        idleConnectionTime = idletime;
    }

    private void markShuttingDown() {
        shuttingDown = true;
    }

    @Override
    public void run() {
        while (!shuttingDown) {
            try {
                Thread.sleep(REAP_INTERVAL_MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            try {
                List<HttpClientConnectionManager> connectionManagers = null;
                synchronized (IdleConnectionReaper.class) {
                    connectionManagers = (List<HttpClientConnectionManager>) IdleConnectionReaper.CONNECTION_MANAGERS
                            .clone();
                }
                for (HttpClientConnectionManager connectionManager : connectionManagers) {
                    try {
                        connectionManager.closeExpiredConnections();
                        connectionManager.closeIdleConnections(idleConnectionTime, TimeUnit.MILLISECONDS);
                    } catch (Exception ex) {
                        // ignore
                    }
                }
            } catch (Throwable t) {
                // ignore
            }
        }
    }

}
