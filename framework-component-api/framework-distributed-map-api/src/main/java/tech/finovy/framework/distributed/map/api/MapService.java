package tech.finovy.framework.distributed.map.api;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface MapService {
    <T extends Serializable> T put(String mapKey, String key,T value);
    <T extends Serializable> T get(String mapKey, String key);
    boolean containsKey(String mapKey, String key);
    boolean isEmpty(String mapKey);
    int size(String mapKey);
    <T extends Serializable> T remove(String mapKey,String key);
    <T extends Serializable> void putAll(String mapKey,Map<String, ? extends T> m);
    void clear(String mapKey);
    Set<String> keySet(String mapKey);
}
