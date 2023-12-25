package tech.finovy.framework.transaction.tcc.client.fence;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import tech.finovy.framework.common.core.exception.FrameworkErrorCode;
import tech.finovy.framework.transaction.tcc.client.constant.Constants;
import tech.finovy.framework.transaction.tcc.client.exception.TCCFenceException;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCCFenceConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCCFenceConfig.class);
    /**
     * TCC fence clean period max value. maximum interval is 68 years
     */
    private static final Duration MAX_PERIOD = Duration.ofSeconds(Integer.MAX_VALUE);
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    /**
     * TCC fence datasource
     */
    private final DataSource dataSource;
    /**
     * TCC fence transactionManager
     */
    private final PlatformTransactionManager transactionManager;
    /**
     * TCC fence clean scheduled thread pool
     */
    private final ScheduledThreadPoolExecutor tccFenceClean = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern(String.join("-", "tccFenceClean-", "%s")).build());
    /**
     * TCC fence clean period. only duration type format are supported
     */
    private Duration cleanPeriod = Duration.ofDays(1);
    /**
     * TCC fence log table name
     */
    private String logTableName = Constants.DEFAULT_TCC_FENCE_LOG_TABLE_NAME;

    public TCCFenceConfig(final DataSource dataSource, final PlatformTransactionManager transactionManager) {
        this.dataSource = dataSource;
        this.transactionManager = transactionManager;
    }

    public AtomicBoolean getInitialized() {
        return initialized;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setCleanPeriod(Duration cleanPeriod) {
        this.cleanPeriod = cleanPeriod;
    }

    public void setLogTableName(String logTableName) {
        this.logTableName = logTableName;
    }

    /**
     * init tcc fence clean task
     */
    public void initCleanTask() {
        try {
            // convert to second level. maximum interval is 68 years
            long periodSeconds = cleanPeriod.compareTo(MAX_PERIOD) >= 0 ? Integer.MAX_VALUE : cleanPeriod.toMillis() / 1000;
            // start tcc fence clean schedule
            tccFenceClean.scheduleWithFixedDelay(() -> {
                Date timeBefore = null;
                try {
                    timeBefore = DateUtils.addSeconds(new Date(), -(int) periodSeconds);
                    int deletedRowCount = TCCFenceHandler.deleteFenceByDate(timeBefore);
                    if (deletedRowCount > 0) {
                        LOGGER.info("TCC fence clean task executed success, timeBefore: {}, deleted row count: {}",
                                timeBefore, deletedRowCount);
                    }
                } catch (RuntimeException e) {
                    LOGGER.error("Delete tcc fence log failed, timeBefore: {}", timeBefore, e);
                }
            }, 0, periodSeconds, TimeUnit.SECONDS);
            LOGGER.info("TCC fence log clean task start success, cleanPeriod:{}", cleanPeriod);
        } catch (NumberFormatException e) {
            LOGGER.error("TCC fence log clean period only supports positive integers, clean task start failed");
        }
    }

    @Override
    public void afterPropertiesSet() {
        // set log table name
        if (logTableName != null) {
            TCCFenceStoreDataBaseDAO.getInstance().setLogTableName(logTableName);
        }
        if (dataSource != null) {
            // set dataSource
            TCCFenceHandler.setDataSource(dataSource);
        } else {
            throw new TCCFenceException(FrameworkErrorCode.DateSourceNeedInjected);
        }
        if (transactionManager != null) {
            // set transaction template
            TCCFenceHandler.setTransactionTemplate(new TransactionTemplate(transactionManager));
        } else {
            throw new TCCFenceException(FrameworkErrorCode.TransactionManagerNeedInjected);
        }
    }
}

