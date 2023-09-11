package tech.finovy.framework.core.redis;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Dtype.huang
 */
@Slf4j
@ComponentScan(basePackages = {"tech.finovy.*"})
@ContextConfiguration
@EnableDiscoveryClient
@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(NacosConfigAutoConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestShardingEngineRedisDefinitionManager.class)
class TestShardingEngineRedisDefinitionManager {


    @Test
    @DisplayName("TestRefreshTaskFlow")
    void refreshTaskFlowTest() {

    }


}
