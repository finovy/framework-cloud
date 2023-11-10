package tech.finovy.framework.distributed.cache;


import tech.finovy.framework.distributed.cache.mock.CacheServiceMockImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tech.finovy.framework.redis.entity.cache.entity.CacheKey;
import tech.finovy.framework.redis.entity.cache.entity.CachePack;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestCacheMock {

    private CacheServiceMockImpl cacheServiceStub;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        cacheServiceStub=new CacheServiceMockImpl();
    }
    @Test
    @DisplayName("TestDeleteCache")
    void deleteCacheTest(){
        CacheKey cacheKey=new CacheKey("test");
        boolean result= cacheServiceStub.deleteCache(cacheKey);
        Assertions.assertFalse(result);
    }
    @Test
    @DisplayName("TestgetCache")
    void getCacheTest(){
        CachePack<String> cachePack = cacheServiceStub.getCache(String.class,"aa");
        Assertions.assertNotNull(cachePack.getErrMsg());
        Assertions.assertTrue(cachePack.isMock());
        cachePack = cacheServiceStub.getCache(String.class,"aa",true);
        Assertions.assertNotNull(cachePack.getErrMsg());
        Assertions.assertTrue(cachePack.isMock());
    }
    @Test
    @DisplayName("TestPutCache")
    void putCacheTest(){
        CachePack<String> cachePack=new CachePack<>("123");
        cachePack = cacheServiceStub.putCache(cachePack);
        Assertions.assertTrue(cachePack.isMock());
        cachePack.setData("123");
        cachePack = cacheServiceStub.putCache(cachePack);
        Assertions.assertTrue(cachePack.isMock());
        CachePack<Integer> cac=new CachePack<>("123");
        cac=cacheServiceStub.putCache(cac);
        Assertions.assertTrue(cac.isMock());

        CachePack<CacheKey> cacheKeyPack=new CachePack<>("123");
        cacheKeyPack=cacheServiceStub.putCache(cacheKeyPack);
        Assertions.assertTrue(cacheKeyPack.isMock());
    }
    @Test
    @DisplayName("TestExpire")
    void expireTest(){
        CacheKey cacheKey=new CacheKey("test");
        boolean result= cacheServiceStub.setExpire(cacheKey);
        Assertions.assertFalse(result);
        cacheKey.setTimeToLive(1000);
        result= cacheServiceStub.setExpire(cacheKey);
        Assertions.assertFalse(result);
    }
}
