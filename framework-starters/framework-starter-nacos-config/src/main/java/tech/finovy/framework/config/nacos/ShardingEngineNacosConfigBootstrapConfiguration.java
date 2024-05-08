package tech.finovy.framework.config.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
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
import tech.finovy.framework.config.nacos.context.ShardingEngineNacosContext;
import tech.finovy.framework.config.nacos.listener.NacosConfigDefinitionListener;
import tech.finovy.framework.config.nacos.listener.ShardingEngineInitApplicationListener;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.provider.DefaultDisruptorEngineProvider;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.disruptor.spi.ProcessInterface;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"tech.finovy.framework.config.nacos.*"})
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", matchIfMissing = true)
@AutoConfigureAfter(NacosConfigDefinitionListener.class)
public class ShardingEngineNacosConfigBootstrapConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ShardingEngineNacosContext shardingEngineNacosContext(ApplicationContext context, NacosConfigManager nacosConfigManager, List<NacosConfigDefinitionListener> listeners) {
//        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(), ShardingEngineNacosContext.class).length > 0) {
//            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), ShardingEngineNacosContext.class);
//        }
        return new ShardingEngineNacosContext(nacosConfigManager.getConfigService(), listeners);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationListener shardingEngineInitApplicationListener(ApplicationContext context) {
//        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(), ShardingEngineInitApplicationListener.class).length > 0) {
//            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), ShardingEngineInitApplicationListener.class);
//        }
        ShardingEngineInitApplicationListener shardingEngineInitApplicationListener = new ShardingEngineInitApplicationListener();
        return shardingEngineInitApplicationListener;
    }

//    @Bean Bootstrap上下文不使用内容刷新，由application上下文做
//    @ConditionalOnMissingBean(value = DisruptorEngine.class, search = SearchStrategy.CURRENT)
//    public DisruptorEngine disruptorEngine(ApplicationContext context, DisruptorEventConfiguration configuration, List<ProcessInterface> listeners) {
////        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(), DisruptorEngine.class).length > 0) {
////            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), DisruptorEngine.class);
////        }
//        final DefaultDisruptorEngineProvider engineProvider = new DefaultDisruptorEngineProvider(configuration);
//        // add extension
//        engineProvider.addProcessInterfaces(listeners);
//        return engineProvider;
//    }
}
