package tech.finovy.framework.logappender.conf;

import tech.finovy.framework.logappender.push.http.Protocol;

public class ClientConfiguration {
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60 * 1000;
    public static final long DEFAULT_IDLE_CONNECTION_TIME = 60 * 1000L;
    public static final int DEFAULT_VALIDATE_AFTER_INACTIVITY = 2 * 1000;
    public static final int DEFAULT_THREAD_POOL_WAIT_TIME = 60 * 1000;
    public static final int DEFAULT_REQUEST_TIMEOUT = 60 * 1000;
    public static final boolean DEFAULT_USE_REAPER = false;
    protected int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
    protected Protocol protocol = Protocol.HTTP;
    protected String proxyHost = null;
    protected int proxyPort = -1;
    protected String proxyUsername = null;
    protected String proxyPassword = null;
    protected String proxyDomain = null;
    protected String proxyWorkstation = null;
    protected long idleConnectionTime = DEFAULT_IDLE_CONNECTION_TIME;
    protected int requestTimeout = DEFAULT_REQUEST_TIMEOUT;
    protected boolean useReaper = DEFAULT_USE_REAPER;
    private int maxConnections = 50;
    private int socketTimeout = 50 * 1000;
    private int connectionTimeout = 50 * 1000;
    private boolean requestTimeoutEnabled = false;

    public ClientConfiguration() {
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getProxyDomain() {
        return proxyDomain;
    }

    public String getProxyWorkstation() {
        return proxyWorkstation;
    }

    public long getIdleConnectionTime() {
        return idleConnectionTime;
    }

    public void setIdleConnectionTime(long idleConnectionTime) {
        this.idleConnectionTime = idleConnectionTime;
    }

    public int getValidateAfterInactivity() {
        return DEFAULT_VALIDATE_AFTER_INACTIVITY;
    }

    public boolean isRequestTimeoutEnabled() {
        return requestTimeoutEnabled;
    }

    public void setRequestTimeoutEnabled(boolean requestTimeoutEnabled) {
        this.requestTimeoutEnabled = requestTimeoutEnabled;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public boolean isUseReaper() {
        return useReaper;
    }

    public void setUseReaper(boolean useReaper) {
        this.useReaper = useReaper;
    }
}
