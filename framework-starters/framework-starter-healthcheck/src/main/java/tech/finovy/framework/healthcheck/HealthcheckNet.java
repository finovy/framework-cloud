package tech.finovy.framework.healthcheck;

import jakarta.annotation.PreDestroy;
import kong.unirest.Callback;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@Component
public class HealthcheckNet {
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    private ConfigurableApplicationContext applicationContext;
    private HealthcheckConfiguration healthcheckConfiguration;
    private String monitorAddr ;
    private String applicationTag;
    private String applicationKey;
    private String hostname;

    public HealthcheckNet(ConfigurableApplicationContext applicationContext, HealthcheckConfiguration healthcheckConfiguration) {
        this.applicationContext = applicationContext;
        this.healthcheckConfiguration = healthcheckConfiguration;
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> evnMap = environment.getSystemEnvironment();
        monitorAddr = Optional.ofNullable(evnMap.get("MONITOR_ADDR")).orElse("").toString();
        applicationTag = Optional.ofNullable(evnMap.get("APPLICATION_TAG")).orElse("").toString();
        applicationKey = Optional.ofNullable(evnMap.get("APPLICATION_KEY")).orElse("").toString();
        hostname = Optional.ofNullable(evnMap.get("HOSTNAME")).orElse("").toString();
    }

    public void readiness() {
        monitor("readiness");
    }

    public void liveness() {
        monitor("liveness");
    }

    public void monitor(String stage) {
        if (StringUtils.isBlank(monitorAddr)) {
            log.info("MONITOR SKIP,MONITOR_ADDR NOT Config....................");
            return;
        }
        if (StringUtils.isBlank(applicationTag)) {
            log.info("MONITOR SKIP,APPLICATION_TAG NOT Config....................");
            return;
        }
        if (StringUtils.isBlank(applicationKey)) {
            log.info("MONITOR SKIP,APPLICATION_KEY NOT Config....................");
            return;
        }
        if (StringUtils.isBlank(hostname)) {
            log.info("MONITOR SKIP,HOSTNAME NOT Config....................");
            return;
        }
        Unirest.config().verifySsl(false);
        String http = String.join("/", monitorAddr, stage, hostname, applicationKey, applicationTag);
        try {
            Unirest
                    .get(http)
                    .connectTimeout(100)
                    .asStringAsync(new Callback<>() {
                        @Override
                        public void completed(HttpResponse response) {
                            if (healthcheckConfiguration.isDebug()) {
                                log.info("http:{}", response.getStatus());
                            }
                        }

                        @Override
                        public void failed(UnirestException e) {
                            log.warn("http:{}", e.getMessage());
                        }
                        @Override
                        public void cancelled() {
                            Callback.super.cancelled();
                        }
                    });
        } catch (Exception e) {
            log.warn(e.toString());
        }
    }

    @PreDestroy
    public void destroy() {
        Unirest.shutDown();
    }
}
