package tech.finovy.framework.event;

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
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.distributed.event.api.EventService;
import tech.finovy.framework.ratelimiter.RateLimiterFactoryAutoConfiguration;
import tech.finovy.framework.rocketmq.client.producer.RocketMqProducerAutoConfiguration;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RefreshAutoConfiguration.class,DisruptorEventConfiguration.class, RateLimiterFactoryAutoConfiguration.class, RocketMqProducerAutoConfiguration.class, EventAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = EventAutoConfiguration.class)
public class EventAutoConfigurationTest {

    @Autowired
    EventService eventService;

    @Test
    public void testStart() {
        Assertions.assertNotNull(eventService);
    }
}
