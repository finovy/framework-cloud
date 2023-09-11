package tech.finovy.framework.logappender;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import tech.finovy.framework.logappender.conf.ProducerConfig;
import tech.finovy.framework.logappender.conf.ProjectConfig;
import tech.finovy.framework.logappender.entry.LogItem;
import tech.finovy.framework.logappender.exception.ProducerException;
import tech.finovy.framework.logappender.push.LogProducer;
import tech.finovy.framework.logappender.push.Producer;

import java.util.*;


public class LogHttpAppender<E> extends UnsynchronizedAppenderBase<E> {

    private static final String APPLICATION_NAME = "APPLICATION_NAME";
    protected Encoder<E> encoder;
    protected ProducerConfig producerConfig = new ProducerConfig();
    protected ProjectConfig projectConfig;
    protected Producer producer;
    protected String logStore;
    protected String topic = "";
    protected String source = "";
    protected int maxThrowable = 500;
    private String project;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String appName;
    private String userAgent = "logback";
    private String mdcFields;

    @Override
    public void start() {
        try {
            doStart();
        } catch (Exception e) {
            addError("Failed to start LogHttpAppender.", e);
        }
    }

    private void doStart() {
        Map<String, String> map = System.getenv();
        if (map.get(APPLICATION_NAME) != null) {
            userAgent = map.get(APPLICATION_NAME);
            if (logStore == null || logStore.isEmpty()) {
                logStore = map.get(APPLICATION_NAME);
            }
            if (topic == null || topic.isEmpty()) {
                topic = map.get(APPLICATION_NAME);
            }
            if (logStore == null || logStore.isEmpty()) {
                logStore = map.get(APPLICATION_NAME);
            }
        }
        if (map.get("LOG_STORE_USER_AGENT") != null) {
            userAgent = map.get("LOG_STORE_USER_AGENT");
        }
        if (map.get("LOG_STORE_PROJECT") != null) {
            project = map.get("LOG_STORE_PROJECT");
        }
        if (map.get("LOG_STORE_KEY") != null) {
            accessKeyId = map.get("LOG_STORE_KEY");
        }
        if (map.get("LOG_STORE_SECRET") != null) {
            accessKeySecret = map.get("LOG_STORE_SECRET");
        }
        if (map.get("LOG_STORE") != null) {
            logStore = map.get("LOG_STORE");
        }
        if (map.get("LOG_STORE_TOPIC") != null) {
            topic = map.get("LOG_STORE_TOPIC");
        }
        if (map.get("MDC_FIELDS") != null) {
            mdcFields = map.get("MDC_FIELDS");
        }
        if (map.get("LOG_STORE_ENDPOINT") != null) {
            endpoint = map.get("LOG_STORE_ENDPOINT");
        }
        if (endpoint == null || endpoint.isEmpty()) {
            System.out.println("SKIP HTTP_LOG_STORE CPU:" + Runtime.getRuntime().availableProcessors());
            super.start();
            return;
        }
        producer = createProducer();
        super.start();
    }

    public Producer createProducer() {
        projectConfig = buildProjectConfig();
        System.out.println("START HTTP_LOG_STORE CPU:" + Runtime.getRuntime().availableProcessors() + " Endpoint:" + endpoint);
        producerConfig.setLogFormat(ProducerConfig.LogFormat.JSON);
        Producer logProducer = new LogProducer(producerConfig);
        logProducer.putProjectConfig(projectConfig);
        return logProducer;
    }

    private ProjectConfig buildProjectConfig() {
        return new ProjectConfig(project, endpoint, accessKeyId, accessKeySecret, null, userAgent);
    }

    @Override
    public void stop() {
        try {
            doStop();
        } catch (Exception e) {
            addError("Failed to stop LogHttpAppender.", e);
            Thread.currentThread().interrupt();
        }
    }

    private void doStop() throws InterruptedException, ProducerException {
        if (!isStarted()) {
            return;
        }
        super.stop();
        if (producer != null) {
            producer.close();
        }
    }

    @Override
    public void append(E eventObject) {
        try {
            appendEvent(eventObject);
        } catch (Exception e) {
            addError("Failed to append event.", e);
        }
    }

