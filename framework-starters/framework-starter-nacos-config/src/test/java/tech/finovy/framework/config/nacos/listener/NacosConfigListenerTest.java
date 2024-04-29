package tech.finovy.framework.config.nacos.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.finovy.framework.nacos.entity.NacosCas;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NacosConfigListenerTest {

    @Mock
    private AtomicInteger mockVersion;

    private NacosConfigListener<String> nacosConfigListenerUnderTest;

    @BeforeEach
    void setUp() {
        AbstractNacosConfigDefinitionListener listener = new AbstractNacosConfigDefinitionListener<>(String.class,
                "dataId", "dataGroup", null) {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, String config, int version) {

            }
        };
        Map<String, NacosCas> mapValue = new HashMap<>();
        Map<String, NacosConfigDefinitionListener> map = new HashMap<>();
        map.put("test",listener);
        nacosConfigListenerUnderTest = new NacosConfigListener<>(
                mapValue, map, mockVersion);
    }

    @Test
    void testGetExecutor() {
        assertNull(nacosConfigListenerUnderTest.getExecutor());
    }

    @Test
    void testReceiveConfigInfo() {

        // Run the test
        nacosConfigListenerUnderTest.receiveConfigInfo("configInfo");

        // Verify the results
    }
}
