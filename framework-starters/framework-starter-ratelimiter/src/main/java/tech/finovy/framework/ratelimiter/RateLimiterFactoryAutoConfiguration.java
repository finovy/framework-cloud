package tech.finovy.framework.ratelimiter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RateLimiterFactoryAutoConfiguration {

    @Bean
    public DistributedRateLimiterFactoryManager distributedRateLimiterFactoryManager(){
        return new DistributedRateLimiterFactoryManager();
    }
}
