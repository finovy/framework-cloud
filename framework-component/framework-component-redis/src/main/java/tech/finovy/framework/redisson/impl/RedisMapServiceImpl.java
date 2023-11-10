package tech.finovy.framework.redisson.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.distributed.map.api.MapService;
import tech.finovy.framework.redisson.api.MapApi;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class RedisMapServiceImpl implements MapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMapServiceImpl.class);

    private final MapApi mapApi;

    public RedisMapServiceImpl(MapApi mapApi) {
        this.mapApi = mapApi;
    }

    @Override
    public <T extends Serializable> T put(String mapKey, String key, T value) {
        return mapApi.put(mapKey,key,value);
    }

    @Override
    public <T extends Serializable> T get(String mapKey, String key) {
        return mapApi.get(mapKey,key);
    }

    @Override
    public boolean containsKey(String mapKey, String key) {
        return mapApi.containsKey(mapKey,key);
    }

    @Override
    public boolean isEmpty(String mapKey) {
        return mapApi.isEmpty(mapKey);
    }

    @Override
    public int size(String mapKey) {
        return mapApi.size(mapKey);
    }

    @Override
    public <T extends Serializable> T remove(String mapKey,String key) {
        return mapApi.remove(mapKey,key);
    }

    @Override
    public <T extends Serializable> void putAll(String mapKey, Map<String, ? extends T> m) {
        mapApi.putAll(mapKey,m);
    }

    @Override
    public void clear(String mapKey) {
        mapApi.clear(mapKey);
    }

    @Override
    public Set<String> keySet(String mapKey) {
        return mapApi.keySet(mapKey);
    }
}
