package tech.finovy.framework.http;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import tech.finovy.framework.http.resttempalte.HttpTemplateService;
import tech.finovy.framework.http.resttempalte.RestTemplateProperties;

import java.util.Arrays;

@Configuration(proxyBeanMethods = false)
public class HttpTemplateAutoConfiguration {

    @Bean
    @RefreshScope
    public RestTemplateProperties restTemplateProperties(Environment environment) {
        return Binder.get(environment)
                .bind(RestTemplateProperties.PREFIX, RestTemplateProperties.class)
                .orElse(new RestTemplateProperties());
    }

    @LoadBalanced
    @Bean("loadbalanceRestTemplate")
    public RestTemplate loadbalanceRestTemplate(RestTemplateProperties properties) {
        return createRestTemplate(properties);
    }


    @Bean("httpRestTemplate")
    public RestTemplate httpRestTemplate(RestTemplateProperties properties) {
        return createRestTemplate(properties);
    }

    private RestTemplate createRestTemplate(RestTemplateProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_XML,
                MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        return restTemplate;
    }

    @Bean
    public HttpTemplateService<RestTemplate> httpTemplateService(RestTemplateProperties properties, RestTemplate loadbalanceRestTemplate, RestTemplate httpRestTemplate) {
        return new HttpTemplateService<>(properties, loadbalanceRestTemplate, httpRestTemplate);
    }

}
