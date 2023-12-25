package tech.finovy.framework.scheduler.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author dtype.huang
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "job")
public class JobExecutorControllerProperties {
    private boolean debug;
    private boolean skipDistributedLock;
}
