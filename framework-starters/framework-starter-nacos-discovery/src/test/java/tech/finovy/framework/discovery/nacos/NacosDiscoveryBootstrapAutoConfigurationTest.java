package tech.finovy.framework.discovery.nacos;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.cache.local.LocalCacheAutoConfiguration;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RefreshAutoConfiguration.class, ShardingEngineNacosDiscoveryConfigBootstrapConfiguration.class, LocalCacheAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = NacosDiscoveryBootstrapAutoConfigurationTest.class)
public class NacosDiscoveryBootstrapAutoConfigurationTest {

    @Test
    public void testInit(){

    }
}
