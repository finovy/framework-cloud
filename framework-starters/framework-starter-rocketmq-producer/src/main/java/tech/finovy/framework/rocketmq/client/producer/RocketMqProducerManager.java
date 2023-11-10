package tech.finovy.framework.rocketmq.client.producer;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;


public class RocketMqProducerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqProducerManager.class);

    static {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
    }

    private DefaultMQProducer producer = null;

    public DefaultMQProducer init(RocketMqProducerProperties properties) throws MQClientException {
        if (StringUtils.isBlank(properties.getNameserver())) {
            LOGGER.info("empty mq {}-----------------------", properties.getNameserver());
            return new DefaultMQProducer();
        }
        LOGGER.info("init mq {}-----------------------", properties.getNameserver());
        if (StringUtils.isBlank(properties.getLogin().getName())) {
            producer = new DefaultMQProducer(properties.getProducer().getGroup());
        } else {
            LOGGER.info("init MQProducer({}) with ACL-----------------------", properties.getNameserver());
            producer = new DefaultMQProducer(properties.getProducer().getGroup(), new AclClientRPCHook(new SessionCredentials(properties.getLogin().getName(), properties.getLogin().getPasswd())));
        }
        producer.setNamesrvAddr(properties.getNameserver());
        producer.setVipChannelEnabled(properties.getProducer().isVipChannelEnable());
        if (properties.getProducer().getRetryTimesWhenSendFailed() > -1) {
            producer.setRetryTimesWhenSendFailed(properties.getProducer().getRetryTimesWhenSendFailed());
        }
        if (RocketMqProducerProperties.CLOUD_ACCESS_CHANNEL.equals(properties.getProducer().getAccessChannel())) {
            producer.setAccessChannel(AccessChannel.CLOUD);
        }
        if (!StringUtils.isEmpty(properties.getProducer().getNameSpace())) {
            producer.setNamespace(properties.getProducer().getNameSpace());
        }
        producer.start();
        LOGGER.info("init MQProducer({}) success-----------------------", properties.getNameserver());
        return producer;
    }

    @PreDestroy
    public void destroy() {
        if (producer != null) {
            producer.shutdown();
        }
    }
}
