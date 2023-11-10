package tech.finovy.framework.config.nacos.listener.entity;


import tech.finovy.framework.nacos.entity.AbstractNacosConfigEntity;

import java.io.Serializable;
import java.util.List;

public class TaskFlowEntity extends AbstractNacosConfigEntity implements Serializable {
    private String key;
    private List<TaskCallEntity> call;
    private int timeout;
    private boolean log;
    private boolean lock;
    private boolean debug;
    private boolean queue;
    private boolean database;
    private boolean cache;
    private boolean rocket;
    private boolean async;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public List<TaskCallEntity> getCall() {
        return call;
    }

    public void setCall(List<TaskCallEntity> call) {
        this.call = call;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean logEnable) {
        this.log = logEnable;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isQueue() {
        return queue;
    }

    public void setQueue(boolean queue) {
        this.queue = queue;
    }

    public boolean isDatabase() {
        return database;
    }

    public void setDatabase(boolean database) {
        this.database = database;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isRocket() {
        return rocket;
    }

    public void setRocket(boolean rocket) {
        this.rocket = rocket;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
