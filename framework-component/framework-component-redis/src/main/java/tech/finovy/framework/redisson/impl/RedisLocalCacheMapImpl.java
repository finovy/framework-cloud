package tech.finovy.framework.redisson.impl;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.redisson.api.LocalCacheMapApi;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class RedisLocalCacheMapImpl implements LocalCacheMapApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLocalCacheMapImpl.class);

    private final RedisContext context = RedisContextHolder.get();
    private final LocalCachedMapOptions options;

    public RedisLocalCacheMapImpl(LocalCachedMapOptions options) {
        this.options = options;
    }

    private final ConcurrentMap<String, RLocalCachedMap> localCachedMap = new ConcurrentHashMap<>();

    @Override
    public <T extends Serializable> T put(String mapKey, String key, T value) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.put(key, value);
    }

    @Override
    public <T extends Serializable> boolean fastPut(String mapKey, String key, T value) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.fastPut(key, value);
    }

    @Override
    public <T extends Serializable> T get(String mapKey, String key) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.get(key);
    }

    @Override
    public <T extends Serializable> T computeIfAbsent(String mapKey, String key, Function<? super String, ? extends T> mappingFunction) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public boolean containsKey(String mapKey, String key) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.containsKey(key);
    }

    @Override
    public boolean isEmpty(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.isEmpty();
    }

    @Override
    public int size(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.size();
    }

    @Override
    public <T extends Serializable> T remove(String mapKey, String key) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.remove(key);
    }

    @Override
    public long fastRemove(String mapKey, String key) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.fastRemove(key);
    }

    @Override
    public <T extends Serializable> void putAll(String mapKey, Map<String, ? extends T> m) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        map.putAll(m);
    }

    @Override
    public void clear(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        map.clear();
    }

    @Override
    public Set<String> keySet(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.keySet();
    }

    @Override
    public Set<String> readAllKeySet(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options);
        return map.readAllKeySet();
    }

    @Override
    public <T extends Serializable> Set<T> cachedKeySet(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.cachedKeySet();
    }

    @Override
    public <T extends Serializable> Collection<T> cachedValues(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap<String, T> map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.cachedValues();
    }

    @Override
    public void preloadCache(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        map.preloadCache();
    }

    @Override
    public void preloadCache(String mapKey, int count) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        map.preloadCache(count);
    }

    @Override
    public void destroy(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        localCachedMap.remove(mapKey);
        map.destroy();
    }

    @Override
    public Collection values(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.values();
    }

    @Override
    public Collection readAllValues(String mapKey) {
        final RedissonClient client = context.getClient();
        RLocalCachedMap map = localCachedMap.computeIfAbsent(mapKey, o -> client.getLocalCachedMap(context.createLocalCacheMapKey(mapKey), options));
        return map.readAllValues();
    }
}
