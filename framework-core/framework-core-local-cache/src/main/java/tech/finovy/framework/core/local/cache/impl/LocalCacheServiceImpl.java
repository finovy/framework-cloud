package tech.finovy.framework.core.local.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import tech.finovy.framework.core.local.cache.api.LocalCacheService;
import tech.finovy.framework.core.local.cache.entity.LocalCacheKey;
import tech.finovy.framework.core.local.cache.entity.LocalCachePack;

import java.io.Serializable;

/**
 * @author Dtype.huang
 */
@Slf4j
@Service
@Configuration
public class LocalCacheServiceImpl implements LocalCacheService {
    private final String[] rep = new String[]{"tech.finovy.framework.", "tech.finovy.", "java.lang.", "com.alibaba.fastjson.", "java.util.", "::"};
    private final String[] emptyString = new String[]{"", "", "", "", "", ""};
    @Resource(name = "localCacheCaffeine")
    private Cache<String, LocalCachePack> localCacheCaffeine;

    private LocalCachePack buildLoader(String key) {
        return new LocalCachePack<>(key);
    }

    @Override
    public <T extends Serializable> LocalCachePack<T> getCache(Class<T> dataType, String key) {
        return localCacheCaffeine.get(createKey(key, dataType.getTypeName()), this::buildLoader);
    }


    @Override
    public <T extends Serializable> boolean containsKey(Class<T> dataType, String key) {
        LocalCachePack pack = getCache(dataType, key);
        return pack.isExists();
    }


    @Override
    public <T extends Serializable> LocalCachePack<T> putCache(LocalCachePack<T> cachePack) {
        if (cachePack.getData() == null) {
            cachePack.setErrMsg("Input Data IS NULL");
            log.warn("KEY={},Input Data IS NULL", cachePack.getKey());
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
            log.error(e.toString());
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
        StringBuilder strBul = new StringBuilder(type);
        strBul.append("-").append(key);
        return strBul.toString();
    }

    private String createKey(LocalCacheKey cachePack) {
        return createKey(cachePack.getKey(), cachePack.getCacheType());
    }
}
