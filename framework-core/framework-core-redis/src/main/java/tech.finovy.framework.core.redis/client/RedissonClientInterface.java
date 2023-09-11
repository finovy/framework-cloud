package tech.finovy.framework.core.redis.client;

import org.redisson.api.RedissonClient;

/**
 * @author: Dtype.Huang
 * @date: 2020/6/12 16:20
 */
public interface RedissonClientInterface extends RedissonClient {
    void setRedissonConfiguration(RedissonConfiguration redissonConfiguration, int version);

    boolean isDebug();

    int getVersion();

    String createKey(String key, String type, boolean skipPrefix);

    String createKey(String key, String type);

    String createKey(String key);

    String createMapKey(String key);

    String createLocalCacheMapKey(String key);

    int calHash(String key);

    int calKeyHash(String key);
}
