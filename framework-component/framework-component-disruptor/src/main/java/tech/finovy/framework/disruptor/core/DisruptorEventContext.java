package tech.finovy.framework.disruptor.core;

import com.lmax.disruptor.RingBuffer;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;

public class DisruptorEventContext {
    private RingBuffer<DisruptorEvent> ringBuffer;

    public RingBuffer<DisruptorEvent> getRingBuffer() {
        return ringBuffer;
    }

    public void setRingBuffer(RingBuffer<DisruptorEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
}
