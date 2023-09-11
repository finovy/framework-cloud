package tech.finovy.framework.discovery.nacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.provider.DefaultDisruptorEngineProvider;
import tech.finovy.framework.core.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.core.disruptor.spi.ProcessInterface;

import java.util.List;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"tech.finovy.*"})
@ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", matchIfMissing = true)
public class ShardingEngineNacosDiscoveryConfigBootstrapConfiguration {

    @Autowired
    private CacheManager cacheManager;

    @Bean
    @ConditionalOnMissingBean(value = NacosCacheSubcribe.class, search = SearchStrategy.CURRENT)
    public NacosCacheSubcribe shardingNacosCacheSubcribe(ApplicationContext context) {
        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(), NacosCacheSubcribe.class).length > 0) {
            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), NacosCacheSubcribe.class);
        }
        return new NacosCacheSubcribe(cacheManager);
    }

    @Bean
    @ConditionalOnMissingBean(value = DisruptorEngine.class, search = SearchStrategy.CURRENT)
    public DisruptorEngine disruptorEngine(ApplicationContext context, DisruptorEventConfiguration configuration, List<ProcessInterface> listeners) {
        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(), DisruptorEngine.class).length > 0) {
            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), DisruptorEngine.class);
        }
        final DefaultDisruptorEngineProvider engineProvider = new DefaultDisruptorEngineProvider(configuration);
        // add extension
        engineProvider.addProcessInterfaces(listeners);
        return engineProvider;
    }
}
