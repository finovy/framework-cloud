package tech.finovy.framework.core.redis.entity.cache;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class CacheBatchKey extends CacheKey {
    private List<String> batchKey;

    public CacheBatchKey(List<String> key) {
        super("");
        this.batchKey = key;
    }

    public CacheBatchKey(List<String> key, boolean skipPrefix) {
        super("", skipPrefix);
        this.batchKey = key;
    }

    public <T> CacheBatchKey(List<String> key, Class<T> cacheType) {
        super("", cacheType);
        this.batchKey = key;
    }

    public <T> CacheBatchKey(List<String> key, boolean skipPrefix, Class<T> cacheType) {
        super("", skipPrefix, cacheType);
        this.batchKey = key;
    }
}
