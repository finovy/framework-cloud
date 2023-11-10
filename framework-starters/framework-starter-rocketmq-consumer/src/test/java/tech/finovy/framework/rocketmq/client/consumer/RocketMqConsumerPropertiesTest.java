package tech.finovy.framework.rocketmq.client.consumer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RocketMqConsumerPropertiesTest {

    private RocketMqConsumerProperties properties;

    @BeforeEach
    void setUp() {
        properties = new RocketMqConsumerProperties();
    }

    @SneakyThrows
    @Test
    public void test(){
        final RocketMqConsumerProperties.Login login = new RocketMqConsumerProperties.Login();
        final RocketMqConsumerProperties.Consumer consumer = new RocketMqConsumerProperties.Consumer();
        final RocketMqConsumerProperties.Producer producer = new RocketMqConsumerProperties.Producer();

        login.getName();
        login.setName("name");
        login.getPasswd();
        login.setPasswd("password");

        consumer.getThreadMaxSize();
        consumer.setThreadMinSize(1);
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.getConsumeMessageBatchMaxSize();
        consumer.setNamespace("namespace");
        consumer.getNamespace();
        consumer.setPullBatchSize(1);
        consumer.getPullBatchSize();
        consumer.setVipChannelEnable(true);
        consumer.isVipChannelEnable();
        consumer.setAccessChannel(RocketMqConsumerProperties.CLOUD_ACCESS_CHANNEL);
        consumer.getAccessChannel();
        consumer.setThreadMaxSize(1);
        consumer.getThreadMinSize();
        consumer.getPullThresholdForQueue();
        consumer.setPullThresholdForQueue(1);

        producer.setProducerGroup("group");
        producer.setProducerNamespace("namespace");
        producer.setProducerAccessChannel("test");
        producer.setRetryTimesWhenSendFailed(1);
        producer.setCustomizedTraceTopic(true);
        producer.setEnableMessageTrace(true);
        producer.setVipChannelEnable(true);

        producer.getProducerGroup();
        producer.getProducerNamespace();
        producer.getProducerAccessChannel();
        producer.getRetryTimesWhenSendFailed();
        producer.isCustomizedTraceTopic();
        producer.isEnableMessageTrace();
        producer.isVipChannelEnable();

        properties.setConsumer(consumer);
        properties.setProducer(producer);
        properties.setLogin(login);
        properties.setNameserver("nameServer");

        final RocketMqConsumer rocketMqConsumer = new RocketMqConsumer(properties);
        rocketMqConsumer.getConsumer("groupId","test","*");
    }
}
