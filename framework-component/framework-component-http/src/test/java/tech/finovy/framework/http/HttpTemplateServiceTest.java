package tech.finovy.framework.http;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tech.finovy.framework.http.resttempalte.HttpTemplatePack;
import tech.finovy.framework.http.resttempalte.HttpTemplateService;
import tech.finovy.framework.http.resttempalte.RestTemplateProperties;


public class HttpTemplateServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpTemplateServiceTest.class);

    @Test
    void testHttpTemplateService() {
        final RestTemplateProperties restTemplateProperties = new RestTemplateProperties();
        final RestTemplate loadbalanceRestTemplate = new RestTemplate();
        final RestTemplate restTemplate = new RestTemplate();
        final HttpTemplateService<RestTemplate> httpTemplateService = new HttpTemplateService<>(restTemplateProperties,loadbalanceRestTemplate,restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.set("TX_ID", "123");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT_ENCODING, MediaType.ALL_VALUE);
        HttpEntity httpEntity = new HttpEntity<>("{}", headers);
        String api = "http://10.7.0.27:8848/nacos";
        HttpTemplatePack<RestTemplate> httpTemplatePack = httpTemplateService.choice(api);
        // code cover
        httpTemplatePack.setRestTemplate(restTemplate);
        ResponseEntity<String> response = httpTemplatePack.getRestTemplate().exchange(httpTemplatePack.getHost(), HttpMethod.GET, httpEntity, String.class);
        LOGGER.info("response:{} {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
    }

    @Test
    public void testChoice(){
        final RestTemplateProperties restTemplateProperties = new RestTemplateProperties();
        restTemplateProperties.setEnableHttp(true);
        final RestTemplate loadbalanceRestTemplate = new RestTemplate();
        final RestTemplate restTemplate = new RestTemplate();
        final HttpTemplateService<RestTemplate> httpTemplateService = new HttpTemplateService<>(restTemplateProperties,loadbalanceRestTemplate,restTemplate);
        Assertions.assertNotNull(httpTemplateService.choice("http://127.0.0.1/"));
        Assertions.assertNotNull(httpTemplateService.choice("lb://framework-cloud"));
        Assertions.assertNotNull(httpTemplateService.choice("lbs://framework-cloud"));
    }
}
