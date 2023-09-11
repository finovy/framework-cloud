package tech.finovy.framework.core.redis;

import tech.finovy.framework.core.disruptor.DisruptorEngineFactory;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.core.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.core.redis.client.RedissonClientInterface;
import tech.finovy.framework.core.redis.client.RedissonConfiguration;
import tech.finovy.framework.core.redis.client.RedissonExtImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShardingEngineRedisConext {
    private final static Map<Integer, RedissonClientInterface> SHUTDOWN = new HashMap<>();
    private final DisruptorEngine disruptorEngine = DisruptorEngineFactory.getDisruptorEngine();
    private RedissonClientInterface client = null;

    public void refreshClient(RedissonConfiguration redissonConfiguration, Config config, int version) {
        log.info("RedisClient init ...............");
        RedissonClientInterface clientInterface = client;
        if (clientInterface != null) {
            SHUTDOWN.put(clientInterface.getVersion(), clientInterface);
        }
        try {
            if (config.isSentinelConfig()) {
                Method method = config.getClass().getDeclaredMethod("getSentinelServersConfig");
                method.setAccessible(true);
                SentinelServersConfig s = (SentinelServersConfig) method.invoke(config);
                log.info("SentinelConfig:{}", s.getSentinelAddresses());
            } else if (config.isClusterConfig()) {

            }
            client = RedissonExtImpl.create(config);
            client.setRedissonConfiguration(redissonConfiguration, version);
            log.info("RedisClient connected successfully...............");
            if (clientInterface != null) {
                DisruptorEvent event = new DisruptorEvent();
                event.setDisruptorEvent(clientInterface.getVersion());
                event.setEventId(clientInterface.getVersion());
                event.setEventTags("shutdownRedisTag");
                event.setEventTopic("shutdownRedisTopic");
                event.setTransactionId(String.valueOf(clientInterface.getVersion()));
                event.setDisruptorEventType(ShardingEngineRedisConstant.SHUTDOWN_REDIS_EVENT_LISTENER_TYPE);
                disruptorEngine.post(event);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public RedissonClientInterface getClient() {
        return client;
    }

    public void shutdown(int version) {
        RedissonClientInterface clientInterface = SHUTDOWN.get(version);
        if (clientInterface != null) {
            clientInterface.shutdown(0, 30, TimeUnit.SECONDS);
            SHUTDOWN.remove(version);
        }
    }
}
