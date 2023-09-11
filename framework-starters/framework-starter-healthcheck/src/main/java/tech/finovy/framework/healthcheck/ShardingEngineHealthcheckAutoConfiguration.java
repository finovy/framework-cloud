package tech.finovy.framework.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"tech.finovy.framework.healthcheck"})
public class ShardingEngineHealthcheckAutoConfiguration {


}
