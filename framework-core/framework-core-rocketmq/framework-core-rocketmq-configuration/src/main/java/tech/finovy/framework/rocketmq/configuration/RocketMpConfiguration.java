package tech.finovy.framework.rocketmq.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author dtype.huang
 */
@Getter
@RefreshScope
@Configuration
public class RocketMpConfiguration {
    @Value("${spring.application.name:sharding-engine}")
    private String applicationName;
    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${rocketmq.producer.data-id:sharding-engine-rocketmq-producer.json}")
    private String producerDataId;
    @Value("${rocketmq.producer.data-group:DEFAULT_GROUP}")
    private String producerDataGroup;

    @Value("${rocketmq.consumer.data-id:sharding-engine-continuous-notify.json}")
    private String consumerDataId;
    @Value("${rocketmq.consumer.data-group:DEFAULT_GROUP}")
    private String consumerDataGroup;

}
