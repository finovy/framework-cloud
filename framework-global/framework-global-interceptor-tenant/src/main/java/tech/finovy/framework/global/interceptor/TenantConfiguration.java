package tech.finovy.framework.global.interceptor;

import tech.finovy.framework.global.Constant;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
public class TenantConfiguration {

    public static final String PREFIX = "trace";

    private String traceIdKey = Constant.X_TRACEID;
    private String appIdKey = Constant.TOKEN_HEADER_APPID;
    private boolean enable = true;
    private boolean traceDebug;

}
