package tech.finovy.framework.rocketmq.producer;

import tech.finovy.framework.core.common.exception.ShouldNeverHappenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketMqProducerHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqProducerContext.class);

    private RocketMqProducerHolder() {

    }

    /**
     * Get ShardingEngine.
     *
     * @return the ShardingEngine
     */
    public static RocketMqProducerContext get() {
        if (SingletonHolder.INSTANCE == null) {
            throw new ShouldNeverHappenException("RocketMqProducerContext is NOT ready!");
        }
        return SingletonHolder.INSTANCE;
    }

    public static void set(RocketMqProducerContext mock) {
        SingletonHolder.INSTANCE = mock;
    }

    private static class SingletonHolder {

        private static RocketMqProducerContext INSTANCE;

        static {
            INSTANCE = new RocketMqProducerContext();
        }
    }
}
