package tech.finovy.framework.core.common.holder;

import tech.finovy.framework.core.common.exception.ShouldNeverHappenException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ObjectHolder {
    /**
     * singleton instance
     */
    INSTANCE;
    private static final int MAP_SIZE = 8;
    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>(MAP_SIZE);

    public Object getObject(String objectKey) {
        return OBJECT_MAP.get(objectKey);
    }

    public <T> T getObject(Class<T> clasz) {
        return clasz.cast(OBJECT_MAP.values().stream().filter(clasz::isInstance).findAny().orElseThrow(() -> new ShouldNeverHappenException("Can't find any object of class " + clasz.getName())));
    }

    /**
     * Sets object.
     *
     * @param objectKey the key
     * @param object    the object
     * @return the previous object with the key, or null
     */
    public Object setObject(String objectKey, Object object) {
        return OBJECT_MAP.put(objectKey, object);
    }
}
