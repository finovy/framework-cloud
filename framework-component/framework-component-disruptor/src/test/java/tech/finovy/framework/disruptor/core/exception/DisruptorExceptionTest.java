package tech.finovy.framework.disruptor.core.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.openMocks;

class DisruptorExceptionTest {

    @Mock
    private Throwable mockCause;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    public void testException(){
        DisruptorException A = new DisruptorException(mockCause);
        DisruptorException B = new DisruptorException("test");
        Assertions.assertNotNull(A);
        Assertions.assertNotNull(B);
    }
}
