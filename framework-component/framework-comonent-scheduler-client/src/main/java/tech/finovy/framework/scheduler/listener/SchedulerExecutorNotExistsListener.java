package tech.finovy.framework.scheduler.listener;

import lombok.extern.slf4j.Slf4j;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;

@Slf4j
public class SchedulerExecutorNotExistsListener extends AbstractSchedulerExecutorListener {

    public static final String NOT_EXISTS_SCHEDULER_EXECUTOR_LISTENER ="NotExistsSchedulerExecutorListener";
    @Override
    public RemoteJobExecuteResult trigger(RemoteJobExecuteConfig config) {
        log.warn("Trigger:{} NOT EXISTS {}", config.getJobKey(), config.getJobName());
        return super.trigger(config);
    }

    @Override
    public String getType() {
        return NOT_EXISTS_SCHEDULER_EXECUTOR_LISTENER;
    }
}
