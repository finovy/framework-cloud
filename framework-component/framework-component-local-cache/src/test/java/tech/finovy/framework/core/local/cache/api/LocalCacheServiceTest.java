package tech.finovy.framework.core.local.cache.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.cache.local.api.LocalCacheService;
import tech.finovy.framework.cache.local.entity.LocalCacheKey;
import tech.finovy.framework.cache.local.entity.LocalCachePack;
import tech.finovy.framework.cache.local.impl.LocalCacheServiceImpl;

import java.io.Serializable;

@Slf4j
public class LocalCacheServiceTest {
    private final static String txt = "this is a test message!!";

    private Caffeine caffeine() {
        return Caffeine.newBuilder().initialCapacity(1000);
    }

    @Test
    void testLocalCacheExpirationService() {
        final LocalCacheService localCacheService = new LocalCacheServiceImpl(caffeine().build());
        String cacheKey = "testCache";
        LocalCachePack<String> cachePack = new LocalCachePack<>(cacheKey);
        cachePack.setData(txt);
        localCacheService.putCache(cachePack);

        final LocalCachePack<Serializable> result = localCacheService.putCache(new LocalCachePack<>(cacheKey));
        Assertions.assertNotNull(result.getErrMsg());

        boolean a = localCacheService.containsKey(String.class, cacheKey);
        Assertions.assertTrue(a);
        cachePack = localCacheService.getCache(String.class, cacheKey);
        Assertions.assertTrue(cachePack.isExists());
        Assertions.assertNull(cachePack.getErrMsg());
        Assertions.assertEquals(txt, cachePack.getData());
        LocalCacheKey k = new LocalCacheKey(String.class, cacheKey);
        localCacheService.deleteCache(k);
        a = localCacheService.containsKey(String.class, cacheKey);
        Assertions.assertFalse(a);
        cachePack = localCacheService.getCache(String.class, cacheKey);
        Assertions.assertFalse(cachePack.isExists());

        localCacheService.clearCache();

        Assertions.assertFalse(localCacheService.deleteCache(null));
        Assertions.assertTrue(localCacheService.deleteCache(new LocalCacheKey("test")));
    }
}
