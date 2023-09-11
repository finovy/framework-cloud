package tech.finovy.framework.rocketmq.producer;

import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.client.producer.MQProducer;

@Getter
@Setter
public class RocketMqProducerContext {
    private MQProducer producer;
}
