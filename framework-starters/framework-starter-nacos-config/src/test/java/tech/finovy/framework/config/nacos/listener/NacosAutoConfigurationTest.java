package tech.finovy.framework.config.nacos.listener;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigBootstrapConfiguration;
import tech.finovy.framework.config.nacos.context.ShardingEngineNacosContext;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;

@Slf4j
@ComponentScan(basePackages = {"tech.finovy.framework.config.nacos"})
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ImportAutoConfiguration({RefreshAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = NacosAutoConfigurationTest.class)
public class NacosAutoConfigurationTest {

    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.config.group-id}")
    private String dataGroup;
    @Autowired
    private ShardingEngineNacosContext shardingEngineNacosContext;

    @Test
    void nacosConfigPublishTest() throws NacosException {
        // just test bootstrap auto
        Assertions.assertFalse(shardingEngineNacosContext.publish(null, "test.json", dataGroup, namespace));
    }

}
