package tech.finovy.framework.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.distributed.event.EventConstant;

import static org.junit.jupiter.api.Assertions.*;

class EventPropertiesTest {

    private EventProperties eventPropertiesUnderTest;

    @BeforeEach
    void setUp() {
        eventPropertiesUnderTest = new EventProperties();
    }

    @Test
    void testGet() {
        assertFalse(eventPropertiesUnderTest.isDebug());
        assertEquals(5, eventPropertiesUnderTest.getLogRateLimiter());
        assertEquals(EventConstant.APPLICATION_ACCESS_TOPIC, eventPropertiesUnderTest.getAccessEventTopic());
        assertEquals(EventConstant.GATEWAY_REQUEST_TAG, eventPropertiesUnderTest.getAccessEventTagRequest());
        assertEquals(EventConstant.GATEWAY_RESPONSE_TAG, eventPropertiesUnderTest.getAccessEventTagResponse());
        assertEquals(EventConstant.APPLICATION_EVENT_TOPIC,eventPropertiesUnderTest.getApplicationEventTopic());
        assertEquals(EventConstant.APPLICATION_STARTUP_TAG,eventPropertiesUnderTest.getStartupTag());
        assertEquals(EventConstant.APPLICATION_EXCEPTION_TAG,eventPropertiesUnderTest.getExceptionTag());
    }

}
