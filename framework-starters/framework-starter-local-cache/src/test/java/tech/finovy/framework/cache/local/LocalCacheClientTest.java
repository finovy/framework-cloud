package tech.finovy.framework.cache.local;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.cache.local.api.LocalCacheService;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RefreshAutoConfiguration.class,LocalCacheAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = LocalCacheClientTest.class)
public class LocalCacheClientTest {

    @Autowired
    private LocalCacheService localCacheService;

    @Test
    public void testLocalCacheService(){
        Assertions.assertFalse(localCacheService.getCache(String.class, "test-key").isExists());
    }

}
