package tech.finovy.framework.disruptor.provider;

import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.listener.AbstractDisruptorListener;
import tech.finovy.framework.disruptor.spi.ProcessInterface;

public class ProcessInterfaceCImpl extends AbstractDisruptorListener {
    @Override
    public int getOrder() {
        return 0;
    }


    @Override
    public String getKey() {
        return "B";
    }

    @Override
    public String getIndex() {
        return null;
    }

    @Override
    public void setIndex(String index) {

    }

    @Override
    public boolean isEnable() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void init() {

    }

    @Override
    public void onEvent(DisruptorEvent event, int handlerId) {

    }

    @Override
    public void setDisruptorEventConfiguration(DisruptorEventConfiguration configuration) {

    }
}
