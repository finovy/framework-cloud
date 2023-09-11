package tech.finovy.framework.core.disruptor.spi;

import tech.finovy.framework.core.common.chain.ChainListener;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;

import java.io.Serializable;

public interface ProcessInterface<T extends Serializable> extends ChainListener {
    void onEvent(DisruptorEvent<T> event, int handlerId);

    void setDisruptorEventConfiguration(DisruptorEventConfiguration configuration);
}
