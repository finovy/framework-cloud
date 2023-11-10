package tech.finovy.framework.scheduler.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tech.finovy.framework.listener.DistributedListenerFactoryManager;
import tech.finovy.framework.scheduler.listener.SchedulerExecutorListener;
import tech.finovy.framework.scheduler.listener.SchedulerExecutorNotExistsListener;
import tech.finovy.framework.scheduler.listener.SchedulerFlowListener;
import tech.finovy.framework.scheduler.listener.SchedulerFlowNotExistsListener;

import java.util.Map;

@EnableConfigurationProperties(JobExecutorControllerProperties.class)
@Configuration(proxyBeanMethods = false)
public class SchedulerClientAutoConfiguration {

    @Primary
    @Bean
    public Map<String, Map<String, SchedulerExecutorListener>> multiExecutorMonitorListenerSort(Map<String, SchedulerExecutorListener> schedulerListeners) {
        return DistributedListenerFactoryManager.multiDistributedListenerSort(schedulerListeners);
    }

    @Bean
    public SchedulerExecutorListener schedulerExecutorListener() {
        return new SchedulerExecutorNotExistsListener();
    }

    @Bean
    public <T> SchedulerFlowListener<T> schedulerFlowListener() {
        return new SchedulerFlowNotExistsListener<>();
    }

    @Bean
    public <T> SchedulerExecutorService<T> schedulerExecutorService(Map<String, Map<String, SchedulerExecutorListener>> listeners, Map<String, Map<String, SchedulerFlowListener<T>>> flowListeners) {
        return new SchedulerExecutorServiceImpl<>(listeners, flowListeners);
    }

    @Primary
    @Bean
    public <T> Map<String, Map<String, SchedulerFlowListener<T>>> multiSchedulerFlowListenerSort(Map<String, SchedulerFlowListener<T>> schedulerFlowListener) {
        return DistributedListenerFactoryManager.multiDistributedListenerSort(schedulerFlowListener);
    }

    @Bean
    public <T> JobExecutorTriggerController<T> jobExecutorTriggerController(JobExecutorControllerProperties properties, SchedulerExecutorService<T> service) {
        return new JobExecutorTriggerController<>(properties, service);
    }
}