    private void appendEvent(E eventObject) {
        if (!(eventObject instanceof LoggingEvent) || producer == null) {
            return;
        }
        LoggingEvent event = (LoggingEvent) eventObject;
        List<LogItem> logItems = new ArrayList<>(1);
        LogItem item = new LogItem();
        logItems.add(item);
        item.setTime(event.getTimeStamp());
        item.pushBack("level", event.getLevel().toString());
        item.pushBack("thread", event.getThreadName());
        item.pushBack("appName", appName);
        StackTraceElement[] caller = event.getCallerData();
        if (caller != null && caller.length > 0) {
            item.pushBack("location", caller[0].toString());
        }
        String message = event.getFormattedMessage();
        item.pushBack("message", message);
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            StringBuilder throwable = new StringBuilder(this.getExceptionInfo(throwableProxy));
            do {
                throwable.append(this.fullDump(throwableProxy.getStackTraceElementProxyArray()));
                throwableProxy = throwableProxy.getCause();
                if (throwableProxy != null) {
                    throwable.append("\n\nCaused by:")
                            .append(this.getExceptionInfo(throwableProxy));
                }
            } while (throwableProxy != null);
            String throwableSub;
            if (throwable.length() > maxThrowable) {
                throwableSub = throwable.substring(0, maxThrowable);
            } else {
                throwableSub = throwable.toString();
            }
            item.pushBack("throwable", throwableSub);
        }
        if (this.encoder != null) {
            item.pushBack("log", new String(this.encoder.encode(eventObject)));
        }
        Optional.ofNullable(mdcFields).ifPresent(
                f -> event.getMDCPropertyMap().entrySet().stream()
                        .filter(v -> Arrays.stream(f.split(",")).anyMatch(i -> i.equals(v.getKey())))
                        .forEach(map -> item.pushBack(map.getKey(), map.getValue()))
        );
        try {
            producer.send(projectConfig.getProject(), logStore, topic, source, logItems, new LogHttpAppenderCallback<>(this, projectConfig.getProject(), logStore, topic, source, logItems));
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getExceptionInfo(IThrowableProxy iThrowableProxy) {
        String s = iThrowableProxy.getClassName();
        String message = iThrowableProxy.getMessage();
        return (message != null) ? (s + ": " + message) : s;
    }

    private String fullDump(StackTraceElementProxy[] stackTraceElementProxyArray) {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElementProxy step : stackTraceElementProxyArray) {
            builder.append(CoreConstants.LINE_SEPARATOR);
            String string = step.toString();
            builder.append(CoreConstants.TAB).append(string);
            ThrowableProxyUtil.subjoinPackagingData(builder, step);
        }
        return builder.toString();
    }

    public String getLogStore() {
        return logStore;
    }

    public void setLogStore(String logStore) {
        this.logStore = logStore;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getTotalSizeInBytes() {
        return producerConfig.getTotalSizeInBytes();
    }

    public void setTotalSizeInBytes(int totalSizeInBytes) {
        producerConfig.setTotalSizeInBytes(totalSizeInBytes);
    }

    public long getMaxBlockMs() {
        return producerConfig.getMaxBlockMs();
    }

    public void setMaxBlockMs(long maxBlockMs) {
        producerConfig.setMaxBlockMs(maxBlockMs);
    }

    public int getIoThreadCount() {
        return producerConfig.getIoThreadCount();
    }

    public void setIoThreadCount(int ioThreadCount) {
        producerConfig.setIoThreadCount(ioThreadCount);
    }

    public int getBatchSizeThresholdInBytes() {
        return producerConfig.getBatchSizeThresholdInBytes();
    }

    public void setBatchSizeThresholdInBytes(int batchSizeThresholdInBytes) {
        producerConfig.setBatchSizeThresholdInBytes(batchSizeThresholdInBytes);
    }

    public int getBatchCountThreshold() {
        return producerConfig.getBatchCountThreshold();
    }

    public void setBatchCountThreshold(int batchCountThreshold) {
        producerConfig.setBatchCountThreshold(batchCountThreshold);
    }

    public int getLingerMs() {
        return producerConfig.getLingerMs();
    }

    public void setLingerMs(int lingerMs) {
        producerConfig.setLingerMs(lingerMs);
    }

    public int getRetries() {
        return producerConfig.getRetries();
    }

    public void setRetries(int retries) {
        producerConfig.setRetries(retries);
    }

    public int getMaxReservedAttempts() {
        return producerConfig.getMaxReservedAttempts();
    }

    public void setMaxReservedAttempts(int maxReservedAttempts) {
        producerConfig.setMaxReservedAttempts(maxReservedAttempts);
    }

    public long getBaseRetryBackoffMs() {
        return producerConfig.getBaseRetryBackoffMs();
    }

    public void setBaseRetryBackoffMs(long baseRetryBackoffMs) {
        producerConfig.setBaseRetryBackoffMs(baseRetryBackoffMs);
    }

    public long getMaxRetryBackoffMs() {
        return producerConfig.getMaxRetryBackoffMs();
    }

    public void setMaxRetryBackoffMs(long maxRetryBackoffMs) {
        producerConfig.setMaxRetryBackoffMs(maxRetryBackoffMs);
    }

    public Encoder<E> getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

    public void setMdcFields(String mdcFields) {
        this.mdcFields = mdcFields;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getMaxThrowable() {
        return maxThrowable;
    }

    public void setMaxThrowable(int maxThrowable) {
        this.maxThrowable = maxThrowable;
    }
}
