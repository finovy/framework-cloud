package tech.finovy.framework.disruptor.core.listener;


import tech.finovy.framework.common.core.chain.AbstractChainListener;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.spi.ProcessInterface;

public abstract class AbstractDisruptorListener extends AbstractChainListener implements ProcessInterface {
    protected String type;
    protected DisruptorEventConfiguration configuration;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setDisruptorEventConfiguration(DisruptorEventConfiguration configuration) {
        this.configuration = configuration;
    }


}
