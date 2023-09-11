package tech.finovy.framework.core.disruptor;

import tech.finovy.framework.core.common.loader.EnhancedServiceLoader;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.spi.DisruptorEngine;

public class DisruptorEngineFactory {
    private DisruptorEngineFactory() {
    }

    private static DisruptorEngine INSTANCE = null;

    public static DisruptorEngine getDisruptorEngine() {
        return getDisruptorEngine(new DisruptorEventConfiguration());
    }

    public static DisruptorEngine getDisruptorEngine(DisruptorEventConfiguration configuration) {
        if (INSTANCE == null) {
            Class<?>[] arrType = {DisruptorEventConfiguration.class};
            Object[] args = {configuration};
            INSTANCE = EnhancedServiceLoader.load(DisruptorEngine.class, DisruptorConstant.DISRUPTOR, arrType, args);
        }
        return INSTANCE;
    }
}
