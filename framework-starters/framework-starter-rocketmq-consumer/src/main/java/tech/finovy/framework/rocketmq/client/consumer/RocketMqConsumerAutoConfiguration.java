package tech.finovy.framework.rocketmq.client.consumer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RocketMqConsumerProperties.class)
public class RocketMqConsumerAutoConfiguration {

    @Bean
    public RocketMqConsumer rocketMqConsumer(RocketMqConsumerProperties properties) {
        return new RocketMqConsumer(properties);
    }
}
