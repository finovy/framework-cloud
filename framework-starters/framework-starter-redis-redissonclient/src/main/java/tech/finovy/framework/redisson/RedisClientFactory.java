package tech.finovy.framework.redisson;

import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.disruptor.DisruptorEngineFactory;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.redisson.client.RedissonClientInterface;
import tech.finovy.framework.redisson.client.RedissonExtImpl;
import tech.finovy.framework.redisson.config.RedissonConfiguration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientFactory.class);

    private final static Map<Integer, RedissonClientInterface> SHUTDOWN = new HashMap<>();
    private static final DisruptorEngine disruptorEngine = DisruptorEngineFactory.getDisruptorEngine();
    private static RedissonClientInterface client = null;

    public static RedissonClientInterface init(RedissonConfiguration redissonConfiguration, Config config, int version) {
        LOGGER.info("RedisClient init ...............");
        RedissonClientInterface clientInterface = client;
        if (clientInterface != null) {
            // close past conn
            SHUTDOWN.put(clientInterface.getVersion(), clientInterface);
            DisruptorEvent event = new DisruptorEvent();
            event.setDisruptorEvent(clientInterface.getVersion());
            event.setEventId(clientInterface.getVersion());
            event.setEventTags("shutdownRedisTag");
            event.setEventTopic("shutdownRedisTopic");
            event.setTransactionId(String.valueOf(clientInterface.getVersion()));
            event.setDisruptorEventType(RedisConstant.SHUTDOWN_REDIS_EVENT_LISTENER_TYPE);
            disruptorEngine.post(event);
        }
        try {
            if (config.isSentinelConfig()) {
                Method method = config.getClass().getDeclaredMethod("getSentinelServersConfig");
                method.setAccessible(true);
                SentinelServersConfig s = (SentinelServersConfig) method.invoke(config);
                LOGGER.info("SentinelConfig:{}", s.getSentinelAddresses());
            }
            // else if (config.isClusterConfig()) {
            client = RedissonExtImpl.create(config);
            client.setRedissonConfiguration(redissonConfiguration, version);
            LOGGER.info("RedisClient connected successfully...............");
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void shutdown(int version) {
        RedissonClientInterface clientInterface = SHUTDOWN.get(version);
        if (clientInterface != null) {
            clientInterface.shutdown(0, 30, TimeUnit.SECONDS);
            SHUTDOWN.remove(version);
        }
    }
}
