package tech.finovy.framework.rocketmq.client.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RocketMqProducerManagerTest {

    private RocketMqProducerManager rocketMqProducerManagerUnderTest;

    @BeforeEach
    void setUp() {
        rocketMqProducerManagerUnderTest = new RocketMqProducerManager();
    }

    @Test
    void testInit() throws Exception {
        // Setup
        final RocketMqProducerProperties properties = new RocketMqProducerProperties();
        properties.setNameserver("nameserver");
        final RocketMqProducerProperties.Login login = new RocketMqProducerProperties.Login();
        login.setName("name");
        login.setPasswd("passwd");
        properties.setLogin(login);
        final RocketMqProducerProperties.Consumer consumer = new RocketMqProducerProperties.Consumer();
        consumer.setVipChannelEnable(false);
        consumer.setConsumeMessageBatchMaxSize(0);
        consumer.setPullBatchSize(0);
        consumer.setThreadMinSize(0);
        properties.setConsumer(consumer);
        final RocketMqProducerProperties.Producer producer = new RocketMqProducerProperties.Producer();
        producer.setVipChannelEnable(false);
        producer.setRetryTimesWhenSendFailed(0);
        producer.setAccessChannel("accessChannel");
        producer.setNameSpace("nameSpace");
        producer.setGroup("group");
        producer.setAccessChannel(RocketMqProducerProperties.CLOUD_ACCESS_CHANNEL);
        properties.setProducer(producer);

        // Run the test
        final DefaultMQProducer result = rocketMqProducerManagerUnderTest.init(properties);
        rocketMqProducerManagerUnderTest.destroy();
        // Verify the results
        properties.setNameserver(null);
        final DefaultMQProducer resultDefault = rocketMqProducerManagerUnderTest.init(properties);
        Assertions.assertEquals("DEFAULT_PRODUCER",resultDefault.getProducerGroup());

    }


}
