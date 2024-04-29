package tech.finovy.framework.config.nacos.listener;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@RefreshScope
@EnableDiscoveryClient
@Configuration
public class CustomConfiguration {

    @Value("${spring.application.name:sharding-engine}")
    private String applicationName;
    @Value("${server.port:8080}")
    private String applicationPort;
    @Value("${sharding-engine.data-id.execution:sharding-engine-execution.json}")
    private String shardingExecutionDataId;
    @Value("${sharding-engine.data-id.default-instance:sharding-engine-default-instance.yaml}")
    private String shardingDefaultInstanceDataId;
    @Value("${sharding-engine.data-id.instance:sharding-engine-instance.yaml}")
    private String shardingInstanceDataId;
    @Value("${sharding-engine.data-group:DEFAULT_GROUP}")
    private String shardingDataGroup;
    @Value("${sharding-engine.distributed.enable:true}")
    private boolean enable;
    @Value("${sharding-engine.distributed.try-lock-timeout:20000}")
    private long tryLockTimeout;
    @Value("${sharding-engine.distributed.disable-self-check:false}")
    private boolean disableSelfCheck;
    @Value("${sharding-engine.distributed.self-check-count:25}")
    private int selfCheckCount = 25;
    @Value("${sharding-engine.distributed.provider:}")
    private String provider;
    @Value("${sharding-engine.distributed.shutdown-timeout:20000}")
    private long shutdownTimeout;
    @Value("${sharding-engine.distributed.init-after-startup:false}")
    private boolean initAfterStartup;
    @Value("${sharding-engine.distributed.debug.show-state-check:false}")
    private boolean showStateCheck;
    @Value("${sharding-engine.distributed.debug.show-node-filter:false}")
    private boolean showNodeFilter;
    @Value("${sharding-engine.distributed.debug.show-discovery-state:false}")
    private boolean showDiscoveryState;
    @Value("${sharding-engine.manager.debug:false}")
    private boolean debug;
    @Value("${sharding-engine.scheduled.initial-delay:30}")
    private int initialDelay;
    @Value("${sharding-engine.scheduled.period:10}")
    private int period;
    @Value("${sharding-engine.executor.pool-size:10}")
    private int corePoolSize;
    @Value("${sharding-engine.executor.max-pool-size:50}")
    private int maxPoolSize;
}
