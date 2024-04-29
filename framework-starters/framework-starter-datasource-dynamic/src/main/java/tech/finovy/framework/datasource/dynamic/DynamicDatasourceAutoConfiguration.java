package tech.finovy.framework.datasource.dynamic;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tech.finovy.framework.datasource.api.DynamicDatasourceApi;
import tech.finovy.framework.datasource.dynamic.executor.DynamicDatasourceImpl;
import tech.finovy.framework.datasource.dynamic.manager.DynamicDatasourceListener;
import tech.finovy.framework.datasource.dynamic.manager.DynamicDatasourceProperties;
import tech.finovy.framework.datasource.dynamic.pools.DynamicDataSourceMap;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "dynamic-datasource.enable", matchIfMissing = true)
public class DynamicDatasourceAutoConfiguration {


    @Bean
    @RefreshScope
    public DynamicDatasourceProperties restTemplateProperties(Environment environment) {
        return Binder.get(environment)
                .bind(DynamicDatasourceProperties.PREFIX, DynamicDatasourceProperties.class)
                .orElse(new DynamicDatasourceProperties());
    }

    @Bean
    public DynamicDatasourceListener dynamicDatasourceListener(DynamicDatasourceProperties properties){
        return new DynamicDatasourceListener(properties);
    }

    @Bean
    public DynamicDataSourceMap dynamicDataSourceMap(){
        return new DynamicDataSourceMap();
    }

    @Bean
    public DynamicDatasourceApi dynamicDatasourceApi(DynamicDataSourceMap map){
        return new DynamicDatasourceImpl(map);
    }

}
