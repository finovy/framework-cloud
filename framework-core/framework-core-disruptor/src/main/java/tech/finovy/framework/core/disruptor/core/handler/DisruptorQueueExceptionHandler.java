package tech.finovy.framework.core.disruptor.core.handler;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.core.DisruptorEventContext;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEventTranslator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dtype.huang
 */
@Slf4j
public class DisruptorQueueExceptionHandler implements ExceptionHandler<DisruptorEvent> {
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
        log.error("DisruptorType:{},Exception:{},EventId:{},Topic:{},Tag:{},Try:{}", event.getDisruptorEventType(), ex.toString(), event.getEventId(), event.getEventTopic(), event.getEventTags(), event.getEventRetryCount());
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
            log.info("Retry disruptorType:{},eventId:{},tryRemain:{}", event.getDisruptorEventType(), event.getEventId(), event.getEventRetryCount());
        }
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        log.error("handleOnStartException:{}", ex.toString());
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error("handleOnShutdownException:{}", ex.toString());
    }
}
