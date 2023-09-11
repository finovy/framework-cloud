package tech.finovy.framework.core.disruptor.provider;

import tech.finovy.framework.core.common.loader.LoadLevel;
import tech.finovy.framework.core.common.loader.Scope;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConstant;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.core.disruptor.core.listener.AbstractDisruptorListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dtype.huang
 */
@Slf4j
@LoadLevel(name = "defaultDisruptorClearListener", order = 1, scope = Scope.SINGLETON)
public class DefaultDisruptorClearListener extends AbstractDisruptorListener {
    @Override
    public void onEvent(DisruptorEvent event, int handlerId) {
        if (configuration.isDebug()) {
            log.info("DisruptorEventType:{},application:{},eventTopic:{},eventTags:{},handlerId:{},transactionId:{},eventId:{}", event.getDisruptorEventType(), event.getApplication(), event.getEventTopic(), event.getEventTags(), handlerId, event.getTransactionId(), event.getEventId());
        }
    }

    @Override
    public String getType() {
        return DisruptorEventConstant.SYS_GLOBAL_CLEAR_LISTENER_TYPE;
    }
}
