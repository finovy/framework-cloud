package tech.finovy.framework.core.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdWorkerTest {

    @Test
    void testNegativeWorkerId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new IdWorker(-1L);
        }, "should throw IllegalArgumentException when workerId is negative");
    }

    @Test
    void testTooLargeWorkerId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new IdWorker(1024L);
        }, "should throw IllegalArgumentException when workerId is bigger than 1023");
    }

    @Test
    void testNextId() {
        IdWorker worker = new IdWorker(null);
        long id1 = worker.nextId();
        long id2 = worker.nextId();
        assertEquals(1L, id2 - id1, "increment step should be 1");
    }
}
