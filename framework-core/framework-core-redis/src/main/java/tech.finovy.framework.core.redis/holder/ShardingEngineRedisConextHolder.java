package tech.finovy.framework.core.redis.holder;


import tech.finovy.framework.core.common.exception.ShouldNeverHappenException;
import tech.finovy.framework.core.redis.ShardingEngineRedisConext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShardingEngineRedisConextHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingEngineRedisConextHolder.class);

    private ShardingEngineRedisConextHolder() {

    }

    /**
     * Get ShardingEngineGlobalConfiguration.
     *
     * @return the ShardingEngineGlobalConfiguration
     */
    public static ShardingEngineRedisConext get() {
        if (SingletonHolder.INSTANCE == null) {
            throw new ShouldNeverHappenException("ShardingEngineConext is NOT ready!");
        }
        return SingletonHolder.INSTANCE;
    }

    /**
     * Set a TM instance.
     *
     * @param mock commonly used for test mocking
     */
    public static void set(ShardingEngineRedisConext mock) {
        SingletonHolder.INSTANCE = mock;
    }

    private static class SingletonHolder {

        private static ShardingEngineRedisConext INSTANCE = null;

        static {
            try {
                INSTANCE = new ShardingEngineRedisConext();
                LOGGER.info("ShardingEngineConext Singleton {}", INSTANCE);
            } catch (Throwable anyEx) {
                LOGGER.error("Failed to load ShardingEngineConext Singleton! ", anyEx);
            }
        }
    }
}
