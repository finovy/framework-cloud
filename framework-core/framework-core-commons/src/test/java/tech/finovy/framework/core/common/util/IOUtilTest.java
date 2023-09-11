package tech.finovy.framework.core.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IOUtilTest {

    @Test
    public void testCloseWithSingleParameter() {
        FakeResource resource = new FakeResource();

        IOUtil.close(resource);

        Assertions.assertTrue(resource.isClose());
    }

    @Test
    public void testCloseWithArrayParameter() {
        FakeResource resource1 = new FakeResource();
        FakeResource resource2 = new FakeResource();

        IOUtil.close(resource1, resource2);

        Assertions.assertTrue(resource1.isClose());
        Assertions.assertTrue(resource2.isClose());
    }

    @Test
    public void testIgnoreExceptionOnClose() {
        FakeResource resource = new FakeResource() {
            @Override
            public void close() throws Exception {
                super.close();
                throw new Exception("Ops!");
            }
        };

        IOUtil.close(resource);

        Assertions.assertTrue(resource.isClose());
    }

    private class FakeResource implements AutoCloseable {
        private boolean close = false;

        @Override
        public void close() throws Exception {
            this.close = true;
        }

        public boolean isClose() {
            return close;
        }
    }
}
