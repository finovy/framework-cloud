package tech.finovy.framework.disruptor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ImportAutoConfiguration({RefreshAutoConfiguration.class, DisruptorEventAutoConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = DisruptorTest.class)
public class DisruptorTest {


    @Autowired
    private DisruptorEventConfiguration disruptorEventConfiguration;

    @Test
    void testDisruptor(){
        Assertions.assertEquals(24, disruptorEventConfiguration.getMaxAvailableProcessors()); ;
    }

}
