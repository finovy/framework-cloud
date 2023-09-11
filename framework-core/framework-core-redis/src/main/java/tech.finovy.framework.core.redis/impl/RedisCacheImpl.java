package tech.finovy.framework.core.redis.impl;

import tech.finovy.framework.core.redis.ShardingEngineRedisConext;
import tech.finovy.framework.core.redis.api.CacheApi;
import tech.finovy.framework.core.redis.client.RedissonConfiguration;
import tech.finovy.framework.core.redis.entity.cache.CacheKey;
import tech.finovy.framework.core.redis.entity.cache.CachePack;
import tech.finovy.framework.core.redis.holder.ShardingEngineRedisConextHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Dtype.huang
 */
@Slf4j
@Service
public class RedisCacheImpl implements CacheApi {
    private final ShardingEngineRedisConext conext = ShardingEngineRedisConextHolder.get();
    @Autowired
    private RedissonConfiguration redissonConfiguration;

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key) {
        return getCache(dataType, key, false);
    }

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key, boolean skipPrefix) {
        CachePack<T> pack = new CachePack<>(key, skipPrefix);
        CacheKey cacheKey = new CacheKey(key, skipPrefix);
        if (dataType == null) {
            log.warn("TSerial IS NULL,key={},skipPrefix:{}", key, skipPrefix);
            pack.setErrMsg("TSerial IS NULL");
            return pack;
        }
        cacheKey.setCacheType(dataType.getTypeName());
        RBucket<String> cache = conext.getClient().getBucket(key);
        if (cache.isExists() && cacheKey.isRefreshTimeToLive()) {
            cache.expire(cacheKey.getTimeToLive(), TimeUnit.MILLISECONDS);
        }
        pack.setExists(cache.isExists());
        if (conext.getClient().isDebug()) {
            log.info("Cache key:{},Exists:{}", key, pack.isExists());
        }
        if (cache.isExists()) {
            pack.setExists(true);
            pack.setTimeToLive(cache.remainTimeToLive());
            return pack;
        }
        pack.setExists(false);
        return pack;
    }


    @Override
    public <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern, boolean skipPrefix) {
        Iterable<String> keys = conext.getClient().getKeys().getKeysByPattern(pattern);
        List<CachePack<T>> packs = new ArrayList<>();
        keys.forEach(key -> packs.add(getCache(dataType, key, skipPrefix)));
        return packs;
    }

    @Override
    public <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern) {
        return getCachesByPattern(dataType, pattern, false);
    }

    @Override
    public <T extends Serializable> CachePack<T> putCache(CachePack<T> cachePack) {
        if (cachePack.getData() == null) {
            cachePack.setErrMsg("Input Data IS NULL");
            log.warn("KEY={},Input Data IS NULL", cachePack.getKey());
            return cachePack;
        }
        RBucket<T> cache = conext.getClient().getBucket(cachePack.getKey());
        if (cachePack.getTimeToLive() > 0) {
            cache.set(cachePack.getData(), cachePack.getTimeToLive(), TimeUnit.MILLISECONDS);
        } else {
            cache.set(cachePack.getData());
        }
        if (cachePack.getExpireAt() > 0) {
            cache.expireAt(cachePack.getExpireAt());
        }
        cachePack.setExists(true);
        cachePack.setTimeToLive(cache.remainTimeToLive());
        return cachePack;
    }

    @Override
    public boolean deleteCache(CacheKey cacheKey) {
        String key = createKey(cacheKey);
        if (conext.getClient().isDebug()) {
            log.info("Delete SerialCache key:{}", key);
        }
        RBucket<String> cache = conext.getClient().getBucket(key, StringCodec.INSTANCE);
        return cache.delete();
    }

    @Override
    public long deleteCachesByPattern(String pattern) {
        return deleteCachesByPattern(pattern, false);
    }

    @Override
    public long deleteCachesByPattern(String pattern, boolean skipPrefix) {
        Iterable<String> keys = conext.getClient().getKeys().getKeysByPattern(pattern);
        int i = 0;
        for (String key : keys) {
            CacheKey cacheKey = new CacheKey(key, skipPrefix);
            boolean delete = deleteCache(cacheKey);
            if (delete) {
                i++;
            }
        }
        return i;
    }

    @Override
    public boolean setExpire(CacheKey cacheKey) {
        String key = createKey(cacheKey);
        if (conext.getClient().isDebug()) {
            log.info("Set Expire key:{},ttl:{}", key, cacheKey.getTimeToLive());
        }
        RBucket<String> cache = conext.getClient().getBucket(key, StringCodec.INSTANCE);
        return cache.expire(cacheKey.getTimeToLive(), TimeUnit.MILLISECONDS);
    }

    private String createKey(CacheKey cachePack) {
        return conext.getClient().createKey(cachePack.getKey(), cachePack.getCacheType(), cachePack.isSkipPrefix());
    }
}
