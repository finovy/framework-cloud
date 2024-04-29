package tech.finovy.framework.redisson.listener;

import lombok.extern.slf4j.Slf4j;
import tech.finovy.framework.common.core.loader.LoadLevel;
import tech.finovy.framework.common.core.loader.Scope;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.listener.AbstractDisruptorListener;
import tech.finovy.framework.redisson.RedisClientFactory;
import tech.finovy.framework.redisson.RedisConstant;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

@Slf4j
@LoadLevel(name = RedisConstant.SHUTDOWN_REDIS_EVENT_LISTENER_TYPE, order = 1, scope = Scope.SINGLETON)
public class ShutdownHandlerListener extends AbstractDisruptorListener {
    private final RedisContext context = RedisContextHolder.get();

    @Override
    public String getType() {
        return RedisConstant.SHUTDOWN_REDIS_EVENT_LISTENER_TYPE;
    }

    @Override
    public void onEvent(DisruptorEvent event, int handlerId) {
        RedisClientFactory.shutdown((int) event.getEvent());
    }
}
