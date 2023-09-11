package tech.finovy.framework.config.nacos.listener;

import com.alibaba.nacos.api.exception.NacosException;
import tech.finovy.framework.config.nacos.context.ShardinEngineNacosContext;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dtype.huang
 */
@Slf4j
@ComponentScan(basePackages = {"tech.finovy.*"})
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestNacosConfigurationRepository.class)
class TestNacosConfigurationRepository {

    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.config.group-id}")
    private String dataGroup;
    @Autowired
    private ShardinEngineNacosContext shardinEngineNacosContext;

    @Test
    @DisplayName("TestNacosConfigDefaultItemRepository")
    void nacosConfigDefaultItemRepositoryTest() {
        List<NacosConfigDefinitionListener> configurationEndpoints = new ArrayList<>();
        configurationEndpoints.add(new TestListener("test1.yaml", dataGroup, namespace));
        configurationEndpoints.add(new TestListener("test2.yaml", dataGroup, namespace));
        configurationEndpoints.add(new TestListener("test3.yaml", dataGroup, namespace));
        configurationEndpoints.add(new TestListener1("test3.yaml", dataGroup, namespace));
        configurationEndpoints.add(new TestListener2("test3.yaml", dataGroup, namespace));
        shardinEngineNacosContext.addNacosConfigDefinitionListeners(configurationEndpoints);
        shardinEngineNacosContext.getNacosConfigDefinition(new TestListener("ci-dev_continuous-app_framework-continuous.json", dataGroup, namespace));
    }

    @Test
    @DisplayName("TestNacosConfigPublish")
    void nacosConfigPublishTest() throws NacosException {
        TestItem item = new TestItem();
        item.setA("TEAD");
        item.setB("CCCCC");
        shardinEngineNacosContext.publish(item, "testc.json", dataGroup, namespace);
        item.setB("CCCCC22");
        shardinEngineNacosContext.publish(item, "testc.json", dataGroup, namespace);
    }

}
