package tech.finovy.framework.disruptor.spi;

import tech.finovy.framework.common.core.chain.ChainListener;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;

import java.io.Serializable;

public interface ProcessInterface<T extends Serializable> extends ChainListener {
    void onEvent(DisruptorEvent<T> event, int handlerId);

    void setDisruptorEventConfiguration(DisruptorEventConfiguration configuration);
}
