package tech.finovy.framework.rocketmq.client.producer;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.finovy.framework.distributed.queue.api.QueueService;
import tech.finovy.framework.ratelimiter.DistributedRateLimiterFactoryManager;
import tech.finovy.framework.rocketmq.impl.QueueServiceImpl;

@ConditionalOnProperty(name = "rocketmq.nameserver")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RocketMqProducerProperties.class)
public class RocketMqProducerAutoConfiguration {

    @Bean
    public RocketMqProducerManager rocketMqProducerManager() {
        return new RocketMqProducerManager();
    }

    @Bean
    @SneakyThrows
    public DefaultMQProducer defaultMqProducer(RocketMqProducerManager manager, RocketMqProducerProperties properties) {
        return manager.init(properties);
    }

    @Bean
    public QueueService queueService(DefaultMQProducer producer, DistributedRateLimiterFactoryManager manager) {
        return new QueueServiceImpl(producer, manager);
    }
}
