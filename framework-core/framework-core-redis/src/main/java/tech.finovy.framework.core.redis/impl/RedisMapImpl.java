package tech.finovy.framework.core.redis.impl;

import tech.finovy.framework.core.redis.ShardingEngineRedisConext;
import tech.finovy.framework.core.redis.api.MapApi;
import tech.finovy.framework.core.redis.client.RedissonConfiguration;
import tech.finovy.framework.core.redis.holder.ShardingEngineRedisConextHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
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
public class RedisMapImpl implements MapApi {
    private final ShardingEngineRedisConext conext = ShardingEngineRedisConextHolder.get();
    private final ConcurrentMap<String, RMap> cachedMap = new ConcurrentHashMap<>();
    @Autowired
    private RedissonConfiguration redissonConfiguration;

    @Override
    public <T extends Serializable> T put(String mapKey, String key, T value) {
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.put(key, value);
    }

    @Override
    public <T extends Serializable> boolean fastPut(String mapKey, String key, T value) {
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.fastPut(key, value);
    }

    @Override
    public <T extends Serializable> T get(String mapKey, String key) {
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.get(key);
    }

    @Override
    public <T extends Serializable> T get(String mapKey, String key, boolean skipPrefix) {
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, skipPrefix)));
        return map.get(key);
    }

    @Override
    public <T extends Serializable> T computeIfAbsent(String mapKey, String key, Function<? super String, ? extends T> mappingFunction) {
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public boolean containsKey(String mapKey, String key) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.containsKey(key);
    }

    @Override
    public boolean isEmpty(String mapKey) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.isEmpty();
    }

    @Override
    public int size(String mapKey) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.size();
    }

    @Override
    public <T extends Serializable> T remove(String mapKey, String key) {
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.remove(key);
    }

    @Override
    public long fastRemove(String mapKey, String key) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.fastRemove(key);
    }

    @Override
    public <T extends Serializable> void putAll(String mapKey, Map<String, ? extends T> m) {
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        map.putAll(m);
    }

    @Override
    public void clear(String mapKey) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        map.clear();
    }

    @Override
    public Set<String> keySet(String mapKey) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.keySet();
    }

    @Override
    public Set<String> readAllKeySet(String mapKey) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.readAllKeySet();
    }

    @Override
    public <T extends Serializable> Collection<T> readAllValues(String mapKey) {
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.readAllValues();
    }

    @Override
    public void destroy(String mapKey) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        cachedMap.remove(mapKey);
        map.delete();
    }

    @Override
    public Collection values(String mapKey) {
        RMap map = cachedMap.computeIfAbsent(mapKey, o -> conext.getClient().getMap(createKey(mapKey, false)));
        return map.values();
    }

    private String createKey(String mapKey, boolean skipPrefix) {
        if (skipPrefix) {
            return mapKey;
        }
        return conext.getClient().createMapKey(mapKey);
    }

}
