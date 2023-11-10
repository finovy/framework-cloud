package tech.finovy.framework.cache.local.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


@Data
public class LocalCacheKey implements Serializable {
    private static final long serialVersionUID = -1946325934868173708L;
    private final String key;
    private String cacheType;

    public LocalCacheKey(String key) {
        this.key = key;
    }

    public <T> LocalCacheKey(Class<T> cacheType, String key) {
        this.cacheType = cacheType.getTypeName();
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocalCacheKey)) {
            return false;
        }
        LocalCacheKey objectCacheKey = (LocalCacheKey) o;
        String objectKey = objectCacheKey.getKey();
        String objectCacheType = objectCacheKey.getCacheType();
        if (!StringUtils.equals(key, objectKey)) {
            return false;
        }
        if (!StringUtils.equals(cacheType, objectCacheType)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (key + cacheType).hashCode();
    }
}
