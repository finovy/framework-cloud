package tech.finovy.framework.http.resttempalte;

import lombok.Data;

@Data
public class RestTemplateProperties {

    public static final String PREFIX = "rest-template";

    private int connectTimeout = 60 * 1000;

    private int readTimeout = 20 * 1000;

    private boolean enableHttp;

}
