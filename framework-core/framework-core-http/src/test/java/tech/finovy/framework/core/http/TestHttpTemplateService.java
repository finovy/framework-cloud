package tech.finovy.framework.core.http;


import tech.finovy.framework.core.http.resttempalte.HttpTemplatePack;
import tech.finovy.framework.core.http.resttempalte.HttpTemplateService;
import tech.finovy.framework.core.http.resttempalte.RestTemplateConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Dtype.huang
 */
@Slf4j
@ComponentScan(basePackages = {"tech.finovy.*"})
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RefreshAutoConfiguration.class, RestTemplateConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestHttpTemplateService.class)
class TestHttpTemplateService {

    @Resource
    private HttpTemplateService httpTemplateService;

    @Test
    @DisplayName("TestHttpTemplateService")
    void httpTemplateServiceTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT_ENCODING, MediaType.ALL_VALUE);
        HttpEntity httpEntity = new HttpEntity<>("{}", headers);
        String api = "https://www.baidu.com";
        HttpTemplatePack httpTemplatePack = httpTemplateService.choice(api);
        ResponseEntity<String> response = httpTemplatePack.getRestTemplate().exchange(httpTemplatePack.getHost(), HttpMethod.GET, httpEntity, String.class);
        log.info("response:{} {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
    }

}
