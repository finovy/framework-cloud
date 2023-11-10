package tech.finovy.framework.healthcheck;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@RefreshScope
@Configuration
public class HealthcheckConfiguration {
    @Value("${spring.application.name:sharding-engine}")
    private String applicationName;
    @Value("${spring.cloud.nacos.discovery.namespace:}")
    private String namespace;
    @Value("${healthcheck.debug:false}")
    private boolean debug;
}
