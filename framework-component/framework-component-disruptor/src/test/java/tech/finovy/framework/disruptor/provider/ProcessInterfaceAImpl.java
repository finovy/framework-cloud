package tech.finovy.framework.disruptor.provider;

import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.spi.ProcessInterface;

public class ProcessInterfaceAImpl implements ProcessInterface {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getType() {
        return "A";
    }

    @Override
    public String getKey() {
        return "A";
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
