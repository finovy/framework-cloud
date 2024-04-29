package tech.finovy.framework.datasource.dynamic;

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
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.datasource.dynamic.manager.DynamicDatasourceListener;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RefreshAutoConfiguration.class,DynamicDatasourceAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = DynamicDatasourceAutoConfiguration.class)
class DynamicDatasourceAutoConfigurationTest {

    @Autowired
    private DynamicDatasourceListener dynamicDatasourceListener;

    @Test
    void testAutoConfiguration(){
        Assertions.assertNotNull(dynamicDatasourceListener);
    }
}
