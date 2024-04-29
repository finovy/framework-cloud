package tech.finovy.framework.ratelimiter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RateLimiterFactoryAutoConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = RatelimiterTest.class)
public class RatelimiterTest {

    @Autowired
    private DistributedRateLimiterFactoryManager manager;

    @Test
    public void ratelimiterTest(){
        for (int i = 0; i < 5; i++) {
            final boolean test = manager.tryAcquire("TEST", 1);
            if (i == 0) {
                Assertions.assertTrue(test);
                continue;
            }
            Assertions.assertFalse(test);
        }
    }
}
