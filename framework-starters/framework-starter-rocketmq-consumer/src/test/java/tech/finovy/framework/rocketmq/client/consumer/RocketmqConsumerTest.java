package tech.finovy.framework.rocketmq.client.consumer;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
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

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RocketMqConsumerAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = RocketmqConsumerTest.class)
public class RocketmqConsumerTest {

    @Autowired
    private RocketMqConsumer rocketMqConsumer;

    @Test
    @SneakyThrows
    void testRocketMqConsumerClient() {
        final DefaultMQPushConsumer consumer = rocketMqConsumer.getConsumer("TEST","MQ-TEST","*");
        consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> ConsumeConcurrentlyStatus.CONSUME_SUCCESS);
        consumer.start();
        // only init connection
        Assertions.assertNotNull(consumer);
        rocketMqConsumer.destroy();
    }


}
