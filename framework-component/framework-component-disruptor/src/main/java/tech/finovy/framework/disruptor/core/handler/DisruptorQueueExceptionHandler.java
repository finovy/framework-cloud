package tech.finovy.framework.disruptor.core.handler;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventContext;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.event.DisruptorEventTranslator;

public class DisruptorQueueExceptionHandler implements ExceptionHandler<DisruptorEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorQueueExceptionHandler.class);


    private final DisruptorEventConfiguration configuration;
    private final DisruptorEventContext context;
    private final DisruptorEventTranslator publisher;

    public DisruptorQueueExceptionHandler(DisruptorEventConfiguration configuration, DisruptorEventTranslator publisher, DisruptorEventContext context) {
        this.configuration = configuration;
        this.context = context;
        this.publisher = publisher;
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, DisruptorEvent event) {
        LOGGER.error("DisruptorType:{},Exception:{},EventId:{},Topic:{},Tag:{},Try:{}", event.getDisruptorEventType(), ex.getMessage(), event.getEventId(), event.getEventTopic(), event.getEventTags(), event.getEventRetryCount());
        if (event.getEventRetryCount() < 1) {
            event.setDisruptorEvent(null);
            return;
        }
        RingBuffer<DisruptorEvent> ringBuffer = context.getRingBuffer();
        if (ringBuffer == null) {
            event.setDisruptorEvent(null);
            return;
        }
        event.setEventRetryCount(event.getEventRetryCount() - 1);
        ringBuffer.publishEvent(publisher, event.getTransactionId(), event.getEventId(), event);
        if (configuration.isDebug()) {
            LOGGER.info("Retry disruptorType:{},eventId:{},tryRemain:{}", event.getDisruptorEventType(), event.getEventId(), event.getEventRetryCount());
        }
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        LOGGER.error("handleOnStartException:{}", ex.toString());
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        LOGGER.error("handleOnShutdownException:{}", ex.toString());
    }
}
