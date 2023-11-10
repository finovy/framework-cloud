package tech.finovy.framework.disruptor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;

class DisruptorEngineFactoryTest {

    @Test
    void testGetDisruptorEngine() {
        // Setup
        // Run the test
        final DisruptorEngine result = DisruptorEngineFactory.getDisruptorEngine();
        // Verify the results
        Assertions.assertNotNull(result);
    }

}
