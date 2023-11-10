package tech.finovy.framework.mongodb.client;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.mongodb.client.autoconfigure.MongoDbClientAutoConfiguration;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({MongoDbClientAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = MongoDbClientTest.class)
public class MongoDbClientTest {

    @Autowired
    private MongodbClientSourceMap clientSourceMap;

    @Test
    void TestMongoClient() {
        // only init connection
        MongoDatabase database = clientSourceMap.getMongodbDatabase("export-system");
        Assertions.assertNotNull(database);
    }
}
