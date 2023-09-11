package tech.finovy.framework.core.local.cache.api;

import tech.finovy.framework.core.local.cache.api.LocalCacheService;
import tech.finovy.framework.core.local.cache.entity.LocalCacheKey;
import tech.finovy.framework.core.local.cache.entity.LocalCachePack;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ComponentScan(basePackages = {"tech.finovy.*"})
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestLocalCacheService.class)
class TestLocalCacheService {
    private final static String txt = "this is a test message!!";
    @Autowired
    private LocalCacheService localCacheService;

    @Test
    @DisplayName("TestLocalCacheExpirationService")
    void localCacheExpirationServiceTest() {
        String cacheKey = "testCache";
        LocalCachePack<String> cachePack = new LocalCachePack(cacheKey);
        cachePack.setData(txt);
        localCacheService.putCache(cachePack);
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
    }
}
