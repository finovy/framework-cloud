package tech.finovy.framework.scheduler.listener;


import tech.finovy.framework.listener.DistributedListener;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;

public interface SchedulerExecutorListener extends DistributedListener {
    RemoteJobExecuteResult trigger(RemoteJobExecuteConfig config);
}
