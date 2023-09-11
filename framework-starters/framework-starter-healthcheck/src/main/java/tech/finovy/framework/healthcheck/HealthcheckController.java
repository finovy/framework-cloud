package tech.finovy.framework.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthcheckController implements EnvironmentAware {
    private Environment environment;
    private boolean liveness = false;
    private boolean readiness = false;
    @Autowired
    private HealthcheckNet healthcheckNet;


    @GetMapping(value = "/healthcheck/liveness")
    public HealthcheckResult liveness() {
        HealthcheckResult healthcheckResult = new HealthcheckResult(true, 200);
        String tag = environment.getProperty("APPLICATION_TAG");
        healthcheckResult.setTag(tag);
        if (!liveness) {
            log.info("Healthcheck liveness........................");
            liveness = true;
        }
        healthcheckNet.liveness();
        return healthcheckResult;
    }

    @GetMapping(value = {"/healthcheck", "/healthcheck/readiness"})
    public HealthcheckResult readiness() {
        HealthcheckResult healthcheckResult = new HealthcheckResult(true, 200);
        String tag = environment.getProperty("APPLICATION_TAG");
        healthcheckResult.setTag(tag);
        if (!readiness) {
            log.info("Healthcheck readiness........................");
            readiness = true;
        }
        healthcheckNet.readiness();
        return healthcheckResult;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


}
