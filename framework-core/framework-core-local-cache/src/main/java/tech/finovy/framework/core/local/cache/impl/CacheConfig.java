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
public class CacheConfig {
    @Value("${spring.application.name:loaclCache}")
    private String applicationName;
    @Value("${cache.local-cache.alias:localCacheConfigured}")
    private String localTokenCacheAlias;
    @Value("${cache.local-cache.heap:100}")
    private int localCacheHeap = 50;
    @Value("${cache.local-cache.offheap:50}")
    private int localCacheOffHeap = 25;
    @Value("${cache.local-cache.disk:250}")
    private int localCacheDisk = 250;
    @Value("${cache.local-cache.ttl:20}")
    private int localCacheTtl = 200;
}
