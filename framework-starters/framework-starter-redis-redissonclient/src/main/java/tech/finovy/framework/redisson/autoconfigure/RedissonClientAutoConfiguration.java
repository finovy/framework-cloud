package tech.finovy.framework.redisson.autoconfigure;

import org.redisson.api.LocalCachedMapOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tech.finovy.framework.distributed.cache.api.CacheService;
import tech.finovy.framework.distributed.lock.api.DistributedLockService;
import tech.finovy.framework.distributed.map.api.MapService;
import tech.finovy.framework.redisson.api.*;
import tech.finovy.framework.redisson.config.RedissonConfiguration;
import tech.finovy.framework.redisson.listener.RedisConfigDefinitionListener;
import tech.finovy.framework.redisson.impl.*;

@EnableConfigurationProperties(RedissonProperties.class)
@Configuration(proxyBeanMethods = false)
public class RedissonClientAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RedisConfigDefinitionListener redisConfigDefinitionListener(RedissonProperties properties, RedissonConfiguration configuration) {
        return new RedisConfigDefinitionListener(properties, configuration);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RedissonConfiguration redissonConfiguration(RedissonProperties source) {
        final RedissonConfiguration target = new RedissonConfiguration();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributedIdApi distributedIdApi() {
        return new DistributedIdImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributedLockApi distributedLockApi() {
        return new DistributedLockImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheApi cacheApi(RedissonConfiguration configuration) {
        return new RedisCacheImpl(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalCacheMapApi localCacheMapApi(LocalCachedMapOptions options) {
        return new RedisLocalCacheMapImpl(options);
    }

    @Bean
    @ConditionalOnMissingBean
    public MapApi mapApi() {
        return new RedisMapImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheService cacheService(CacheApi cacheApi) {
        return new RedisCacheServiceImpl(cacheApi);
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributedLockService distributedLockService(DistributedLockApi lockApi) {
        return new DistributedLockServiceImpl(lockApi);
    }

    @Bean
    @ConditionalOnMissingBean
    public MapService mapService(MapApi mapApi) {
        return new RedisMapServiceImpl(mapApi);
    }

    @Bean
    public LocalCachedMapOptions ocalCachedMapOption() {
        return LocalCachedMapOptions.defaults()
                // Defines whether to store a cache miss into the local cache.
                // Default value is false.
                .storeCacheMiss(false)
                // Defines store mode of cache data.
                // Follow options are available:
                // LOCALCACHE - store data in local cache only and use Redis only for data update/invalidation.
                // LOCALCACHE_REDIS - store data in both Redis and local cache.
                .storeMode(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS)
                // Defines Cache provider used as local cache store.
                // Follow options are available:
                // REDISSON - uses Redisson own implementation
                // CAFFEINE - uses Caffeine implementation
                .cacheProvider(LocalCachedMapOptions.CacheProvider.CAFFEINE)
                // Defines local cache eviction policy.
                // Follow options are available:
                // LFU - Counts how often an item was requested. Those that are used least often are discarded first.
                // LRU - Discards the least recently used items first
                // SOFT - Uses weak references, entries are removed by GC
                // WEAK - Uses soft references, entries are removed by GC
                // NONE - No eviction
                .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.NONE)
                // If cache size is 0 then local cache is unbounded.
                .cacheSize(10000)
                // Defines strategy for load missed local cache updates after Redis connection failure.
                // Follow reconnection strategies are available:
                // CLEAR - Clear local cache if map instance has been disconnected for a while.
                // LOAD - Store invalidated entry hash in invalidation log for 10 minutes
                //        Cache keys for stored invalidated entry hashes will be removed
                //        if LocalCachedMap instance has been disconnected less than 10 minutes
                //        or whole cache will be cleaned otherwise.
                // NONE - Default. No reconnection handling
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.LOAD)
                // Defines local cache synchronization strategy.
                // Follow sync strategies are available:
                // INVALIDATE - Default. Invalidate cache entry across all LocalCachedMap instances on map entry change
                // UPDATE - Insert/update cache entry across all LocalCachedMap instances on map entry change
                // NONE - No synchronizations on map changes
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE);
        // time to live for each map entry in local cache
        // .timeToLive(10, TimeUnit.SECONDS)
        // max idle time for each map entry in local cache
        // .maxIdle(10, TimeUnit.SECONDS);
    }

}
