package tech.finovy.framework.core.redis.impl;

import tech.finovy.framework.core.redis.ShardingEngineRedisConext;
import tech.finovy.framework.core.redis.api.LocalCacheMapApi;
import tech.finovy.framework.core.redis.client.RedissonConfiguration;
import tech.finovy.framework.core.redis.holder.ShardingEngineRedisConextHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author Dtype.huang
 */
@Slf4j
@Service
public class RedisLocalCacheMapImpl implements LocalCacheMapApi {
    private final ShardingEngineRedisConext conext = ShardingEngineRedisConextHolder.get();
    private final ConcurrentMap<String, RLocalCachedMap> localCachedMap = new ConcurrentHashMap<>();
    @Autowired
    private RedissonConfiguration redissonConfiguration;
    private LocalCachedMapOptions options = ocalCachedMapOption();

    @Override
    public <T extends Serializable> T put(String mapKey, String key, T value) {
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.put(key, value);
    }

    @Override
    public <T extends Serializable> boolean fastPut(String mapKey, String key, T value) {
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.fastPut(key, value);
    }

    @Override
    public <T extends Serializable> T get(String mapKey, String key) {
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.get(key);
    }

    @Override
    public <T extends Serializable> T computeIfAbsent(String mapKey, String key, Function<? super String, ? extends T> mappingFunction) {

        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public boolean containsKey(String mapKey, String key) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.containsKey(key);
    }

    @Override
    public boolean isEmpty(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.isEmpty();
    }

    @Override
    public int size(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.size();
    }

    @Override
    public <T extends Serializable> T remove(String mapKey, String key) {
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.remove(key);
    }

    @Override
    public long fastRemove(String mapKey, String key) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.fastRemove(key);
    }

    @Override
    public <T extends Serializable> void putAll(String mapKey, Map<String, ? extends T> m) {
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        map.putAll(m);
    }

    @Override
    public void clear(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        map.clear();
    }

    @Override
    public Set<String> keySet(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.keySet();
    }

    @Override
    public Set<String> readAllKeySet(String mapKey) {
        RLocalCachedMap map = conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options);
        return map.readAllKeySet();
    }

    @Override
    public <T extends Serializable> Set<T> cachedKeySet(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.cachedKeySet();
    }

    @Override
    public <T extends Serializable> Collection<T> cachedValues(String mapKey) {
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.cachedValues();
    }

    @Override
    public void preloadCache(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        map.preloadCache();
    }

    @Override
    public void preloadCache(String mapKey, int count) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        map.preloadCache(count);
    }

    @Override
    public void destroy(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        localCachedMap.remove(mapKey);
        map.destroy();
    }

    @Override
    public Collection values(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.values();
    }

    @Override
    public Collection readAllValues(String mapKey) {
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getLocalCachedMap(conext.getClient().createLocalCacheMapKey(mapKey), options));
        return map.readAllValues();
    }


    private LocalCachedMapOptions ocalCachedMapOption() {
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
//			.timeToLive(10, TimeUnit.SECONDS)
        // max idle time for each map entry in local cache
//			.maxIdle(10, TimeUnit.SECONDS);
    }
}
