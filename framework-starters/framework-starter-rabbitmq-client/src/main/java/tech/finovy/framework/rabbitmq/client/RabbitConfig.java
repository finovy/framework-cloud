package tech.finovy.framework.rabbitmq.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@RefreshScope
@Configuration
public class RabbitConfig {
    @Autowired
    private RabbitMqConfiguration rabbitMqConfiguration;

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(rabbitMqConfiguration.getUrl());
//        factory.setAddresses(rabbitProperties.getAddresses());
//        factory.setUsername(rabbitProperties.getUsername());
//        factory.setPassword(rabbitProperties.getPassword());
//        factory.setVirtualHost(rabbitProperties.getVirtualHost());
//        factory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
//        factory.setPublisherReturns(rabbitProperties.isPublisherReturns());
//
//        factory.addChannelListener(rabbitChannelListener);
//        factory.addConnectionListener(rabbitConnectionListener);
//        factory.setRecoveryListener(rabbitRecoveryListener);
        log.info("--------------CachingConnectionFactory---------------------------------------------------------");
        return factory;
    }
}
