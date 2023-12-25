package tech.finovy.framework.transaction.tcc.client.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.transaction.tcc.client.context.TccClientContext;

import java.util.Arrays;

public class Log {
    private static final Logger log = LoggerFactory.getLogger(Log.class);

    public static void debug(String template, Object... params) {
        if (log.isDebugEnabled()) {
            log.debug(addCommonInfo(template), Arrays.stream(params).toArray());
        }
    }

    public static void info(String template, Object... params) {
        if (log.isInfoEnabled()) {
            log.info(addCommonInfo(template), Arrays.stream(params).toArray());
        }
    }

    public static void warn(String template, Object... params) {
        log.warn(addCommonInfo(template), Arrays.stream(params).toArray());
    }

    public static void error(String template, Object... params) {
        log.error(addCommonInfo(template), Arrays.stream(params).toArray());
    }

    private static String addCommonInfo(String template) {
        String txId = TccClientContext.getTxId() == null ? "sys" : TccClientContext.getTxId();
        return String.format("[tcc-client(txId:%s)] %s", txId, template);
    }
}
