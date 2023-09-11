package tech.finovy.framework.core.redis.listener;

import tech.finovy.framework.core.common.loader.LoadLevel;
import tech.finovy.framework.core.common.loader.Scope;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.core.disruptor.core.listener.AbstractDisruptorListener;
import tech.finovy.framework.core.redis.ShardingEngineRedisConext;
import tech.finovy.framework.core.redis.ShardingEngineRedisConstant;
import tech.finovy.framework.core.redis.holder.ShardingEngineRedisConextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@LoadLevel(name = ShardingEngineRedisConstant.SHUTDOWN_REDIS_EVENT_LISTENER_TYPE, order = 1, scope = Scope.SINGLETON)
public class ShutdownHandlerListener extends AbstractDisruptorListener {
    private ShardingEngineRedisConext conext = ShardingEngineRedisConextHolder.get();
    @Autowired
    private DisruptorEventConfiguration configuration;

    @Override
    public String getType() {
        return ShardingEngineRedisConstant.SHUTDOWN_REDIS_EVENT_LISTENER_TYPE;
    }

    @Override
    public void onEvent(DisruptorEvent event, int handlerId) {
        conext.shutdown((int) event.getEvent());
    }
}
