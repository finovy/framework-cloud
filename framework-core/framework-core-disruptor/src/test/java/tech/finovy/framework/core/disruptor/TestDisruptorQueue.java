package tech.finovy.framework.core.disruptor;


import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.core.disruptor.spi.DisruptorEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Dtype.huang
 */
@Slf4j
@ComponentScan(basePackages = {"tech.finovy.*"})
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestDisruptorQueue.class)
class TestDisruptorQueue {

    private DisruptorEngine disruptorService = DisruptorEngineFactory.getDisruptorEngine();

    @Test
    @DisplayName("TestDisruptorQueue")
    void disruptorQueueTest() {
        StopWatch sw = new StopWatch();
        sw.start();
        int max = 10;
        log.info("Begin push event:{}", max);
        for (int x = 0; x < max; x++) {
            DisruptorEvent event = new DisruptorEvent();
            event.setDisruptorEvent(x);
            event.setEventId(x);
            event.setEventTags("testTag");
            event.setEventTopic("testTopic");
            event.setTransactionId(String.valueOf(x));
            event.setDisruptorEventType(TeatDisruptorEventConstant.TEST_EVENT_LISTENER_TYPE);
            disruptorService.post(event);
        }
        log.info("time:{}", sw.getTime());
    }

    @Test
    @DisplayName("TestExceptionDisruptorQueue")
    void exceptionDisruptorQueueTest() {
        StopWatch sw = new StopWatch();
        sw.start();
        int max = 3;
        log.info("Begin push event:{}", max);
        for (int x = 0; x < max; x++) {
            DisruptorEvent event = new DisruptorEvent();
            event.setDisruptorEvent(x);
            event.setEventId(x);
            event.setEventRetryCount(5);
            event.setEventTags("testTag");
            event.setEventTopic("testTopic");
            event.setTransactionId(String.valueOf(x));
            event.setDisruptorEventType("test-exception");
            disruptorService.post(event);
        }
        log.info("time:{}", sw.getTime());
//        try{
//        Thread.sleep(1000000);}
//        catch (InterruptedException e){
//
//        }
    }
}
