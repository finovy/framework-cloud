package tech.finovy.framework.cache.local.impl;

import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import tech.finovy.framework.cache.local.api.LocalCacheService;
import tech.finovy.framework.cache.local.entity.LocalCacheKey;
import tech.finovy.framework.cache.local.entity.LocalCachePack;

import java.io.Serializable;

public class LocalCacheServiceImpl implements LocalCacheService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    private final String[] rep = new String[]{"tech.finovy.framework.", "tech.finovy.", "java.lang.", "com.alibaba.fastjson.", "com.alibaba.fastjson2.", "java.util.", "::"};
    private final String[] emptyString = new String[]{"", "", "", "", "", "", ""};
    private final Cache<String, LocalCachePack> localCacheCaffeine;

    public LocalCacheServiceImpl(Cache<String, LocalCachePack> localCacheCaffeine) {
        this.localCacheCaffeine = localCacheCaffeine;
    }

    @Override
    public <T extends Serializable> LocalCachePack<T> getCache(Class<T> dataType, String key) {
        return localCacheCaffeine.get(createKey(key, dataType.getTypeName()), this::buildLoader);
    }

    private <T extends Serializable> LocalCachePack<T> buildLoader(String key) {
        return new LocalCachePack<>(key);
    }

    @Override
    public <T extends Serializable> boolean containsKey(Class<T> dataType, String key) {
        LocalCachePack<T> pack = getCache(dataType, key);
        return pack.isExists();
    }


    @Override
    public <T extends Serializable> LocalCachePack<T> putCache(LocalCachePack<T> cachePack) {
        if (cachePack.getData() == null) {
            cachePack.setErrMsg("Input Data IS NULL");
            LOGGER.warn("KEY={},Input Data IS NULL", cachePack.getKey());
            return cachePack;
        }
        localCacheCaffeine.put(createKey(cachePack), cachePack);
        return cachePack;
    }

    @Override
    public boolean deleteCache(LocalCacheKey cacheKey) {
        try {
            localCacheCaffeine.invalidate(createKey(cacheKey));
            return true;
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return false;
    }

    @Override
    public void clearCache() {
        localCacheCaffeine.invalidateAll();
    }

    public String createKey(String key, String type) {
        if (type == null) {
            return key;
        }
        type = StringUtils.replaceEach(type, rep, emptyString);
        return type + "-" + key;
    }

    private String createKey(LocalCacheKey cachePack) {
        return createKey(cachePack.getKey(), cachePack.getCacheType());
    }
}
