package tech.finovy.framework.scheduler.listener;

import lombok.extern.slf4j.Slf4j;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;
import tech.finovy.framework.scheduler.entity.RemoteJobProcessConfig;

import java.util.List;

@Slf4j
public class SchedulerFlowNotExistsListener<T> extends AbstractSchedulerFlowListener<T> {

    public static final String NOT_EXISTS_SCHEDULER_FLOW_LISTENER ="NotExistsSchedulerFlowListener";

    @Override
    public List<T> fetch(RemoteJobExecuteConfig config) {
        return super.fetch(config);
    }

    @Override
    public RemoteJobExecuteResult process(RemoteJobProcessConfig<T> input) {
        return super.process(input);
    }

    @Override
    public String getType() {
        return NOT_EXISTS_SCHEDULER_FLOW_LISTENER;
    }
}
