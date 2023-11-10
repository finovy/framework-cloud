package tech.finovy.framework.event;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.distributed.event.api.AsyncEventService;
import tech.finovy.framework.distributed.event.api.EventService;
import tech.finovy.framework.event.impl.AsyncEventServiceImpl;
import tech.finovy.framework.event.impl.EventServiceImpl;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(ShardingEngineNacosConfigAutoConfiguration.class)
public class EventAutoConfiguration {

    @Bean
    @RefreshScope
    public EventProperties eventProperties(Environment environment) {
        return Binder.get(environment)
                .bind(EventProperties.PREFIX, EventProperties.class)
                .orElse(new EventProperties());
    }

    @Bean
    public AsyncEventService asyncEventService(EventService eventService) {
        return new AsyncEventServiceImpl(eventService);
    }

    @Bean
    public EventService eventService(EventProperties properties, DisruptorEngine disruptorEngine) {
        return new EventServiceImpl(properties, disruptorEngine);
    }

    @Bean
    public QueueAppEventDisruptorListener queueAppEventDisruptorListener(DefaultMQProducer defaultMqProducer, EventProperties eventproperties) {
        return new QueueAppEventDisruptorListener(defaultMqProducer, eventproperties);
    }

    @Bean
    public QueueAppSerialEventDisruptorListener queueAppSerialEventDisruptorListener(DefaultMQProducer defaultMqProducer, EventProperties eventproperties) {
        return new QueueAppSerialEventDisruptorListener(defaultMqProducer, eventproperties);
    }
}
