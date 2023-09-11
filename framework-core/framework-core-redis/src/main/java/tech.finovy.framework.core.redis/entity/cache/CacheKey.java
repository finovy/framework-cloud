package tech.finovy.framework.core.redis.entity.cache;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author: Dtype.Huang
 * @date: 2020/4/26 17:11
 */

@Data
@SuperBuilder
public class CacheKey implements Serializable {
    private static final long serialVersionUID = -4441736199198440099L;
    private final String key;
    private final boolean skipPrefix;
    private String cacheType;
    private long timeToLive = 0;
    private long expireAt = 0;
    private boolean refreshTimeToLive = false;

    public CacheKey(String key) {
        this.skipPrefix = false;
        this.key = key;
    }

    public CacheKey(String key, boolean skipPrefix) {
        this.skipPrefix = skipPrefix;
        this.key = key;
    }

    public <T> CacheKey(String key, Class<T> cacheType) {
        this.cacheType = cacheType.getTypeName();
        this.skipPrefix = false;
        this.key = key;
    }

    public <T> CacheKey(String key, boolean skipPrefix, Class<T> cacheType) {
        this.cacheType = cacheType.getTypeName();
        this.skipPrefix = skipPrefix;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CacheKey)) {
            return false;
        }
        CacheKey objectCacheKey = (CacheKey) o;
        String objectKey = objectCacheKey.getKey();
        String objectCacheType = objectCacheKey.getCacheType();
        if (!StringUtils.equals(key, objectKey)) {
            return false;
        }
        if (!StringUtils.equals(cacheType, objectCacheType)) {
            return false;
        }
        if (skipPrefix != objectCacheKey.isSkipPrefix()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (key + cacheType + skipPrefix).hashCode();
    }
}
