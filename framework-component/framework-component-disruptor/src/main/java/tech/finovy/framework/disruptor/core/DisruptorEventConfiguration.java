package tech.finovy.framework.disruptor.core;

import lombok.Data;

@Data
public class DisruptorEventConfiguration {

    public static final String PREFIX = "disruptor";

    private int ringBufferSize = 1024;
    private boolean debug;
    private int retryCount = 1;
    private int waitStrategy = 0;
    private String applicationName = "DisruptorEvent";
    private String name = "DisruptorEvent";
    private int maxAvailableProcessors = 24;

}
