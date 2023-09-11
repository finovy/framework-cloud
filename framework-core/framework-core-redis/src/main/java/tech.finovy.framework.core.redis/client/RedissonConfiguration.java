package tech.finovy.framework.core.redis.client;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author dtype.huang
 */
@Getter
@RefreshScope
@Configuration
public class RedissonConfiguration {
    @Value("${redis.key.version:0.1}")
    private String version;
    @Value("${redis.key.prefix:core}")
    private String prefix;
    @Value("${redis.key.default-ttl:86400000}")
    private long defaultTtl;
    @Value("${redis.key.hash:30}")
    private int hashModSize;
    @Value("${redis.nacos-data-id:sharding-engine-redis.yaml}")
    private String redisDataId;
    @Value("${redis.nacos-data-group:DEFAULT_GROUP}")
    private String redisDataGroup;
    @Value("${redis.nacos-data-timeout:10000}")
    private long timeoutMs;
    @Value("${redis.debug:false}")
    private boolean debug;
}
