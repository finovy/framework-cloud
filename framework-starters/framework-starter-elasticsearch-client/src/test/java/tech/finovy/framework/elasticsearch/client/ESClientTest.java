package tech.finovy.framework.elasticsearch.client;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.elasticsearch.client.autoconfigure.ElasticsearchClientAutoConfiguration;
import tech.finovy.framework.elasticsearch.impl.ElasticSearchContext;
import tech.finovy.framework.elasticsearch.impl.ElasticSearchContextHolder;

import java.io.IOException;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({ElasticsearchClientAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ESClientTest.class)
public class ESClientTest {
    ElasticSearchContext context = ElasticSearchContextHolder.get();

    @Test
    void testRestClient() throws IOException {
        Request query = new Request("get", "");
        Response resp = context.getClient().performRequest(query);
        Assertions.assertNotNull(resp);
        HttpEntity entity = resp.getEntity();
        Assertions.assertNotNull(entity);
    }

    @Test
    public void testException(){
        final ElasticClientConfigurationException exception = new ElasticClientConfigurationException("mock");
        Assertions.assertNotNull(exception);
    }
}
