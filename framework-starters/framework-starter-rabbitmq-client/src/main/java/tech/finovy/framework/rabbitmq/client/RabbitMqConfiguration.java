package tech.finovy.framework.rabbitmq.client;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@RefreshScope
@Configuration
public class RabbitMqConfiguration {
    @Value("${rabbit-mq.url:}")
    private String url;
}
