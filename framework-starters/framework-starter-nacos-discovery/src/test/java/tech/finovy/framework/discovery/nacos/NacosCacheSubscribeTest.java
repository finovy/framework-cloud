package tech.finovy.framework.discovery.nacos;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NacosCacheSubscribeTest {

    @Mock
    private CacheManager mockDefaultLoadBalancerCacheManager;

    private NacosCacheSubscribe nacosCacheSubscribeUnderTest;

    @BeforeEach
    void setUp() {
        nacosCacheSubscribeUnderTest = new NacosCacheSubscribe(mockDefaultLoadBalancerCacheManager);
    }

    @Test
    void testOnEvent() {
        // Setup
        final Instance instance = new Instance();
        instance.setInstanceId("instanceId");
        instance.setIp("ip");
        instance.setPort(0);
        instance.setWeight(0.0);
        instance.setHealthy(false);
        instance.setClusterName("clusterName");
        instance.setServiceName("serviceName");
        instance.setMetadata(Map.ofEntries(Map.entry("value", "value")));
        instance.setEnabled(false);
        instance.setEphemeral(false);
        final InstancesChangeEvent instancesChangeEvent = new InstancesChangeEvent("eventScope", "serviceName",
                "groupName", "clusters", List.of(instance));
        when(mockDefaultLoadBalancerCacheManager.getCache(CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME)).thenReturn(null);
        when(mockDefaultLoadBalancerCacheManager.getCacheNames()).thenReturn(List.of("value"));
        // Run the test
        nacosCacheSubscribeUnderTest.onEvent(instancesChangeEvent);
        // Verify the results
    }


}
