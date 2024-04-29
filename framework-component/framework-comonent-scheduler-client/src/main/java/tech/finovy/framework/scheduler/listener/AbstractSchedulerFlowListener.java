package tech.finovy.framework.scheduler.listener;

import tech.finovy.framework.listener.AbstractDistributedListener;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;
import tech.finovy.framework.scheduler.entity.RemoteJobProcessConfig;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSchedulerFlowListener<T> extends AbstractDistributedListener implements SchedulerFlowListener<T> {

    @Override
    public List<T> fetch(RemoteJobExecuteConfig config){
        return Collections.emptyList();
    }

    @Override
    public RemoteJobExecuteResult process(RemoteJobProcessConfig<T> input) {
        RemoteJobExecuteResult result = new RemoteJobExecuteResult();
        result.setSuccess(false);
        result.setResult("SchedulerFlowListener Not implement!");
        return result;
    }
}
