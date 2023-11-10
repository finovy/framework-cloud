package tech.finovy.framework.config.nacos.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.finovy.framework.common.core.listener.AbstractShardingEngineSchedulerRefreshListener;


@Slf4j
@Component
public class ShardingEngineSelfCheckListenerSlave extends AbstractShardingEngineSchedulerRefreshListener {


    @Override
    public void trigger(long trigger) {
    }

    @Override
    public void startup() {
        refresh();
    }

    private void refresh() {
    }

}
