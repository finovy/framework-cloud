package tech.finovy.framework.oss.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tech.finovy.framework.oss.client.config.OssClientProperties;
import tech.finovy.framework.oss.client.config.OssConfigurationListener;
import tech.finovy.framework.oss.client.config.SecurityProperties;
import tech.finovy.framework.oss.client.service.OssClientEncryptionService;
import tech.finovy.framework.oss.client.service.OssClientService;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "oss.enable", matchIfMissing = true)
public class OssClientAutoConfiguration {

    @Bean
    @RefreshScope
    public OssClientProperties ossClientProperties(Environment environment) {
        return Binder.get(environment)
                .bind(OssClientProperties.PREFIX, OssClientProperties.class)
                .orElse(new OssClientProperties());
    }

    @Bean
    @RefreshScope
    public SecurityProperties securityProperties(Environment environment) {
        return Binder.get(environment)
                .bind(SecurityProperties.PREFIX, SecurityProperties.class)
                .orElse(new SecurityProperties());
    }

    @Bean
    @ConditionalOnMissingBean
    public OssClientMap ossClientMap(){
        return new OssClientMap();
    }

    @Bean
    public OssConfigurationListener ossConfigurationListener(OssClientProperties properties, SecurityProperties securityProperties, OssClientMap ossClientMap){
        return new OssConfigurationListener(properties, securityProperties, ossClientMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public OssClientEncryptionService ossClientEncryptionService(OssClientMap ossClientMap,OssClientProperties properties){
        return new  OssClientEncryptionService(ossClientMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public OssClientService ossClientService(OssClientMap ossClientMap){
        return new  OssClientService(ossClientMap);
    }

}
