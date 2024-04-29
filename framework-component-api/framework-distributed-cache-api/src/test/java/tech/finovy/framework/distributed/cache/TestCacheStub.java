package tech.finovy.framework.distributed.cache;


import tech.finovy.framework.distributed.cache.api.CacheService;
import tech.finovy.framework.distributed.cache.stub.CacheServiceStubImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tech.finovy.framework.redis.entity.cache.entity.CacheKey;
import tech.finovy.framework.redis.entity.cache.entity.CachePack;
import tech.finovy.framework.redis.entity.cache.entity.SerialCache;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestCacheStub {
    @Mock
    private CacheService cacheService;
    private CacheServiceStubImpl cacheServiceStub;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        SerialCache cachePack=new SerialCache("Test");
        cacheServiceStub=new CacheServiceStubImpl(cacheService);
        doReturn(cachePack).when(cacheService).putSerialCache(any(SerialCache.class));
        doReturn(cachePack).when(cacheService).getSerialCache(any(CacheKey.class));
        doReturn(true).when(cacheService).deleteCache(any(CacheKey.class));
    }
    @Test
    @DisplayName("TestDeleteCache")
    void deleteCacheTest(){
        CacheKey cacheKey=new CacheKey("test");
      boolean result= cacheServiceStub.deleteCache(cacheKey);
        Assertions.assertTrue(result);
    }
    @Test
    @DisplayName("TestgetCache")
    void getCacheTest(){
        CachePack<String> cachePack = cacheServiceStub.getCache(String.class,"aa");
        Assertions.assertNull(cachePack.getErrMsg());
        cachePack = cacheServiceStub.getCache(String.class,"aa",true);
        Assertions.assertNull(cachePack.getErrMsg());
    }
    @Test
    @DisplayName("TestPutCache")
    void putCacheTest(){
        CachePack<String> cachePack=new CachePack<>("123");
        cachePack = cacheServiceStub.putCache(cachePack);
        Assertions.assertNull(cachePack.getErrMsg());
        cachePack.setData("123");
        cachePack = cacheServiceStub.putCache(cachePack);
        Assertions.assertNull(cachePack.getErrMsg());
        CachePack<Integer> cac=new CachePack<>("123");
        cac=cacheServiceStub.putCache(cac);
        Assertions.assertNull(cac.getErrMsg());

        CachePack<CacheKey> cacheKeyPack=new CachePack<>("123");
        cacheKeyPack=cacheServiceStub.putCache(cacheKeyPack);
        Assertions.assertNull(cacheKeyPack.getErrMsg());
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
