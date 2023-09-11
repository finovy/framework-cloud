package tech.finovy.framework.core.common.chain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

/**
 * @author Dtype.huang
 */
@Slf4j
@ComponentScan(basePackages = {"tech.finovy.*"})
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestDistributedListenerApplication.class)
class TestDistributedListenerApplication {
    @Autowired
    Map<String, Map<String, TestListenerInterface>> listeners;


    @Test
    @DisplayName("TestDisruptorQueue")
    void disruptorQueueTest() {
        boolean success = false;
        for (Map.Entry<String, Map<String, TestListenerInterface>> each : listeners.entrySet()) {
            for (Map.Entry<String, TestListenerInterface> entry : each.getValue().entrySet()) {
                log.info("Init {},{},{}", each.getKey(), entry.getKey(), entry.getValue().isEnable());
                success = true;
            }
        }
        Assertions.assertTrue(success);
    }

}
