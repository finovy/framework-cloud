package tech.finovy.framework.redisson.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface MapApi {
    <T extends Serializable> T put(String mapKey, String key,T value);
    <T extends Serializable> boolean fastPut(String mapKey, String key,T value);
    <T extends Serializable> T get(String mapKey, String key);
    <T extends Serializable> T get(String mapKey, String key, boolean skipPrefix);
    <T extends Serializable> T computeIfAbsent(String mapKey, String key, Function<? super String, ? extends T> mappingFunction);
    boolean containsKey(String mapKey, String key);
    boolean isEmpty(String mapKey);
    int size(String mapKey);
    <T extends Serializable> T remove(String mapKey,String key);
    long fastRemove(String mapKey,String key);
    <T extends Serializable> void putAll(String mapKey,Map<String, ? extends T> m);
    void clear(String mapKey);
    Set<String> keySet(String mapKey);
    Set<String> readAllKeySet(String mapKey);
    <T extends Serializable> Collection<T> values(String mapKey);
    <T extends Serializable> Collection<T> readAllValues(String mapKey);
    void destroy(String mapKey);
}
