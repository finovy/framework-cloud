package tech.finovy.framework.disruptor;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;

@AutoConfiguration
@Configuration(proxyBeanMethods = false)
public class DisruptorEventAutoConfiguration {

    @Bean
    @RefreshScope
    @ConditionalOnMissingBean(value = DisruptorEventConfiguration.class)
    public DisruptorEventConfiguration disruptorEventConfiguration(Environment environment) {
        final String applicationName = environment.getProperty("spring.application.name");
        final DisruptorEventConfiguration configuration = Binder.get(environment)
                .bind(DisruptorEventConfiguration.PREFIX, DisruptorEventConfiguration.class)
                .orElse(new DisruptorEventConfiguration());
        if (StringUtils.hasLength(applicationName)) {
            configuration.setApplicationName(applicationName);
        }
        return configuration;
    }

}
