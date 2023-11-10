package tech.finovy.framework.rocketmq.client.producer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = RocketMqProducerProperties.PREFIX)
public class RocketMqProducerProperties {
    public static final String PREFIX = "rocketmq";
    public static final String CLOUD_ACCESS_CHANNEL = "cloud";
    private String nameserver;
    private Login login = new Login();
    private Consumer consumer = new Consumer();
    private Producer producer = new Producer();

    static class Login {
        private String name;
        private String passwd;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
    }

    static class Producer {
        private boolean vipChannelEnable;

        private boolean enableMessageTrace;

        private boolean customizedTraceTopic;

        private int retryTimesWhenSendFailed = -1;

        private String accessChannel;

        private String nameSpace;

        private String group = "framework-group";

        public boolean isVipChannelEnable() {
            return vipChannelEnable;
        }

        public void setVipChannelEnable(boolean vipChannelEnable) {
            this.vipChannelEnable = vipChannelEnable;
        }

        public boolean isEnableMessageTrace() {
            return enableMessageTrace;
        }

        public void setEnableMessageTrace(boolean enableMessageTrace) {
            this.enableMessageTrace = enableMessageTrace;
        }

        public boolean isCustomizedTraceTopic() {
            return customizedTraceTopic;
        }

        public void setCustomizedTraceTopic(boolean customizedTraceTopic) {
            this.customizedTraceTopic = customizedTraceTopic;
        }

        public int getRetryTimesWhenSendFailed() {
            return retryTimesWhenSendFailed;
        }

        public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
            this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
        }

        public String getAccessChannel() {
            return accessChannel;
        }

        public void setAccessChannel(String accessChannel) {
            this.accessChannel = accessChannel;
        }

        public String getNameSpace() {
            return nameSpace;
        }

        public void setNameSpace(String nameSpace) {
            this.nameSpace = nameSpace;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }

    static class Consumer {
        private boolean vipChannelEnable;

        private int consumeMessageBatchMaxSize = -1;

        private int pullBatchSize = -1;

        private int threadMinSize = -1;

        private int threadMaxSize = -1;

        private String namespace;

        private String accessChannel;

        private int pullThresholdForQueue = 500;

        public boolean isVipChannelEnable() {
            return vipChannelEnable;
        }

        public void setVipChannelEnable(boolean vipChannelEnable) {
            this.vipChannelEnable = vipChannelEnable;
        }

        public int getConsumeMessageBatchMaxSize() {
            return consumeMessageBatchMaxSize;
        }

        public void setConsumeMessageBatchMaxSize(int consumeMessageBatchMaxSize) {
            this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
        }

        public int getPullBatchSize() {
            return pullBatchSize;
        }

        public void setPullBatchSize(int pullBatchSize) {
            this.pullBatchSize = pullBatchSize;
        }

        public int getThreadMinSize() {
            return threadMinSize;
        }

        public void setThreadMinSize(int threadMinSize) {
            this.threadMinSize = threadMinSize;
        }

        public int getThreadMaxSize() {
            return threadMaxSize;
        }

        public void setThreadMaxSize(int threadMaxSize) {
            this.threadMaxSize = threadMaxSize;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getAccessChannel() {
            return accessChannel;
        }

        public void setAccessChannel(String accessChannel) {
            this.accessChannel = accessChannel;
        }

        public int getPullThresholdForQueue() {
            return pullThresholdForQueue;
        }

        public void setPullThresholdForQueue(int pullThresholdForQueue) {
            this.pullThresholdForQueue = pullThresholdForQueue;
        }
    }

    public String getNameserver() {
        return nameserver;
    }

    public void setNameserver(String nameserver) {
        this.nameserver = nameserver;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }
}
