package tech.finovy.framework.rocketmq.client.producer;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.distributed.queue.api.QueueService;
import tech.finovy.framework.distributed.queue.entity.PushResult;
import tech.finovy.framework.distributed.queue.entity.QueueSerialMessage;
import tech.finovy.framework.ratelimiter.RateLimiterFactoryAutoConfiguration;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RocketMqProducerAutoConfiguration.class, RateLimiterFactoryAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = RocketmqProducerTest.class)
public class RocketmqProducerTest {

    @Autowired
    private QueueService queueService;

    @Test
    @SneakyThrows
    void testRocketMqProducerClient() {
        // only init connection
        final QueueSerialMessage message = new QueueSerialMessage();
        message.setTransactionId("T921929");
        message.setTopic("MQ-TEST");
        message.setTags("*");
        message.setBody("for test");
        final PushResult result = queueService.pushSerial(message);
        log.info("{}", JSON.toJSONString(result));
        Assertions.assertEquals("SEND_OK", result.getPushStatus());
    }
}
