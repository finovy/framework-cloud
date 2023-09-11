package tech.finovy.framework.logappender.push.internals;

import tech.finovy.framework.logappender.utils.Args;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveMap<V> implements Map<String, V> {
    private final Map<String, V> wrappedMap;

    public CaseInsensitiveMap() {
        this(new HashMap<>());
    }

    public CaseInsensitiveMap(Map<String, V> wrappedMap) {
        Args.notNull(wrappedMap, "wrappedMap");
        this.wrappedMap = wrappedMap;
    }

    @Override
    public int size() {
        return wrappedMap.size();
    }

    @Override
    public boolean isEmpty() {
        return wrappedMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return wrappedMap.containsKey(key.toString().toLowerCase());
    }

    @Override
    public boolean containsValue(Object value) {
        return wrappedMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return wrappedMap.get(key.toString().toLowerCase());
    }

    @Override
    public V put(String key, V value) {
        return wrappedMap.put(key.toLowerCase(), value);
    }

    @Override
    public V remove(Object key) {
        return wrappedMap.remove(key.toString().toLowerCase());
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        for (Entry<? extends String, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        wrappedMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return wrappedMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return wrappedMap.values();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return wrappedMap.entrySet();
    }

}
