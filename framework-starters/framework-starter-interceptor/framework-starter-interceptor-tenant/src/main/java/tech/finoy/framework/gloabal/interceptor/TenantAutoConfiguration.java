package tech.finoy.framework.gloabal.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tech.finovy.framework.global.interceptor.TenantConfiguration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class TenantAutoConfiguration {

    @Bean
    @RefreshScope
    public TenantConfiguration eventProperties(Environment environment) {
        return Binder.get(environment)
                .bind(TenantConfiguration.PREFIX, TenantConfiguration.class)
                .orElse(new TenantConfiguration());
    }

}
