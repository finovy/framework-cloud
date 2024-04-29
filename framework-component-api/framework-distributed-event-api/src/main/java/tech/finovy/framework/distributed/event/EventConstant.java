package tech.finovy.framework.distributed.event;

public class EventConstant {
    private EventConstant() {
    }
    public static final String MESSAGE_QUEUE_COMPRESSOR ="message-compressor";
    public static final String MESSAGE_BODY_COMPRESSOR ="body-compressor";
    public static final String NOPROVIDER="No provider available";
    public static final String ASYNC_MOCK ="tech.finovy.framework.distributed.event.mock.AsyncEventServiceMockImpl";
    public static final String ASYNC_STUB ="tech.finovy.framework.distributed.event.stub.AsyncEventServiceStubImpl";

    public static final String MOCK ="tech.finovy.framework.distributed.event.mock.EventServiceMockImpl";
    public static final String STUB ="tech.finovy.framework.distributed.event.stub.EventServiceStubImpl";

    public static final String APPLICATION_EXCEPTION_TOPIC ="RDMS-EXCEPTION-TOPIC";
    public static final String APPLICATION_EVENT_TOPIC ="RDMS-EVENT-TOPIC";
    public static final String APPLICATION_ACCESS_TOPIC ="RDMS-ACCESS-TOPIC";
    public static final String APPLICATION_LOG_TOPIC ="RDMS-LOG-TOPIC";

    public static final String APPLICATION_EXCEPTION_TAG ="EXCEPTION-TAG";
    public static final String GATEWAY_EXCEPTION_TAG ="GATEWAY-EXCEPTION-TAG";

    public static final String APPLICATION_STARTUP_TAG ="STARTUP-TAG";
    public static final String APPLICATION_JOB_SATAUS_TRACE_TAG ="JOB-STATUS-TRACE-TAG";
    public static final String APPLICATION_JOB_EXECUTION_TAG ="JOB-EXECUTION-TAG";
    public static final String APPLICATION_JOB_EXECUTE_CONFIG_TAG ="JOB-EXECUTE-CONFIG-TAG";
    public static final String APPLICATION_JOB_EXECUTE_RESULT_TAG ="JOB-RESULT-TAG";


    public static final String APPLICATION_HEARTBEAT_TAG ="HEARTBEAT-TAG";
    public static final String GATEWAY_REQUEST_TAG ="REQUEST-TAG";
    public static final String GATEWAY_RESPONSE_TAG ="RESPONSE-TAG";

    public static final String DRUID_EVENT_TAG = "DRUID-EVENT-TAG";
}
