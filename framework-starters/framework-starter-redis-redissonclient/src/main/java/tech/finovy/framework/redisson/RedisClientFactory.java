package tech.finovy.framework.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.disruptor.DisruptorEngineFactory;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.redisson.config.RedissonConfiguration;
import tech.finovy.framework.redisson.holder.RedisContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientFactory.class);

    private final static Map<Integer, RedissonClient> SHUTDOWN = new HashMap<>();
    private static final DisruptorEngine disruptorEngine = DisruptorEngineFactory.getDisruptorEngine();
    private static RedissonClient client = null;

    public static RedissonClient init(RedisContext context,RedissonConfiguration redissonConfiguration, Config config, int version) {
        LOGGER.info("RedisClient init ...............");
        RedissonClient clientInterface = client;
        if (context.getClient() != null) {
            // close past conn
            SHUTDOWN.put(context.getVersion(), clientInterface);
            DisruptorEvent event = new DisruptorEvent();
            event.setDisruptorEvent(context.getVersion());
            event.setEventId(context.getVersion());
            event.setEventTags("shutdownRedisTag");
            event.setEventTopic("shutdownRedisTopic");
            event.setTransactionId(String.valueOf(context.getVersion()));
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
            client = Redisson.create(config);
            context.setRedissonConfiguration(redissonConfiguration, version);
            context.setClient(client);
            LOGGER.info("RedisClient connected successfully...............");
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void shutdown(int version) {
        RedissonClient clientInterface = SHUTDOWN.get(version);
        if (clientInterface != null) {
            clientInterface.shutdown(0, 30, TimeUnit.SECONDS);
            SHUTDOWN.remove(version);
        }
    }
}
