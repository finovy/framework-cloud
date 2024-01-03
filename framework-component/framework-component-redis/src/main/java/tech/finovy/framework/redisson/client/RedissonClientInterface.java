package tech.finovy.framework.redisson.client;

import org.redisson.api.RedissonClient;
import tech.finovy.framework.redisson.config.RedissonConfiguration;

/**
 * since 0.2.0-SNAPSHOT
 */
@Deprecated(since = "0.2.0-SNAPSHOT", forRemoval = true)
public interface RedissonClientInterface extends RedissonClient {
    void setRedissonConfiguration(RedissonConfiguration configuration, int version);
    boolean isDebug();
    int getVersion();
    String createKey(String key,String type,boolean skipPrefix);
    String createKey(String key,String type);
    String createKey(String key);
    String createMapKey(String key);
    String createLocalCacheMapKey(String key);
    int calHash(String key);
    int calKeyHash(String key);
}
