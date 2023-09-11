package tech.finovy.framework.healthcheck;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"tech.finovy.framework.healthcheck"})
public class ShardingEngineHealthcheckBootstrapConfiguration {

}
