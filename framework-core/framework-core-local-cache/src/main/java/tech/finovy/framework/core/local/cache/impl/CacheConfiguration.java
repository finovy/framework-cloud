package tech.finovy.framework.core.local.cache.impl;

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
public class CacheConfiguration {
    @Value("${spring.application.name:loaclCache}")
    private String applicationName;

    @Value("${cache.local-cache.initial-capacity:1000}")
    private int initialCapacity = 1000;
    @Value("${cache.local-cache.maximum-size:5000}")
    private int maximumSize = 5000;
    @Value("${cache.local-cache.maximum-weight:0}")
    private int maximumWeight = 0;
    @Value("${cache.local-cache.expire-after-access:60}")
    private long expireAfterAccess = 200;
    @Value("${cache.local-cache.expire-after-write:60}")
    private long expireAfterWrite = 200;

    @Value("${cache.local-cache.weak-keys:false}")
    private boolean weakKeys;
    @Value("${cache.local-cache.weak-values:true}")
    private boolean weakValues;
    @Value("${cache.local-cache.soft-values:false}")
    private boolean softValues;
    @Value("${cache.local-cache.record-stats:false}")
    private boolean recordStats;
}
