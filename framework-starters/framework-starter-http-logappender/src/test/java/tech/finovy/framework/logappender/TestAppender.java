package tech.finovy.framework.logappender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import tech.finovy.framework.logappender.conf.ProducerConfig;
import tech.finovy.framework.logappender.exception.LogException;
import tech.finovy.framework.logappender.utils.LZ4Encoder;
import org.junit.AfterClass;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestAppender {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestAppender.class);

    private static void sleep() {
        ProducerConfig producerConfig = new ProducerConfig();
        try {
            Thread.sleep(2L * producerConfig.getLingerMs());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void checkStatusList() {
        sleep();
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusManager statusManager = lc.getStatusManager();
        List<Status> statusList = statusManager.getCopyOfStatusList();
        for (Status status : statusList) {
            int level = status.getLevel();
//            assertNotEquals(status.getMessage(), Status.ERROR, level);
//            assertNotEquals(status.getMessage(), Status.WARN, level);
        }
    }

    @Test
    public void testLogCommonMessage() {
        MDC.put("MDC_KEY", "MDC_VALUE");
        MDC.put("THREAD_ID", String.valueOf(Thread.currentThread().getId()));
        for (int i = 0; i < 1; i++) {
            LOGGER.warn("This is a test common message logged by logback.");
        }
    }

    @Test
    public void testLogThrowable() {
        for (int x = 0; x < 1; x++) {
            MDC.put("appid", "appid0000000000000000000000000");
            MDC.put("traceid", "traceid00000000000000000000000000000");
            MDC.put("MDC_KEY", "MDC_VALUE");
            MDC.put("THREAD_ID", String.valueOf(Thread.currentThread().getId()));
            LOGGER.error("This is a test error message logged by logback.",
                    new UnsupportedOperationException("Logback UnsupportedOperationException"));
        }
    }


    @Test
    public void testLz4() throws LogException {
        String source = "test lz4 message";
        byte[] sourceByte = source.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = LZ4Encoder.compressToLhLz4Chunk(sourceByte);
        byte[] dec = LZ4Encoder.decompressFromLhLz4Chunk(bytes, sourceByte.length);
    }
}
