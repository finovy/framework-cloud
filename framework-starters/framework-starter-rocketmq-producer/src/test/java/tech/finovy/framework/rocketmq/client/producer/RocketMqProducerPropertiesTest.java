package tech.finovy.framework.rocketmq.client.producer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RocketMqProducerPropertiesTest {

    private RocketMqProducerProperties properties;

    @BeforeEach
    void setUp() {
        properties = new RocketMqProducerProperties();
    }

    @Test
    public void testRocketMqProducerProperties(){
        final RocketMqProducerProperties.Login login = new RocketMqProducerProperties.Login();
        login.setName("user");
        login.setPasswd("password");
        final String passwd = login.getPasswd();
        Assertions.assertEquals("password",passwd);
        // producer
        final RocketMqProducerProperties.Producer producer = new RocketMqProducerProperties.Producer();
        producer.setVipChannelEnable(true);
        producer.isVipChannelEnable();
        producer.isEnableMessageTrace();
        producer.setEnableMessageTrace(false);
        producer.isCustomizedTraceTopic();
        producer.setCustomizedTraceTopic(false);
        producer.setRetryTimesWhenSendFailed(1);
        producer.setAccessChannel("test");
        producer.setNameSpace("nameSpace");
        producer.setGroup("group");
        // consumer
        final RocketMqProducerProperties.Consumer consumer = new RocketMqProducerProperties.Consumer();
        consumer.isVipChannelEnable();
        consumer.setVipChannelEnable(true);
        consumer.getConsumeMessageBatchMaxSize();
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.setPullBatchSize(1);
        consumer.getPullBatchSize();
        consumer.setThreadMinSize(1);
        consumer.getThreadMinSize();
        consumer.setThreadMinSize(1);
        consumer.getThreadMaxSize();
        consumer.setNamespace("nameSpace");
        consumer.getNamespace();
        consumer.setAccessChannel("test");
        consumer.getAccessChannel();
        consumer.getPullThresholdForQueue();
        consumer.setPullThresholdForQueue(20);
        final RocketMqProducerProperties rocketMqProducerProperties = new RocketMqProducerProperties();
        rocketMqProducerProperties.setProducer(producer);
        rocketMqProducerProperties.setConsumer(consumer);
        rocketMqProducerProperties.setLogin(login);
        rocketMqProducerProperties.getConsumer();
    }
}
