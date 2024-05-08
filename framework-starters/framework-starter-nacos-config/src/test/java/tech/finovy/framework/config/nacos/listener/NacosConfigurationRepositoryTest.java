package tech.finovy.framework.config.nacos.listener;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.jupiter.api.Assertions;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
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
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigBootstrapConfiguration;
import tech.finovy.framework.config.nacos.context.ShardingEngineNacosContext;
import tech.finovy.framework.config.nacos.manager.NacosConfigurationManager;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ComponentScan(basePackages = {"tech.finovy.framework.config.nacos"})
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ImportAutoConfiguration({RefreshAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = NacosConfigurationRepositoryTest.class)
public class NacosConfigurationRepositoryTest {

    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.config.group-id}")
    private String dataGroup;
    @Autowired
    private ShardingEngineNacosContext shardingEngineNacosContext;
    @Autowired
    private NacosConfigurationManager manager;

    @Test
    @DisplayName("TestNacosConfigDefaultItemRepository")
    void nacosConfigDefaultItemRepositoryTest() {
        List<NacosConfigDefinitionListener> configurationEndpoints = new ArrayList<>();
        configurationEndpoints.add(new TestListener("test1.yaml", dataGroup, namespace));
        configurationEndpoints.add(new TestListener("test2.yaml", dataGroup, namespace));
        configurationEndpoints.add(new TestListener("test3.yaml", dataGroup, namespace));
        configurationEndpoints.add(new TestListener1("test3.yaml", dataGroup, namespace));
        configurationEndpoints.add(new TestListener2("test3.yaml", dataGroup, namespace));
        shardingEngineNacosContext.addNacosConfigDefinitionListeners(configurationEndpoints);
        shardingEngineNacosContext.getNacosConfigDefinition(new TestListener("framework-store.json", dataGroup, namespace));
        shardingEngineNacosContext.getNacosConfigDefinition(new TestListener("sharding-engine-execution.json", dataGroup, namespace));
        // cover
        final ShardingEngineNacosContext context= new ShardingEngineNacosContext(null);
        manager.schedulerTrigger();
    }

    @Test
    @DisplayName("TestNacosConfigPublish")
    void nacosConfigPublishTest() throws NacosException {
        TestItem item = new TestItem();
        item.setA("AAA");
        item.setB("BBB");
        Assertions.assertFalse(shardingEngineNacosContext.publish(null, "test.json", dataGroup, namespace));
        Assertions.assertTrue(shardingEngineNacosContext.publish(item, "test.json", dataGroup, namespace));
        item.setB("CCC");
        Assertions.assertTrue(shardingEngineNacosContext.publish(item, "test.json", dataGroup, namespace));
        // json
        Assertions.assertTrue(shardingEngineNacosContext.publish(item, "test.md", dataGroup, namespace));
        // yaml
        Assertions.assertTrue(shardingEngineNacosContext.publish(item, "test.yaml", dataGroup, namespace));
        // text
        Assertions.assertTrue(shardingEngineNacosContext.publish("test info", "test.md", dataGroup, namespace));
        // text
        Assertions.assertTrue(shardingEngineNacosContext.publish("test info", "test.text", dataGroup, namespace));

        // remove
        Assertions.assertTrue(shardingEngineNacosContext.removeConfig("test.text", dataGroup));
    }

}
