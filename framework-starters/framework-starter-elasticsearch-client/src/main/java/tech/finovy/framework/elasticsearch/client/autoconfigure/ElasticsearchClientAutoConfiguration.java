package tech.finovy.framework.elasticsearch.client.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tech.finovy.framework.elasticsearch.api.ElasticSearchService;
import tech.finovy.framework.elasticsearch.client.ElasticSearchClientManager;
import tech.finovy.framework.elasticsearch.impl.ElasticSearchServiceImpl;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ElasticClientProperties.class)
@ConditionalOnProperty(value = "elasticsearch.enable", matchIfMissing = true)
public class ElasticsearchClientAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ElasticSearchClientManager elasticSearchConfigListener(ElasticClientProperties configuration, ApplicationContext context) {
        return new ElasticSearchClientManager(configuration,context);
    }

    @Bean
    public ElasticSearchService elasticSearchService(){
        return new ElasticSearchServiceImpl();
    }

}
