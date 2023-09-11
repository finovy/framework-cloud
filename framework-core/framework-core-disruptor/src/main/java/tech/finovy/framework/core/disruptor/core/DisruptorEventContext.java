package tech.finovy.framework.core.disruptor.core;

import com.lmax.disruptor.RingBuffer;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dtype.huang
 */
@Getter
@Setter
public class DisruptorEventContext {
    private RingBuffer<DisruptorEvent> ringBuffer;
}
