package tech.finovy.framework.redisson.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.distributed.cache.api.CacheService;
import tech.finovy.framework.redis.entity.cache.entity.*;
import tech.finovy.framework.redisson.api.CacheApi;

import java.io.Serializable;
import java.util.List;

public class RedisCacheServiceImpl implements CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

    private final CacheApi cacheApi;

    public RedisCacheServiceImpl(CacheApi cacheApi) {
        this.cacheApi = cacheApi;
    }

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key) {
        return cacheApi.getCache(dataType, key, false);
    }

    @Override
    public <T extends Serializable> CacheBatchPack getCache(Class<T> dataType, List<String> key) {
        return cacheApi.getCache(dataType, key);
    }

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key, boolean skipPrefix) {
        return cacheApi.getCache(dataType, key, skipPrefix);
    }

    @Override
    public <T extends Serializable> CacheBatchPack getCache(Class<T> dataType, List<String> key, boolean skipPrefix) {
        return cacheApi.getCache(dataType, key, skipPrefix);
    }

    @Override
    public <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern, boolean skipPrefix) {
        return cacheApi.getCachesByPattern(dataType, pattern, skipPrefix);
    }

    @Override
    public <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern) {
        return cacheApi.getCachesByPattern(dataType, pattern);
    }

    @Override
    public <T extends Serializable> CachePack<T> putCache(CachePack<T> cachePack) {
        return cacheApi.putCache(cachePack);
    }

    @Override
    public <T extends Serializable> BatchSimpleResult putCache(CacheBatchPack<T> cachePack) {
        return null;
    }

    @Override
    public SerialCache getSerialCache(CacheKey cacheKey) {
        return cacheApi.getSerialCache(cacheKey);
    }

    @Override
    public List<SerialCache> getSerialCache(CacheBatchKey cacheKey) {
        return null;
    }

    @Override
    public SerialCache putSerialCache(SerialCache serialCache) {
        return cacheApi.putSerialCache(serialCache);
    }

    @Override
    public BatchSimpleResult putSerialCache(List<SerialCache> batchCache) {
        return cacheApi.putSerialCache(batchCache);
    }

    @Override
    public boolean deleteCache(CacheKey cacheKey) {
        return cacheApi.deleteCache(cacheKey);
    }

    @Override
    public BatchSimpleResult deleteCache(CacheBatchKey cacheKey) {
        return cacheApi.deleteCache(cacheKey);
    }

    @Override
    public long deleteCachesByPattern(String pattern) {
        return cacheApi.deleteCachesByPattern(pattern);
    }

    @Override
    public boolean setExpire(CacheKey cacheKey) {
        return cacheApi.setExpire(cacheKey);
    }
}
