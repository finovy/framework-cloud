package tech.finovy.framework.scheduler.listener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSchedulerExecutorGlobalListener extends AbstractSchedulerExecutorListener {

    public static final String SCHEDULER_EXECUTOR_GLOBAL_LISTENER ="SCHEDULER_EXECUTOR_GLOBAL_LISTENER";

    @Override
    public String getType() {
        return SCHEDULER_EXECUTOR_GLOBAL_LISTENER;
    }
}
