package tech.finovy.framework.event;

import lombok.Data;
import tech.finovy.framework.distributed.event.EventConstant;

@Data
//@ConditionalOnProperty(name = "rocketmq.nameserver")
public class EventProperties {

    public static final String PREFIX = "event";
    private boolean debug;
    //    @Value("${event.log.rateLimiter:5}")
    private double logRateLimiter = 5;
    //    @Value("${event.access-event-topic:"+ EventConstant.APPLICATION_ACCESS_TOPIC+"}")
    private String accessEventTopic = EventConstant.APPLICATION_ACCESS_TOPIC;
    //    @Value("${event.access-event-tag-request:"+ EventConstant.GATEWAY_REQUEST_TAG+"}")
    private String accessEventTagRequest = EventConstant.GATEWAY_REQUEST_TAG;
    //    @Value("${event.access-event-tag-response:"+ EventConstant.GATEWAY_RESPONSE_TAG+"}")
    private String accessEventTagResponse = EventConstant.GATEWAY_RESPONSE_TAG;
    //    @Value("${event.application-event-topic:" + EventConstant.APPLICATION_EVENT_TOPIC + "}")
    private String applicationEventTopic = EventConstant.APPLICATION_EVENT_TOPIC;
    //    @Value("${event.application-event-tag-request:" + EventConstant.APPLICATION_STARTUP_TAG + "}")
    private String startupTag = EventConstant.APPLICATION_STARTUP_TAG;
    //    @Value("${event.application-event-tag-exception:" + EventConstant.APPLICATION_EXCEPTION_TAG + "}")
    private String exceptionTag = EventConstant.APPLICATION_EXCEPTION_TAG;

}
