package tech.finovy.framework.redisson.config;

public class RedissonConfiguration {
    private String keyVersion;
    private String keyPrefix;
    private long keyDefaultTtl;
    private int keyHashModSize;
    private boolean encry;
    private boolean debug;
    private long connectDelay;

    public String getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(String keyVersion) {
        this.keyVersion = keyVersion;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public long getKeyDefaultTtl() {
        return keyDefaultTtl;
    }

    public void setKeyDefaultTtl(long keyDefaultTtl) {
        this.keyDefaultTtl = keyDefaultTtl;
    }

    public int getKeyHashModSize() {
        return keyHashModSize;
    }

    public void setKeyHashModSize(int keyHashModSize) {
        this.keyHashModSize = keyHashModSize;
    }

    public boolean isEncry() {
        return encry;
    }

    public void setEncry(boolean encry) {
        this.encry = encry;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public long getConnectDelay() {
        return connectDelay;
    }

    public void setConnectDelay(long connectDelay) {
        this.connectDelay = connectDelay;
    }
}
