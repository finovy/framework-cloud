package tech.finovy.framework.disruptor.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.common.core.loader.LoadLevel;
import tech.finovy.framework.common.core.loader.Scope;
import tech.finovy.framework.disruptor.core.DisruptorEventConstant;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.listener.AbstractDisruptorListener;

@LoadLevel(name = "defaultDisruptorClearListener", order = 1, scope = Scope.SINGLETON)
public class DefaultDisruptorClearListener extends AbstractDisruptorListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDisruptorClearListener.class);

    @Override
    public void onEvent(DisruptorEvent event, int handlerId) {
        if (configuration.isDebug()) {
            LOGGER.info("DisruptorEventType:{},application:{},eventTopic:{},eventTags:{},handlerId:{},transactionId:{},eventId:{}", event.getDisruptorEventType(), event.getApplication(), event.getEventTopic(), event.getEventTags(), handlerId, event.getTransactionId(), event.getEventId());
        }
    }

    @Override
    public String getType() {
        return DisruptorEventConstant.SYS_GLOBAL_CLEAR_LISTENER_TYPE;
    }

}
