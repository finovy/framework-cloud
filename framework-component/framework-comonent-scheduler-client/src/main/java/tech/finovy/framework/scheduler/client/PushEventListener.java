package tech.finovy.framework.scheduler.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;
import tech.finovy.framework.scheduler.listener.AbstractSchedulerExecutorListener;

@Slf4j
@Service
public class PushEventListener extends AbstractSchedulerExecutorListener {

    @Override
    public RemoteJobExecuteResult trigger(RemoteJobExecuteConfig config) {
        log.debug("Trigger:{} Config:{}", config.getJobKey(), config.getJobName());
        return super.trigger(config);
    }

    @Override
    public String getType() {
        return "pushMQEvent";
    }
}
