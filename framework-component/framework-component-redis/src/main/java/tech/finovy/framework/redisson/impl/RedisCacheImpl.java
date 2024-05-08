package tech.finovy.framework.redisson.impl;

import com.alibaba.fastjson2.JSON;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.redis.entity.cache.entity.*;
import tech.finovy.framework.redisson.api.CacheApi;
import tech.finovy.framework.redisson.config.RedissonConfiguration;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisCacheImpl implements CacheApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheImpl.class);
    private final RedisContext context = RedisContextHolder.get();
    private final RedissonConfiguration configuration;

    public RedisCacheImpl(RedissonConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key) {
        return getCache(dataType, key, false);
    }

    @Override
    public <T extends Serializable> CacheBatchPack<T> getCache(Class<T> dataType, List<String> key) {
        return getCache(dataType, key, false);
    }

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key, boolean skipPrefix) {
        final RedissonClient client = context.getClient();
        CachePack<T> pack = new CachePack<>(key, skipPrefix);
        CacheKey cacheKey = new CacheKey(key, skipPrefix);
        if (dataType == null) {
            LOGGER.warn("TSerial IS NULL,key={},skipPrefix:{}", key, skipPrefix);
            pack.setErrMsg("TSerial IS NULL");
            return pack;
        }
        cacheKey.setCacheType(dataType.getTypeName());
        SerialCache serialCache = getSerialCache(cacheKey);
        if (context.isDebug()) {
            LOGGER.info("Cache key:{},Exists:{}", serialCache.getKey(), serialCache.isExists());
        }
        if (serialCache.isExists()) {
            pack.setExists(true);
            pack.setTimeToLiveRemain(serialCache.getTimeToLiveRemain());
            serialCache.setTimeToLive(serialCache.getTimeToLive());
            switch (dataType.getTypeName()) {
                case "java.lang.String":
                case "java.lang.Integer":
                    pack.setData((T) serialCache.getCacheData());
                    break;
                default:
                    T da = JSON.parseObject(serialCache.getCacheData(), dataType);
                    pack.setData(da);
                    break;
            }
            return pack;
        }
        pack.setExists(false);
        pack.setErrMsg(serialCache.getErrMsg());
        return pack;
    }

    @Override
    public <T extends Serializable> CacheBatchPack<T> getCache(Class<T> dataType, List<String> key, boolean skipPrefix) {
        CacheBatchPack pack = new CacheBatchPack<>(dataType, null, skipPrefix);
        CacheBatchKey cacheKey = new CacheBatchKey(key, skipPrefix);
        if (dataType == null) {
            LOGGER.warn("TSerial IS NULL,key={},skipPrefix:{}", key, skipPrefix);
            pack.setErrMsg("TSerial IS NULL");
            return pack;
        }
        cacheKey.setCacheType(dataType.getTypeName());
        List<SerialCache> serialCache = getSerialCache(cacheKey);
        Map<String, T> data = new HashMap<>(key.size());
        for (SerialCache cache : serialCache) {
            switch (dataType.getTypeName()) {
                case "java.lang.String":
                case "java.lang.Integer":
                    data.put(cache.getKey(), (T) cache.getCacheData());
                    break;
                default:
                    T da = JSON.parseObject(cache.getCacheData(), dataType);
                    data.put(cache.getKey(), da);
                    break;
            }
            pack.setErrMsg(cache.getErrMsg());
        }
        pack.setData(data);
        pack.setExists(!data.isEmpty());
        return pack;
    }

    @Override
    public <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern, boolean skipPrefix) {
        final RedissonClient client = context.getClient();
        Iterable<String> keys = client.getKeys().getKeysByPattern(pattern);
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
            LOGGER.warn("KEY={},Input Data IS NULL", cachePack.getKey());
            return cachePack;
        }
        T cacheData = cachePack.getData();
        SerialCache serialCachePack = new SerialCache(cachePack.getKey(), cachePack.isSkipPrefix());
        serialCachePack.setCacheType(cacheData.getClass().getTypeName());
        serialCachePack.setTimeToLive(cachePack.getTimeToLive());
        serialCachePack.setTimeToLiveRemain(cachePack.getTimeToLiveRemain());
        serialCachePack.setExpireAt(cachePack.getExpireAt());
        boolean needToJson = true;
        if (cacheData instanceof String) {
            serialCachePack.setCacheData((String) cacheData);
            needToJson = false;
        }
        if (cacheData instanceof Integer) {
            serialCachePack.setCacheData(String.valueOf(cacheData));
            needToJson = false;
        }
        if (needToJson) {
            String cacheStr = JSON.toJSONString(cacheData);
            serialCachePack.setCacheData(cacheStr);
        }
        serialCachePack = putSerialCache(serialCachePack);
        if (serialCachePack.getErrMsg() != null) {
            cachePack.setErrMsg(serialCachePack.getErrMsg());
            cachePack.setData(null);
            return cachePack;
        }
        cachePack.setExists(serialCachePack.isExists());
        cachePack.setTimeToLiveRemain(serialCachePack.getTimeToLiveRemain());
        cachePack.setTimeToLive(serialCachePack.getTimeToLive());
        return cachePack;
    }

    @Override
    public <T extends Serializable> BatchSimpleResult putCache(CacheBatchPack<T> cachePack) {
        Map<String, T> dataMap = cachePack.getData();
        List<SerialCache> listSerialCache = new ArrayList<>(dataMap.size());
        for (Map.Entry<String, T> each : dataMap.entrySet()) {
            SerialCache serialCachePack = new SerialCache(each.getKey(), cachePack.isSkipPrefix());
            serialCachePack.setCacheType(cachePack.getCacheType());
            serialCachePack.setTimeToLive(cachePack.getTimeToLive());
            serialCachePack.setTimeToLiveRemain(cachePack.getTimeToLiveRemain());
            serialCachePack.setExpireAt(cachePack.getExpireAt());
            boolean needToJson = true;
            T cacheData = each.getValue();
            if (cacheData instanceof String) {
                serialCachePack.setCacheData((String) cacheData);
                needToJson = false;
            }
            if (cacheData instanceof Integer) {
                serialCachePack.setCacheData(String.valueOf(cacheData));
                needToJson = false;
            }
            if (needToJson) {
                String cacheStr = JSON.toJSONString(cacheData);
                serialCachePack.setCacheData(cacheStr);
            }
            listSerialCache.add(serialCachePack);
        }
        return putSerialCache(listSerialCache);
    }

    @Override
    public SerialCache getSerialCache(CacheKey cacheKey) {
        final RedissonClient client = context.getClient();
        String key = createKey(cacheKey);
        SerialCache serialCache = new SerialCache(cacheKey.getKey(), cacheKey.isSkipPrefix());
        try {
            RBucket<String> cache = client.getBucket(key, StringCodec.INSTANCE);
            if (cacheKey.isRefreshTimeToLive()) {
                cache.expire(cacheKey.getTimeToLive(), TimeUnit.MILLISECONDS);
            }
            serialCache.setExists(cache.isExists());
            if (context.isDebug()) {
                LOGGER.info("Get SerialCache key:[{}],Exists:{},RemainTimeToLive:{}", key, cache.isExists(), cache.remainTimeToLive());
            }
            if (cache.isExists()) {
                serialCache.setCacheData(cache.get());
                serialCache.setTimeToLiveRemain(cache.remainTimeToLive());
                serialCache.setTimeToLive(cache.remainTimeToLive());
                return serialCache;
            }
            serialCache.setExists(false);
        } catch (Exception e) {
            serialCache.setErrMsg(e.toString());
            serialCache.setExists(false);
            LOGGER.error("getSerialCache={},Error:{}", cacheKey.getKey(), e.toString());
        }
        return serialCache;
    }

    @Override
    public List<SerialCache> getSerialCache(CacheBatchKey cacheKey) {
        final RedissonClient client = context.getClient();
        List<SerialCache> batchDeleteResult = new ArrayList<>();
        List<String> keys = cacheKey.getBatchKey();
        BatchOptions options = BatchOptions.defaults()
                .executionMode(BatchOptions.ExecutionMode.IN_MEMORY);
        RBatch batch = client.createBatch(options);
        for (String key : keys) {
            String keyb = context.createKey(key, cacheKey.getCacheType(), cacheKey.isSkipPrefix());
            RBucketAsync<String> bucketAsync = batch.getBucket(keyb, StringCodec.INSTANCE);
            if (cacheKey.isRefreshTimeToLive()) {
                bucketAsync.expireAsync(cacheKey.getTimeToLive(), TimeUnit.MILLISECONDS);
            }
            bucketAsync.isExistsAsync();
            bucketAsync.remainTimeToLiveAsync();
            bucketAsync.getAsync();
        }
        BatchResult res = batch.execute();
        List<?> list = res.getResponses();
        if (list.size() != keys.size() * 3) {
            LOGGER.error("BatchResult size ERROR, Return {} not match {}", list.size(), keys.size() * 3);
            return batchDeleteResult;
        }
        for (int x = 0; x < keys.size(); x++) {
            SerialCache serialCache = new SerialCache(keys.get(x), cacheKey.isSkipPrefix());
            if (Boolean.TRUE.equals(list.get(x * 3))) {
                serialCache.setTimeToLive(Long.parseLong(list.get(x * 3 + 1).toString()));
                serialCache.setCacheData((String) list.get(x * 3 + 2));
            }
            batchDeleteResult.add(serialCache);
        }
        return batchDeleteResult;
    }

    @Override
    public SerialCache putSerialCache(SerialCache serialCache) {
        final RedissonClient client = context.getClient();
        String key = createKey(serialCache);
        try {
            RBucket<String> cache = client.getBucket(key, StringCodec.INSTANCE);
            if (serialCache.getTimeToLive() == 0 && serialCache.getTimeToLiveRemain() == 0) {
                serialCache.setTimeToLive(configuration.getKeyDefaultTtl());
            }
            if (serialCache.getTimeToLive() > 0) {
                cache.set(serialCache.getCacheData(), serialCache.getTimeToLive(), TimeUnit.MILLISECONDS);
            } else if (serialCache.getTimeToLiveRemain() > 0) {
                cache.set(serialCache.getCacheData(), serialCache.getTimeToLiveRemain(), TimeUnit.MILLISECONDS);
            } else {
                cache.set(serialCache.getCacheData());
            }
            if (serialCache.getExpireAt() > 0) {
                cache.expireAt(serialCache.getExpireAt());
            }
            serialCache.setExists(true);
            serialCache.setTimeToLiveRemain(cache.remainTimeToLive());
            serialCache.setTimeToLive(cache.remainTimeToLive());
            if (context.isDebug()) {
                LOGGER.info("Put SerialCache key:[{}],RemainTimeToLive:{}", key, serialCache.getTimeToLive());
            }
        } catch (Exception e) {
            serialCache.setErrMsg(e.toString());
            serialCache.setExists(false);
            LOGGER.error("putSerialCache={},Error:{}", serialCache.toString(), e.toString());
        }
        return serialCache;
    }

    @Override
    public BatchSimpleResult putSerialCache(List<SerialCache> batchCache) {
        final RedissonClient client = context.getClient();
        BatchSimpleResult batchDeleteResult = new BatchSimpleResult();

        BatchOptions options = BatchOptions.defaults()
                .executionMode(BatchOptions.ExecutionMode.IN_MEMORY)
                .skipResult();
        RBatch batch = client.createBatch(options);
        for (SerialCache cache : batchCache) {
            RBucketAsync bucketAsync = batch.getBucket(createKey(cache), StringCodec.INSTANCE);
            if (cache.getTimeToLive() == 0 && cache.getTimeToLiveRemain() == 0) {
                cache.setTimeToLive(configuration.getKeyDefaultTtl());
            }
            if (cache.getTimeToLive() > 0) {
                bucketAsync.setAsync(cache.getCacheData(), cache.getTimeToLive(), TimeUnit.MILLISECONDS);
            } else if (cache.getTimeToLiveRemain() > 0) {
                bucketAsync.setAsync(cache.getCacheData(), cache.getTimeToLiveRemain(), TimeUnit.MILLISECONDS);
            } else {
                bucketAsync.setAsync(cache.getCacheData());
            }
            if (cache.getExpireAt() > 0) {
                bucketAsync.expireAsync(Instant.ofEpochMilli(cache.getExpireAt()));
            }
        }
        BatchResult res = batch.execute();
        List<Boolean> list = res.getResponses();
        List<String> errKeys = new ArrayList<>();
        List<String> successKeys = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            Boolean result = list.get(x);
            if (result != null && result) {
                successKeys.add(batchCache.get(x).getKey());
                continue;
            }
            errKeys.add(batchCache.get(x).getKey());
        }
        batchDeleteResult.setSuccess(true);
        if (!errKeys.isEmpty()) {
            batchDeleteResult.setErrKeys(errKeys);
            batchDeleteResult.setSuccess(false);
        }
        if (!successKeys.isEmpty()) {
            batchDeleteResult.setSuccessKey(successKeys);
        }
        return batchDeleteResult;
    }

    @Override
    public boolean deleteCache(CacheKey cacheKey) {
        final RedissonClient client = context.getClient();
        String key = createKey(cacheKey);
        if (context.isDebug()) {
            LOGGER.info("Delete SerialCache key:{}", key);
        }
        RBucket<String> cache = client.getBucket(key, StringCodec.INSTANCE);
        return cache.delete();
    }

    @Override
    public BatchSimpleResult deleteCache(CacheBatchKey cacheKey) {
        final RedissonClient client = context.getClient();
        BatchSimpleResult batchDeleteResult = new BatchSimpleResult();
        List<String> keys = cacheKey.getBatchKey();

        BatchOptions options = BatchOptions.defaults()
                .executionMode(BatchOptions.ExecutionMode.IN_MEMORY)
                .skipResult();
        RBatch batch = client.createBatch(options);
        for (String key : keys) {
            String keyb = context.createKey(key, cacheKey.getCacheType(), cacheKey.isSkipPrefix());
            batch.getBucket(keyb, StringCodec.INSTANCE).deleteAsync();
        }
        BatchResult res = batch.execute();
        List<Boolean> list = res.getResponses();
        List<String> errKeys = new ArrayList<>();
        List<String> successKeys = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            Boolean result = list.get(x);
            if (result != null && result) {
                successKeys.add(keys.get(x));
                continue;
            }
            errKeys.add(keys.get(x));
        }
        batchDeleteResult.setSuccess(true);
        if (!errKeys.isEmpty()) {
            batchDeleteResult.setErrKeys(errKeys);
            batchDeleteResult.setSuccess(false);
        }
        if (!successKeys.isEmpty()) {
            batchDeleteResult.setSuccessKey(successKeys);
        }
        return batchDeleteResult;
    }

    @Override
    public long deleteCachesByPattern(String pattern) {
        return deleteCachesByPattern(pattern, false);
    }

    @Override
    public long deleteCachesByPattern(String pattern, boolean skipPrefix) {
        final RedissonClient client = context.getClient();
        Iterable<String> keys = client.getKeys().getKeysByPattern(pattern);
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
        final RedissonClient client = context.getClient();
        String key = createKey(cacheKey);
        if (context.isDebug()) {
            LOGGER.info("Set Expire key:{},ttl:{}", key, cacheKey.getTimeToLive());
        }
        RBucket<String> cache = client.getBucket(key, StringCodec.INSTANCE);
        return cache.expire(cacheKey.getTimeToLive(), TimeUnit.MILLISECONDS);
    }

    private String createKey(CacheKey cachePack) {
        final RedissonClient client = context.getClient();
        return context.createKey(cachePack.getKey(), cachePack.getCacheType(), cachePack.isSkipPrefix());
    }
}
