package tech.finovy.framework.disruptor.provider;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.exception.DisruptorException;
import tech.finovy.framework.disruptor.spi.ProcessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DefaultDisruptorEngineProviderTest {

    @Mock
    private DisruptorEventConfiguration mockConfiguration;

    private DefaultDisruptorEngineProvider defaultDisruptorEngineProviderUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        when(mockConfiguration.getRingBufferSize()).thenReturn(1024);
        when(mockConfiguration.getMaxAvailableProcessors()).thenReturn(4);
        defaultDisruptorEngineProviderUnderTest = new DefaultDisruptorEngineProvider(mockConfiguration);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }


    @Test
    void testShutdown() {
        defaultDisruptorEngineProviderUnderTest.shutdown();
    }

    @Test
    void testPost() {
        // Setup
        final DisruptorEvent<String> event = new DisruptorEvent<>();
        event.setExecuteListener(Set.of("value"));
        event.setEventRetryCount(0);
        event.setApplication("application");
        event.setEventTags("eventTags");
        event.setEventTopic("eventTopic");
        event.setEventId(0L);
        event.setTransactionId("transactionId");
        when(mockConfiguration.getApplicationName()).thenReturn("application");
        when(mockConfiguration.getRetryCount()).thenReturn(0);
        // error branch
        Assertions.assertThrows(DisruptorException.class, () -> defaultDisruptorEngineProviderUnderTest.post(null));
        event.setDisruptorEventType(null);
        Assertions.assertThrows(DisruptorException.class, () -> defaultDisruptorEngineProviderUnderTest.post(event));
        event.setDisruptorEvent(null);
        Assertions.assertThrows(DisruptorException.class, () -> defaultDisruptorEngineProviderUnderTest.post(event));
    }

    @Test
    void testAddProcessInterfaces() {
        // Setup
        final List<ProcessInterface> processInterfaces = new ArrayList<>();
        final ProcessInterfaceBImpl processInterfaceB = new ProcessInterfaceBImpl();
        final ProcessInterfaceAImpl processInterfaceA = new ProcessInterfaceAImpl();
        final ProcessInterfaceAImpl processInterfaceA1 = new ProcessInterfaceAImpl();
        processInterfaces.add(processInterfaceA1);
        processInterfaces.add(processInterfaceA);
        processInterfaces.add(processInterfaceB);
        // Run the test
        defaultDisruptorEngineProviderUnderTest.addProcessInterfaces(processInterfaces);
        defaultDisruptorEngineProviderUnderTest.addProcessInterfaces(processInterfaces);
        // Verify the results
        defaultDisruptorEngineProviderUnderTest.addProcessInterface(processInterfaceA);
        Assertions.assertNull(new ProcessInterfaceCImpl().getType());
    }

}
