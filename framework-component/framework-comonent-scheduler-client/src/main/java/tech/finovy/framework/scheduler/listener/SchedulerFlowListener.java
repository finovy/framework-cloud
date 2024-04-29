package tech.finovy.framework.scheduler.listener;


import tech.finovy.framework.listener.DistributedListener;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;
import tech.finovy.framework.scheduler.entity.RemoteJobProcessConfig;

import java.util.List;

public interface SchedulerFlowListener<T> extends DistributedListener {

    List<T> fetch(RemoteJobExecuteConfig config);

    RemoteJobExecuteResult process(RemoteJobProcessConfig<T> input);
}
