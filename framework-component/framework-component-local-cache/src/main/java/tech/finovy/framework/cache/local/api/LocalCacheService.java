package tech.finovy.framework.cache.local.api;


import tech.finovy.framework.cache.local.entity.LocalCacheKey;
import tech.finovy.framework.cache.local.entity.LocalCachePack;

import java.io.Serializable;

public interface LocalCacheService {

    /**
     * getCache
     *
     * @param dataType type
     * @param key      unique key
     * @return cache pack
     */
    <T extends Serializable> LocalCachePack<T> getCache(Class<T> dataType, String key);

    <T extends Serializable> boolean containsKey(Class<T> dataType, String key);

    /**
     * getCache
     *
     * @param cachePack pack
     * @return cache pack
     */
    <T extends Serializable> LocalCachePack<T> putCache(LocalCachePack<T> cachePack);

    /**
     * delete
     *
     * @param cacheKey key
     * @return status
     */
    boolean deleteCache(LocalCacheKey cacheKey);

    /**
     * clear cache
     */
    void clearCache();
}
