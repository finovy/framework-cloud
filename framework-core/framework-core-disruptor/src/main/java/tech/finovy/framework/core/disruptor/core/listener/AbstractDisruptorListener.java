package tech.finovy.framework.core.disruptor.core.listener;


import tech.finovy.framework.core.common.chain.AbstractChainListener;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.spi.ProcessInterface;

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
