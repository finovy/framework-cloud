package tech.finovy.framework.http;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import tech.finovy.framework.http.resttempalte.HttpTemplatePack;
import tech.finovy.framework.http.resttempalte.HttpTemplateService;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RefreshAutoConfiguration.class, HttpTemplateAutoConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = HttpTemplateServiceTest.class)
public class HttpTemplateServiceTest {

    @Autowired
    private HttpTemplateService<RestTemplate> httpTemplateService;

    @Test
    @DisplayName("TestHttpTemplateService")
    void httpTemplateServiceTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("TX_ID", "123");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT_ENCODING, MediaType.ALL_VALUE);
        HttpEntity httpEntity = new HttpEntity<>("{}", headers);
        String api = "http://127.0.0.1:8848/nacos";
        HttpTemplatePack<RestTemplate> httpTemplatePack = httpTemplateService.choice(api);
        ResponseEntity<String> response = httpTemplatePack.getRestTemplate().exchange(httpTemplatePack.getHost(), HttpMethod.GET, httpEntity, String.class);
        log.info("response:{} {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
    }

}
