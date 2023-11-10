package tech.finovy.framework.mongodb.client.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tech.finovy.framework.mongodb.client.MongoDbClientConfigListener;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "mongdb.enable", matchIfMissing = true)
@EnableConfigurationProperties(MongoDbClientProperties.class)
public class MongoDbClientAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public MongoDbClientConfigListener mongodbClientSourceMap(MongoDbClientProperties properties, ApplicationContext context) {
        return new MongoDbClientConfigListener(properties,context);
    }
}
