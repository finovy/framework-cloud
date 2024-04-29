package tech.finovy.framework.config.nacos.listener.entity;


import tech.finovy.framework.nacos.entity.AbstractNacosConfigGroup;

import java.io.Serializable;
import java.util.List;

public class TaskFlowGroup extends AbstractNacosConfigGroup<TaskFlowEntity> implements Serializable {
    private boolean queue;
    private boolean database;
    private boolean cache;
    private boolean rocket;
    private List<TaskFlowEntity> taskFlow;

    @Override
    public List getEntity() {
        return taskFlow;
    }

    public List<TaskFlowEntity> getTaskFlow() {
        return taskFlow;
    }

    public void setTaskFlow(List<TaskFlowEntity> taskFlow) {
        this.taskFlow = taskFlow;
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
}
