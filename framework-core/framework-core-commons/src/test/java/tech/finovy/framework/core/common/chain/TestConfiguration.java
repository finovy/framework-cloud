package tech.finovy.framework.core.common.chain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@Configuration
public class TestConfiguration {
    @Primary
    @Bean
    public Map<String, Map<String, TestListenerInterface>> createTestListenerInterfaces(Map<String, TestListenerInterface> filters) {
        return ChainSortUtil.multiChainListenerSort(filters);
    }

}
