package tech.finovy.framework.distributed.cache.api;


import tech.finovy.framework.redis.entity.cache.entity.*;

import java.io.Serializable;
import java.util.List;

public interface CacheService {
    /**
     * 获取缓存
     *
     * @param dataType
     * @param key
     * @param <T>
     * @return
     */
    <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key);
    <T extends Serializable> CacheBatchPack getCache(Class<T> dataType, List<String> key);
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
    <T extends Serializable> CacheBatchPack getCache(Class<T> dataType, List<String> key, boolean skipPrefix);
    /**
     * 获取缓存
     *
     * @param dataType
     * @param pattern 同 redis 的 keys 命令，支持 * ? [] 三种通配符
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
    <T extends Serializable> BatchSimpleResult putCache(CacheBatchPack<T> cachePack);
    /**
     * 获取序列化缓存
     *
     * @param cacheKey 缓存数据包信息
     * @return SerialCachePack 缓存数据
     */
    SerialCache getSerialCache(CacheKey cacheKey);
    List<SerialCache> getSerialCache(CacheBatchKey cacheKey);
    /**
     * 缓存序列化数据
     *
     * @param serialCache 缓存数据包信息
     * @return SerialCachePack 缓存数据
     */
    SerialCache putSerialCache(SerialCache serialCache);
    BatchSimpleResult putSerialCache(List<SerialCache> batchCache);

    /**
     * 删除缓存
     *
     * @param cacheKey
     * @return
     */
    boolean deleteCache(CacheKey cacheKey);
    BatchSimpleResult deleteCache(CacheBatchKey cacheKey);


    /**
     * 删除缓存
     *
     * @param pattern 同redis的keys，支持 * ? [] 三种通配符
     * @return
     */
    long deleteCachesByPattern(String pattern);

    /**
     * 设置超时
     *
     * @param cacheKey
     * @return
     */
    boolean setExpire(CacheKey cacheKey);
}
