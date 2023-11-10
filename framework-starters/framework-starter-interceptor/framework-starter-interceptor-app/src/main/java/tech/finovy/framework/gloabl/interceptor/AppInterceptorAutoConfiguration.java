package tech.finovy.framework.gloabl.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.finovy.framework.global.interceptor.SessionInterceptor;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class AppInterceptorAutoConfiguration {

    @Bean
    public SessionInterceptor sessionInterceptor(){
        return new SessionInterceptor();
    }
}
