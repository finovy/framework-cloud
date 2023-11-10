package tech.finovy.framework.scheduler.client;


import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;
import tech.finovy.framework.scheduler.entity.RemoteJobProcessConfig;

import java.util.List;

public interface SchedulerExecutorService<T> {

    RemoteJobExecuteResult trigger(RemoteJobExecuteConfig config);

    List<T> fetch(RemoteJobExecuteConfig config);

    RemoteJobExecuteResult process(RemoteJobProcessConfig<T> config);
}
