package tech.finovy.framework.core.redis.api;

import tech.finovy.framework.core.redis.entity.cache.CacheKey;
import tech.finovy.framework.core.redis.entity.cache.CachePack;

import java.io.Serializable;
import java.util.List;

/**
 * @author Dtype.huang
 */
public interface CacheApi {
    /**
     * 获取缓存
     *
     * @param dataType
     * @param key
     * @param <T>
     * @return
     */
    <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key);

    /**
     * 获取缓存
     *
     * @param dataType
     * @param key
     * @param skipPrefix
     * @param <T>
     * @return
     */
    <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key, boolean skipPrefix);

    /**
     * 获取缓存
     *
     * @param dataType
     * @param pattern    同 redis 的 keys 命令，支持 * ? [] 三种通配符
     * @param skipPrefix
     * @param <T>
     * @return
     */
    <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern, boolean skipPrefix);

    <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern);

    /**
     * 缓存数据
     *
     * @param cachePack 需要出入缓存的数据包
     * @param <T>       数据类型
     * @return 缓存数据包
     */
    <T extends Serializable> CachePack<T> putCache(CachePack<T> cachePack);

    /**
     * 删除缓存
     *
     * @param cacheKey
     * @return
     */
    boolean deleteCache(CacheKey cacheKey);

    /**
     * 删除缓存
     *
     * @param pattern 同redis的keys，支持 * ? [] 三种通配符
     * @return
     */
    long deleteCachesByPattern(String pattern);

    long deleteCachesByPattern(String pattern, boolean skipPrefix);

    /**
     * 设置超时
     *
     * @param cacheKey
     * @return
     */
    boolean setExpire(CacheKey cacheKey);
}
