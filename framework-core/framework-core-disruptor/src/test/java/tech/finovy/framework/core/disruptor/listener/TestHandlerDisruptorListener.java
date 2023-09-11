package tech.finovy.framework.core.disruptor.listener;

import tech.finovy.framework.core.common.loader.LoadLevel;
import tech.finovy.framework.core.common.loader.Scope;
import tech.finovy.framework.core.disruptor.TeatDisruptorEventConstant;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.core.disruptor.core.listener.AbstractDisruptorListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@LoadLevel(name = "TestHandlerDisruptorListener", order = 1, scope = Scope.SINGLETON)
public class TestHandlerDisruptorListener extends AbstractDisruptorListener {
    @Autowired
    private DisruptorEventConfiguration configuration;

    @Override
    public void onEvent(DisruptorEvent event, int handlerId) {
        log.info("Handler----------------->{}", handlerId);
    }

    @Override
    public String getType() {
        return TeatDisruptorEventConstant.TEST_EVENT_LISTENER_TYPE;
    }
}
