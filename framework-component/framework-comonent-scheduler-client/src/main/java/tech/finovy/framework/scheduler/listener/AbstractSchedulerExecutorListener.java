package tech.finovy.framework.scheduler.listener;


import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import tech.finovy.framework.listener.AbstractDistributedListener;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;

public abstract class AbstractSchedulerExecutorListener extends AbstractDistributedListener implements SchedulerExecutorListener {
    @SneakyThrows
    @Override
    public RemoteJobExecuteResult trigger(RemoteJobExecuteConfig config) {
        RemoteJobExecuteResult result = new RemoteJobExecuteResult();
        BeanUtils.copyProperties(result, config);
        return result;
    }
}
