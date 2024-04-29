package tech.finovy.framework.cache.local;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import tech.finovy.framework.cache.local.api.LocalCacheService;
import tech.finovy.framework.cache.local.entity.LocalCachePack;
import tech.finovy.framework.cache.local.impl.CacheProperties;
import tech.finovy.framework.cache.local.impl.LocalCacheServiceImpl;

import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
public class LocalCacheAutoConfiguration {

    @Bean
    public LocalCacheService localCacheService(Cache<String, LocalCachePack> localCacheCaffeine){
        return new LocalCacheServiceImpl(localCacheCaffeine);
    }

    @Bean
    @RefreshScope
    public CacheProperties cacheProperties(Environment environment) {
        return Binder.get(environment)
                .bind(CacheProperties.PREFIX, CacheProperties.class)
                .orElse(new CacheProperties());
    }

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
    public Caffeine getConfig(CacheProperties properties) {
        Caffeine c = Caffeine.newBuilder().initialCapacity(properties.getInitialCapacity());
        if (properties.getExpireAfterWrite() > 0) {
            c.expireAfterWrite(properties.getExpireAfterAccess(), TimeUnit.SECONDS);
        }
        if (properties.getExpireAfterAccess() > 0) {
            c.expireAfterAccess(properties.getExpireAfterAccess(), TimeUnit.SECONDS);
        }
        if (properties.getMaximumSize() > 0 && properties.getMaximumWeight() <= 0) {
            c.maximumSize(properties.getMaximumSize());
        }
        if (properties.getMaximumSize() <= 0 && properties.getMaximumWeight() > 0) {
            c.maximumWeight(properties.getMaximumWeight());
            c.weigher((key, value) -> properties.getWeigher());
        }
        if (properties.isRecordStats()) {
            c.recordStats();
        }
        if (properties.isWeakKeys()) {
            c.weakKeys();
        }
        if (properties.isWeakValues() && !properties.isSoftValues()) {
            c.weakValues();
        }
        if (properties.isSoftValues() && !properties.isWeakValues()) {
            c.softValues();
        }
        return c;
    }

}
