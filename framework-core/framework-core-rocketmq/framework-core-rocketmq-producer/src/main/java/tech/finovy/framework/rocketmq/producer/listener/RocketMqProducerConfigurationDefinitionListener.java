package tech.finovy.framework.rocketmq.producer.listener;

import tech.finovy.framework.config.nacos.listener.AbstractNacosConfigDefinitionListener;
import tech.finovy.framework.rocketmq.configuration.RocketMpConfiguration;
import tech.finovy.framework.rocketmq.producer.RocketMqProducerContext;
import tech.finovy.framework.rocketmq.producer.RocketMqProducerHolder;
import tech.finovy.framework.rocketmq.producer.entity.ProducerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Configuration
public class RocketMqProducerConfigurationDefinitionListener extends AbstractNacosConfigDefinitionListener<ProducerConfiguration> {
    static {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
    }

    @Autowired
    private RocketMpConfiguration rocketMpConfiguration;
    private RocketMqProducerContext context = RocketMqProducerHolder.get();

    public RocketMqProducerConfigurationDefinitionListener() {
        super(ProducerConfiguration.class);
    }

    @Override
    public String getDataId() {
        return rocketMpConfiguration.getProducerDataId();
    }

    @Override
    public String getDataGroup() {
        return rocketMpConfiguration.getProducerDataGroup();
    }

    @Override
    public int getOrder() {
        return super.getOrder() + 10;
    }

    @Override
    public void refresh(String dataId, String dataGroup, ProducerConfiguration config, int version) {
        if (StringUtils.isBlank(config.getNameserver())) {
            log.info("empty mq {}-----------------------", config.getNameserver());
            return;
        }
        DefaultMQProducer producer = null;
        log.info("init mq {}-----------------------", config.getNameserver());
        if (StringUtils.isBlank(config.getLoginName())) {
            producer = new DefaultMQProducer(config.getProducerGroup());
        } else {
            log.info("init MQProducer({}) with ACL-----------------------", config.getNameserver());
            producer = new DefaultMQProducer(config.getProducerGroup(), new AclClientRPCHook(new SessionCredentials(config.getLoginName(), config.getLoginPas())));
        }
        producer.setNamesrvAddr(config.getNameserver());
        producer.setVipChannelEnabled(config.isProducerVipChannelEnabled());
        if (config.getRetryTimesWhenSendFailed() > -1) {
            producer.setRetryTimesWhenSendFailed(config.getRetryTimesWhenSendFailed());
        }
        if (config.CLOUD_ACCESS_CHANNEL.equals(config.getProducerAccessChannel())) {
            producer.setAccessChannel(AccessChannel.CLOUD);
        }
        if (!StringUtils.isEmpty(config.getProducerNamespace())) {
            producer.setNamespace(config.getProducerNamespace());
        }
        try {
            producer.start();
            log.info("init MQProducer({}) success-----------------------", config.getNameserver());
            context.setProducer(producer);
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }
}
