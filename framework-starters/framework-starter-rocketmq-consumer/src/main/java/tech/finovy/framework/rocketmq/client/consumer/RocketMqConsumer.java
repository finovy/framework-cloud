package tech.finovy.framework.rocketmq.client.consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

public class RocketMqConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqConsumer.class);

    private static final List<DefaultMQPushConsumer> DESTROY = new ArrayList<>();

    private final RocketMqConsumerProperties properties;

    static {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
    }

    public RocketMqConsumer(RocketMqConsumerProperties properties) {
        this.properties = properties;
    }

    public DefaultMQPushConsumer getConsumer(String groupId, String topic, String tagsExpression) throws MQClientException {
        DefaultMQPushConsumer consumer;
        if (StringUtils.isEmpty(properties.getLogin().getName())) {
            consumer = new DefaultMQPushConsumer(groupId);
        } else {
            consumer = new DefaultMQPushConsumer("", groupId, new AclClientRPCHook(new SessionCredentials(properties.getLogin().getName(), properties.getLogin().getPasswd())));
        }
        consumer.setNamesrvAddr(properties.getNameserver());
        consumer.subscribe(topic, tagsExpression);
        consumer.setVipChannelEnabled(properties.getConsumer().isVipChannelEnable());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setPullThresholdForQueue(properties.getConsumer().getPullThresholdForQueue());
        if (!StringUtils.isEmpty(properties.getConsumer().getNamespace())) {
            consumer.setNamespace(properties.getConsumer().getNamespace());
        }
        if (properties.getConsumer().getConsumeMessageBatchMaxSize() != -1) {
            consumer.setConsumeMessageBatchMaxSize(properties.getConsumer().getConsumeMessageBatchMaxSize());
        }
        if (properties.getConsumer().getPullBatchSize() != -1) {
            consumer.setPullBatchSize(properties.getConsumer().getPullBatchSize());
        }
        if (RocketMqConsumerProperties.CLOUD_ACCESS_CHANNEL.equals(properties.getConsumer().getAccessChannel())) {
            consumer.setAccessChannel(AccessChannel.CLOUD);
        }
        if (properties.getConsumer().getThreadMinSize() != -1) {
            consumer.setConsumeThreadMin(properties.getConsumer().getThreadMinSize());
        }
        if (properties.getConsumer().getThreadMaxSize() != -1) {
            consumer.setConsumeThreadMax(properties.getConsumer().getThreadMaxSize());
        }
        LOGGER.info("init RocketMQ SUCCESS,{}--------------------", consumer.getNamesrvAddr());
        DESTROY.add(consumer);
        return consumer;
    }

    @PreDestroy
    public void destroy() {
        for (DefaultMQPushConsumer dest : DESTROY) {
            if (dest != null) {
                dest.shutdown();
                LOGGER.info("shutdown RocketMQ SUCCESS,{}--------------------", dest.getNamesrvAddr());
            }
        }

    }
}
