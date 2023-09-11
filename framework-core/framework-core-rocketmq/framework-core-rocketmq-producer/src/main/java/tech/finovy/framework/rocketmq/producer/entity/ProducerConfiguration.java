package tech.finovy.framework.rocketmq.producer.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Dtype.huang
 */
@Slf4j
@Getter
@Setter
public class ProducerConfiguration {
    public static final String CLOUD_ACCESS_CHANNEL = "cloud";
    @Value("${rocketmq.nameserver:}")
    private String nameserver;
    @Value("${rocketmq.login.name:}")
    private String loginName;
    @Value("${rocketmq.login.passwd:}")
    private String loginPas;


    @Value("${rocketmq.producer.vip-channel-enable:false}")
    private boolean producerVipChannelEnabled;
    @Value("${rocketmq.producer.enable-message-trace:false}")
    private boolean enableMessageTrace;
    @Value("${rocketmq.producer.customized-trace-topic:}")
    private String customizedTraceTopic;
    @Value("${rocketmq.producer.retry-times-when-send-failed:-1}")
    private int retryTimesWhenSendFailed;
    @Value("${rocketmq.producer.access-channel:}")
    private String producerAccessChannel;
    @Value("${rocketmq.producer.namespace:}")
    private String producerNamespace;
    @Value("${rocketmq.producer.group:framework-group}")
    private String producerGroup;

    @Value("${rocketmq.consumer.vip-channel-enable:false}")
    private boolean consumerVipChannelEnabled;
    @Value("${rocketmq.consumer.consume-message-batch-max-size:-1}")
    private int consumeMessageBatchMaxSize;
    @Value("${rocketmq.consumer.pull-batch-size:-1}")
    private int consumePullBatchSize;
    @Value("${rocketmq.consumer.thread-min-size:-1}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.thread-max-size:-1}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.namespace:}")
    private String consumerNamespace;
    @Value("${rocketmq.consumer.access-channel:}")
    private String consumerAccessChannel;
    @Value("${rocketmq.consumer.pullThresholdForQueue:40}")
    private int pullThresholdForQueue;
}
