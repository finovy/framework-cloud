package tech.finovy.framework.redisson;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.redis.entity.cache.entity.BatchSimpleResult;
import tech.finovy.framework.redis.entity.cache.entity.CacheBatchKey;
import tech.finovy.framework.redis.entity.cache.entity.SerialCache;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;
import tech.finovy.framework.redisson.listener.RedisConfigDefinitionListener;
import tech.finovy.framework.redisson.api.CacheApi;
import tech.finovy.framework.redisson.autoconfigure.RedissonClientAutoConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RedissonClientAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = RedisClientTest.class)
public class RedisClientTest {

    private final static String txt = "this is a test message!!";


    @Autowired
    private CacheApi cacheApi;
    @Autowired
    private RedisConfigDefinitionListener redisConfigDefinitionListener;

    @Test
    @DisplayName("TestKeyHash")
    void keyHashTest() {
        final RedisContext redisContext = RedisContextHolder.get();
        final RedissonClient client = redisContext.getClient();
        int x = redisContext.calHash("test");
        int x2 = redisContext.calHash("test");
        Assertions.assertEquals(x, x2);
        x = redisContext.calHash("test");
        x2 = redisContext.calHash("test");
        Assertions.assertEquals(x, x2);
        // for cover
        redisConfigDefinitionListener.getOrder();
    }

    @Test
    @DisplayName("TestBatchDelete")
    void batchKeyHashTest() {
        String cacheKey = "testCache-00";
        List<String> keys = new ArrayList<>();
        List<SerialCache> batchCache = new ArrayList<>();
        log.info("---------------begin-------------------");
        for (int x = 0; x < 10; x++) {
            SerialCache cachePack = new SerialCache(cacheKey + x);
            keys.add(cacheKey + x);
            cachePack.setCacheData(txt + "-----------" + x);
            cachePack.setExpireAt(1000);
            if (x == 1) {
                cachePack.setTimeToLive(1000);
            }
            if (x == 2) {
                cachePack.setTimeToLiveRemain(1000);
            }
            batchCache.add(cachePack);
        }
        cacheApi.putSerialCache(batchCache);
        log.info("---------------put success-------------------");
        CacheBatchKey batchKey = new CacheBatchKey(keys, String.class);
        List<SerialCache> bat = cacheApi.getSerialCache(batchKey);
        log.info("---------------get success-------------------");
        BatchSimpleResult re = cacheApi.deleteCache(batchKey);
        log.info("-------------->{}", re.isSuccess());
    }

    @Test
    @DisplayName("TestBloomFilter")
    void bloomFilterTest() {
        final RedissonClient client = RedisContextHolder.get().getClient();
        RBloomFilter<String> bloomFilter = client.getBloomFilter("00");
        bloomFilter.tryInit(55000000L, 0.03);
        bloomFilter.add("1");
        bloomFilter.add("2");
        bloomFilter.add("3");
        boolean b = bloomFilter.contains("31");
        log.info("----------------->{}", b);
    }

//    @Test
//    @DisplayName("TestJsonBucket")
//    void jsonBucketTest() {
//        RJsonBucket<CachePack> bucket=  client.getJsonBucket("di",new JacksonCodec<>(CachePack.class));
//        bucket.set(new CachePack("tesp"));
//        CachePack obj = bucket.get();
//        log.info("---------------->{}",obj.getKey());
//    }

    CountDownLatch countdown = new CountDownLatch(10);

    //
//    @Test
//    @DisplayName("TestLock")
    void lockTest() {
        for (int x = 0; x < 10; x++) {
            Thread thread = new Thread(() -> testRlock());
            thread.start();
        }
        try {
            countdown.await();
            log.info("执行成功");
        } catch (InterruptedException e) {

        }
    }

    private void testRlock() {
        try {
            final RedissonClient client = RedisContextHolder.get().getClient();
            RLock lock = client.getLock("123");
            boolean res = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (res) {
                try {
                    log.info("--------------has lock------------------------------");
                    return;
                } finally {
//                    lock.unlock();
                    countdown.countDown();
                }
            }
            log.info("--------no lock------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void testDown() {
        final RedisContext redisContext = RedisContextHolder.get();
        Assertions.assertThrows(RuntimeException.class, () -> RedisClientFactory.init(null, null,null, 0));
        RedisClientFactory.shutdown(1);
        final RedissonClient client = redisContext.getClient();
        client.shutdown();
    }
}
