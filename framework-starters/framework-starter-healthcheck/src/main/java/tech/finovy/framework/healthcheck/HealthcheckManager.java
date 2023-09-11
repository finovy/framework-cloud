package tech.finovy.framework.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@Component
public class HealthcheckManager {
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    private final HealthcheckNet healthcheckNet;
    public HealthcheckManager(HealthcheckNet healthcheckNet) {
        this.healthcheckNet = healthcheckNet;
    }

    @Bean
    @Primary
    public CommandLineRunner shardingEngineConfigurationHealthcheckCommandLineRunner() {
        return strings -> {
            try {
                threadPool.execute(() -> healthcheckNet.monitor("up"));
            } catch (Exception e) {
                log.error(e.toString());
            }
        };
    }
}
