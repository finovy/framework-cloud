package tech.finovy.framework.redisson.impl;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.redisson.api.MapApi;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class RedisMapImpl implements MapApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMapImpl.class);

    private final RedisContext context = RedisContextHolder.get();


    private final ConcurrentMap<String, RMap> cachedMap = new ConcurrentHashMap<>();

    @Override
    public <T extends Serializable> T put(String mapKey, String key, T value) {
        final RedissonClient client = context.getClient();
        RMap<String, T> map =cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.put(key,value);
    }

    @Override
    public <T extends Serializable> boolean fastPut(String mapKey, String key, T value) {
        final RedissonClient client = context.getClient();
        RMap<String, T> map =cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.fastPut(key,value);
    }

    @Override
    public <T extends Serializable> T get(String mapKey, String key) {
        final RedissonClient client = context.getClient();
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.get(key);
    }

    @Override
    public <T extends Serializable> T get(String mapKey, String key, boolean skipPrefix) {
        final RedissonClient client = context.getClient();
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,skipPrefix)));
        return map.get(key);
    }

    @Override
    public <T extends Serializable> T computeIfAbsent(String mapKey, String key, Function<? super String, ? extends T> mappingFunction) {
        final RedissonClient client = context.getClient();
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.computeIfAbsent(key,mappingFunction);
    }

    @Override
    public boolean containsKey(String mapKey, String key) {
        final RedissonClient client = context.getClient();
        RMap map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.containsKey(key);
    }

    @Override
    public boolean isEmpty(String mapKey) {
        final RedissonClient client = context.getClient();
        RMap map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.isEmpty();
    }

    @Override
    public int size(String mapKey) {
        final RedissonClient client = context.getClient();
        RMap map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.size();
    }

    @Override
    public <T extends Serializable> T remove(String mapKey,String key) {
        final RedissonClient client = context.getClient();
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.remove(key);
    }

    @Override
    public long fastRemove(String mapKey, String key) {
        final RedissonClient client = context.getClient();
        RMap map =cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.fastRemove(key);
    }

    @Override
    public <T extends Serializable> void putAll(String mapKey, Map<String, ? extends T> m) {
        final RedissonClient client = context.getClient();
        RMap<String, T> map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        map.putAll(m);
    }

    @Override
    public void clear(String mapKey) {
        final RedissonClient client = context.getClient();
        RMap map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        map.clear();
    }

    @Override
    public Set<String> keySet(String mapKey) {
        final RedissonClient client = context.getClient();
        RMap map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.keySet();
    }

    @Override
    public Set<String> readAllKeySet(String mapKey) {
        final RedissonClient client = context.getClient();
        RMap map =cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.readAllKeySet();
    }

    @Override
    public <T extends Serializable> Collection<T> readAllValues(String mapKey) {
        final RedissonClient client = context.getClient();
        RMap<String, T> map =cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.readAllValues();
    }

    @Override
    public void destroy(String mapKey) {
        final RedissonClient client = context.getClient();
        RMap map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        cachedMap.remove(mapKey);
        map.delete();
    }

    @Override
    public Collection values(String mapKey) {
        final RedissonClient client = context.getClient();
        RMap map = cachedMap.computeIfAbsent(mapKey,o-> client.getMap(createKey(mapKey,false)));
        return map.values();
    }
    private String createKey(String mapKey, boolean skipPrefix) {
        if(skipPrefix){
            return mapKey;
        }
        return context.createMapKey(mapKey);
    }

}
