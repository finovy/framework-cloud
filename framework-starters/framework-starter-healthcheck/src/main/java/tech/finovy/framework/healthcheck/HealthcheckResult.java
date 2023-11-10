package tech.finovy.framework.healthcheck;

import lombok.Data;

@Data
public class HealthcheckResult {

    boolean readiness;
    boolean success;
    int statusCode = 200;
    String message;
    private String tag;

    public HealthcheckResult(boolean success, int statusCode) {
        this.success = success;
        this.statusCode = statusCode;
    }

    public HealthcheckResult(boolean success, int statusCode, String message) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
    }

    public HealthcheckResult() {
    }
}
