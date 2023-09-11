package tech.finovy.framework.core.local.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tech.finovy.framework.core.local.cache.entity.LocalCachePack;

import java.util.concurrent.TimeUnit;

/**
 * @author dtype.huang
 */
@Configuration
public class LocalCacheManager {
    @Bean("localCacheCaffeine")
    @Primary
    public Cache<String, LocalCachePack> localCacheCaffeine(Caffeine caffeine) {
        Cache<String, LocalCachePack> cache = caffeine.build();
        return cache;
    }

    @Primary
    @Bean("caffeineCacheManager")
    public CacheManager CaffeineCacheManager(Caffeine caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

    @Primary
    @Bean("caffeine")
    public Caffeine getConfig(CacheConfiguration configuration) {
        Caffeine c = Caffeine.newBuilder().initialCapacity(configuration.getInitialCapacity());
        if (configuration.getExpireAfterWrite() > 0) {
            c.expireAfterWrite(configuration.getExpireAfterAccess(), TimeUnit.SECONDS);
        }
        if (configuration.getExpireAfterAccess() > 0) {
            c.expireAfterAccess(configuration.getExpireAfterAccess(), TimeUnit.SECONDS);
        }
        if (configuration.getMaximumSize() > 0 && configuration.getMaximumWeight() <= 0) {
            c.maximumSize(configuration.getMaximumSize());
        }
        if (configuration.getMaximumSize() <= 0 && configuration.getMaximumWeight() > 0) {
            c.maximumWeight(configuration.getMaximumWeight());
        }
        if (configuration.isRecordStats()) {
            c.recordStats();
        }
        if (configuration.isWeakKeys()) {
            c.weakKeys();
        }
        if (configuration.isWeakValues() && !configuration.isSoftValues()) {
            c.weakValues();
        }
        if (configuration.isSoftValues() && !configuration.isWeakValues()) {
            c.softValues();
        }
        return c;
    }

}
