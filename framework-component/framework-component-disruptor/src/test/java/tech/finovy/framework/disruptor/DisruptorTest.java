package tech.finovy.framework.disruptor;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.finovy.framework.common.core.loader.EnhancedServiceLoader;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConstant;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.disruptor.spi.ProcessInterface;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class DisruptorTest {

    private DisruptorEventConfiguration configuration = null;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        configuration = Mockito.mock(DisruptorEventConfiguration.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    private static DisruptorEngine INSTANCE = null;

    public DisruptorEngine getDisruptorEngine() {
        return getDisruptorEngine(configuration);
    }

    public DisruptorEngine getDisruptorEngine(DisruptorEventConfiguration configuration) {
        if (INSTANCE == null) {
            Class<?>[] arrType = {DisruptorEventConfiguration.class};
            Object[] args = {configuration};
            INSTANCE = EnhancedServiceLoader.load(DisruptorEngine.class, DisruptorConstant.DISRUPTOR, arrType, args);
        }
        return INSTANCE;
    }

    @Test
    public void testDisruptorEngine() {
        Assertions.assertFalse(configuration.isDebug());
        when(configuration.isDebug()).thenReturn(true);
        when(configuration.getRingBufferSize()).thenReturn(1024);
        when(configuration.getMaxAvailableProcessors()).thenReturn(24);
        // case 1: send a message
        final DisruptorEngine disruptorEngine = getDisruptorEngine();
        disruptorEngine.start();
        DisruptorEvent event = new DisruptorEvent();
        event.setDisruptorEvent(1);
        event.setEventId(1);
        event.setEventTags("shutdownRedisTag");
        event.setEventTopic("shutdownRedisTopic");
        event.setTransactionId(String.valueOf(1));
        event.setDisruptorEventType(DisruptorEventConstant.SYS_GLOBAL_EVENT_LISTENER_TYPE);
        disruptorEngine.post(event);
        event.setDisruptorEventType(DisruptorEventConstant.SYS_GLOBAL_CLEAR_LISTENER_TYPE);
        disruptorEngine.post(event);
    }

    @Test
    void testStrategy() {
        DisruptorEngine engine = new DisruptorEngine() {
            @Override
            public void post(DisruptorEvent event) {

            }

            @Override
            public void addProcessInterfaces(List<ProcessInterface> processInterfaces) {

            }

            @Override
            public void addProcessInterface(ProcessInterface processInterfaces) {

            }

            @Override
            public void start() {

            }

            @Override
            public void shutdown() {

            }
        };
        Assertions.assertTrue(engine.switchWaitStrategy(1) instanceof SleepingWaitStrategy);
        Assertions.assertTrue(engine.switchWaitStrategy(2) instanceof YieldingWaitStrategy);
    }
}
