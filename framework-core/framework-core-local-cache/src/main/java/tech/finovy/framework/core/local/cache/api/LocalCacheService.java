package tech.finovy.framework.core.local.cache.api;


import tech.finovy.framework.core.local.cache.entity.LocalCacheKey;
import tech.finovy.framework.core.local.cache.entity.LocalCachePack;

import java.io.Serializable;

/**
 * @author Dtype.huang
 */
public interface LocalCacheService {

    /**
     * 获取缓存
     *
     * @param dataType
     * @param key
     * @param <T>
     * @return
     */

    <T extends Serializable> LocalCachePack<T> getCache(Class<T> dataType, String key);

    <T extends Serializable> boolean containsKey(Class<T> dataType, String key);

    /**
     * 缓存数据
     *
     * @param cachePack 需要出入缓存的数据包
     * @param <T>       数据类型
     * @return 缓存数据包
     */

    <T extends Serializable> LocalCachePack<T> putCache(LocalCachePack<T> cachePack);

    /**
     * 删除缓存
     *
     * @param cacheKey
     * @return
     */

    boolean deleteCache(LocalCacheKey cacheKey);

    void clearCache();
}
