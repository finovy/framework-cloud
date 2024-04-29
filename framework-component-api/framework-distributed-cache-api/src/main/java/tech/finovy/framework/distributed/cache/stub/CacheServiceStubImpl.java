package tech.finovy.framework.distributed.cache.stub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tech.finovy.framework.distributed.cache.api.CacheService;
import lombok.extern.slf4j.Slf4j;
import tech.finovy.framework.redis.entity.cache.entity.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CacheServiceStubImpl implements CacheService {
    private final CacheService cacheService;

    public CacheServiceStubImpl(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key) {
        return getCache(dataType, key, false);
    }

    @Override
    public <T extends Serializable> CacheBatchPack getCache(Class<T> dataType, List<String> key) {
        return getCache(dataType, key,false);
    }

    @Override
    public <T extends Serializable> CachePack<T> getCache(Class<T> dataType, String key, boolean skipPrefix) {
        CachePack<T> pack = new CachePack<>(key, skipPrefix);
        CacheKey cacheKey = new CacheKey(key, skipPrefix);
        cacheKey.setCacheType(dataType.getTypeName());
        SerialCache serialCache;
        try {
            if (log.isDebugEnabled()) {
                log.debug("SkipPrefix:{},TypeName:{},key:{}", skipPrefix, dataType.getTypeName(), key);
            }
            serialCache = cacheService.getSerialCache(cacheKey);
        } catch (Exception e) {
            log.error(e.toString());
            pack.setErrMsg(e.toString());
            return pack;
        }
        pack.setExists(serialCache.isExists());
        pack.setMock(serialCache.isMock());
        if (serialCache.isExists()) {
            pack.setExists(true);
            pack.setTimeToLiveRemain(serialCache.getTimeToLiveRemain());
            pack.setTimeToLive(serialCache.getTimeToLive());
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

        pack.setErrMsg(serialCache.getErrMsg());
        return pack;
    }

    @Override
    public <T extends Serializable> CacheBatchPack getCache(Class<T> dataType, List<String> key, boolean skipPrefix) {
        CacheBatchPack pack = new CacheBatchPack<>(dataType,null, skipPrefix);
        CacheBatchKey cacheKey = new CacheBatchKey(key, skipPrefix);
        if (dataType == null) {
            log.warn("TSerial IS NULL,key={},skipPrefix:{}", key, skipPrefix);
            pack.setErrMsg("TSerial IS NULL");
            return pack;
        }
        cacheKey.setCacheType(dataType.getTypeName());
        List<SerialCache> serialCache = cacheService.getSerialCache(cacheKey);
        Map<String,T> data=new HashMap<>(key.size());
        for(SerialCache cache:serialCache){
            switch (dataType.getTypeName()) {
                case "java.lang.String":
                case "java.lang.Integer":
                    data.put(cache.getKey(),(T) cache.getCacheData());
                    break;
                default:
                    T da = JSON.parseObject(cache.getCacheData(), dataType);
                    data.put(cache.getKey(),da);
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
        return cacheService.getCachesByPattern(dataType, pattern, skipPrefix);
    }

    @Override
    public <T extends Serializable> List<CachePack<T>> getCachesByPattern(Class<T> dataType, String pattern) {
        return cacheService.getCachesByPattern(dataType, pattern);
    }

    @Override
    public <T extends Serializable> CachePack<T> putCache(CachePack<T> cachePack) {
        SerialCache serialCachePack = new SerialCache(cachePack.getKey(), cachePack.isSkipPrefix());
        T cacheData = cachePack.getData();
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
            String cacheStr = JSON.toJSONString(cacheData, SerializerFeature.WriteSlashAsSpecial);
            serialCachePack.setCacheData(cacheStr);
        }
        serialCachePack.setTimeToLiveRemain(cachePack.getTimeToLiveRemain());
        serialCachePack.setTimeToLive(cachePack.getTimeToLive());
        serialCachePack = cacheService.putSerialCache(serialCachePack);
        cachePack.setMock(serialCachePack.isMock());
        cachePack.setExists(serialCachePack.isExists());
        cachePack.setRefreshTimeToLive(serialCachePack.isRefreshTimeToLive());
        if (serialCachePack.getErrMsg() != null) {
            cachePack.setErrMsg(serialCachePack.getErrMsg());
            cachePack.setData(null);
            return cachePack;
        }
        return cachePack;
    }

    @Override
    public <T extends Serializable> BatchSimpleResult putCache(CacheBatchPack<T> cachePack) {
        Map<String,T> dataMap=cachePack.getData();
        List<SerialCache> listSerialCache=new ArrayList<>(dataMap.size());
        for(Map.Entry<String,T> each:dataMap.entrySet()) {
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
                String cacheStr = JSON.toJSONString(cacheData, SerializerFeature.WriteSlashAsSpecial);
                serialCachePack.setCacheData(cacheStr);
            }
            listSerialCache.add(serialCachePack);
        }
        return cacheService.putSerialCache(listSerialCache);
    }

    @Override
    public SerialCache getSerialCache(CacheKey cacheKey) {
        try {
            return cacheService.getSerialCache(cacheKey);
        } catch (Exception e) {
            log.error(e.toString());
        }
        SerialCache p = new SerialCache(cacheKey.getKey(), cacheKey.isSkipPrefix());
        p.setErrMsg("RemotingException");
        return p;
    }

    @Override
    public List<SerialCache> getSerialCache(CacheBatchKey cacheKey) {
        try {
            return cacheService.getSerialCache(cacheKey);
        } catch (Exception e) {
            log.error(e.toString());
        }
        SerialCache p = new SerialCache(cacheKey.getKey(), cacheKey.isSkipPrefix());
        p.setErrMsg("RemotingException");
        return new ArrayList<>();
    }

    @Override
    public SerialCache putSerialCache(SerialCache serialCache) {
        try {
            return cacheService.putSerialCache(serialCache);
        } catch (Exception e) {
            log.error(e.toString());
        }
        SerialCache p = new SerialCache(serialCache.getKey(), serialCache.isSkipPrefix());
        p.setErrMsg("RemotingException");
        return p;
    }

    @Override
    public BatchSimpleResult putSerialCache(List<SerialCache> batchCache) {
        return null;
    }

    @Override
    public boolean deleteCache(CacheKey cacheKey) {
        return cacheService.deleteCache(cacheKey);
    }

    @Override
    public BatchSimpleResult deleteCache(CacheBatchKey cacheKey) {
        return cacheService.deleteCache(cacheKey);
    }

    @Override
    public long deleteCachesByPattern(String pattern) {
        return cacheService.deleteCachesByPattern(pattern);
    }

    @Override
    public boolean setExpire(CacheKey cacheKey) {
        return cacheService.setExpire(cacheKey);
    }
}
