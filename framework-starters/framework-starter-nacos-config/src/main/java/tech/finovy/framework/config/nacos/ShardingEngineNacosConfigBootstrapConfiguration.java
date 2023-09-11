package tech.finovy.framework.config.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
import tech.finovy.framework.config.nacos.context.ShardinEngineNacosContext;
import tech.finovy.framework.config.nacos.listener.NacosConfigDefinitionListener;
import tech.finovy.framework.config.nacos.listener.ShardingEngineInitApplicationListener;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.provider.DefaultDisruptorEngineProvider;
import tech.finovy.framework.core.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.core.disruptor.spi.ProcessInterface;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"tech.finovy.*"})
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", matchIfMissing = true)
@AutoConfigureAfter(NacosConfigDefinitionListener.class)
public class ShardingEngineNacosConfigBootstrapConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ShardinEngineNacosContext shardinEngineNacosContext(ApplicationContext context, NacosConfigManager nacosConfigManager, List<NacosConfigDefinitionListener> listeners) {
        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(), ShardinEngineNacosContext.class).length > 0) {
            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), ShardinEngineNacosContext.class);
        }
        ShardinEngineNacosContext shardinEngineNacosContext = new ShardinEngineNacosContext(nacosConfigManager.getConfigService(), listeners);
        return shardinEngineNacosContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationListener shardingEngineInitApplicationListener(ApplicationContext context) {
        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(), ShardingEngineInitApplicationListener.class).length > 0) {
            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), ShardingEngineInitApplicationListener.class);
        }
        ShardingEngineInitApplicationListener shardingEngineInitApplicationListener = new ShardingEngineInitApplicationListener();
        return shardingEngineInitApplicationListener;
    }

    @Bean
    @ConditionalOnMissingBean(value = DisruptorEngine.class, search = SearchStrategy.CURRENT)
    public DisruptorEngine disruptorEngine(ApplicationContext context, DisruptorEventConfiguration configuration,List<ProcessInterface> listeners) {
        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(), DisruptorEngine.class).length > 0) {
            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), DisruptorEngine.class);
        }
        final DefaultDisruptorEngineProvider engineProvider = new DefaultDisruptorEngineProvider(configuration);
        // add extension
        engineProvider.addProcessInterfaces(listeners);
        return engineProvider;
    }
}
