package tech.finovy.framework.distributed.cache.mock;

import com.alibaba.fastjson2.JSON;
import tech.finovy.framework.distributed.cache.api.CacheService;
import lombok.extern.slf4j.Slf4j;
import tech.finovy.framework.redis.entity.cache.entity.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class CacheServiceMockImpl implements CacheService {
    private static final String NOPROVIDER="No provider available";
    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key) {
        CachePack<T> cachePack=new CachePack<>(key);
        cachePack.setErrMsg(NOPROVIDER);
        cachePack.setExists(false);
        cachePack.setMock(true);
        return cachePack;
    }

    @Override
    public <T extends Serializable> CacheBatchPack getCache(Class<T> dataType, List<String> key) {
        return  getCache(dataType,key,false);
    }

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key, boolean skipPrefix) {
        CachePack<T> cachePack=new CachePack<>(key);
        cachePack.setErrMsg(NOPROVIDER);
        cachePack.setExists(false);
        cachePack.setMock(true);
        return cachePack;
    }

    @Override
    public <T extends Serializable> CacheBatchPack getCache(Class<T> dataType, List<String> key, boolean skipPrefix) {
        CacheBatchPack<T> pack=new CacheBatchPack<T>(dataType, new HashMap<>(),skipPrefix);
        pack.setErrMsg(NOPROVIDER);
        pack.setExists(false);
        pack.setMock(true);
        return pack;
    }

    @Override
    public <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern, boolean skipPrefix) {
        return Collections.emptyList();
    }

    @Override
    public <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern) {
        return getCachesByPattern(dataType,pattern);
    }

    @Override
    public <T extends Serializable> CachePack<T> putCache(CachePack<T> cachePack) {
        cachePack.setErrMsg(NOPROVIDER);
        cachePack.setExists(false);
        cachePack.setMock(true);
        return cachePack;
    }

    @Override
    public <T extends Serializable> BatchSimpleResult putCache(CacheBatchPack<T> cachePack) {
        BatchSimpleResult batchDeleteResult=new BatchSimpleResult();
        log.error("putSerialCache error,List<SerialCache>:{}", JSON.toJSONString(cachePack));
        return batchDeleteResult;
    }

    @Override
    public SerialCache getSerialCache(CacheKey cacheKey) {
        SerialCache cachePack=new SerialCache(cacheKey.getKey());
        cachePack.setErrMsg(NOPROVIDER);
        cachePack.setExists(false);
        cachePack.setMock(true);
        return cachePack;
    }

    @Override
    public List<SerialCache> getSerialCache(CacheBatchKey cacheKey) {
        log.error("getSerialCache error,CacheBatchKey:{}", JSON.toJSONString(cacheKey));
        return new ArrayList<>();
    }

    @Override
    public SerialCache putSerialCache(SerialCache serialCache) {
        serialCache.setErrMsg(NOPROVIDER);
        serialCache.setExists(false);
        serialCache.setMock(true);
        return serialCache;
    }

    @Override
    public BatchSimpleResult putSerialCache(List<SerialCache> batchCache) {
        BatchSimpleResult batchDeleteResult=new BatchSimpleResult();
        log.error("putSerialCache error,List<SerialCache>:{}", JSON.toJSONString(batchCache));
        return batchDeleteResult;
    }

    @Override
    public boolean deleteCache(CacheKey cacheKey) {
        return false;
    }

    @Override
    public BatchSimpleResult deleteCache(CacheBatchKey cacheKey) {
        return new BatchSimpleResult();
    }

    @Override
    public long deleteCachesByPattern(String pattern) {
        return 0;
    }

    @Override
    public boolean setExpire(CacheKey cacheKey) {
        return false;
    }
}
