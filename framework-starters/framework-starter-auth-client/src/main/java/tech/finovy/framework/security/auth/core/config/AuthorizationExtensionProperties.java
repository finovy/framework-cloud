package tech.finovy.framework.security.auth.core.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class AuthorizationExtensionProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationExtensionProperties.class);

    private List<String> passPaths = new ArrayList<>();

    private String logoutUrl = "/logout";

    private String defaultSuccessUrl = "/";

    private static final List<String> baseUri = new ArrayList<>();

    private final Map<String, ExtensionRegistration> registration = new HashMap<>(8);

    private String defaultAccountLoginUrl = "/login/account";
    private String loginProcessingUrl = "/login/auth/code";

    private boolean debugEnable = false;
    private boolean jwtEnable = true;
    private boolean stateEnable = false;
    private boolean codeModeEnable = false;
    private String codeModeUsername = "developer";
    private String tokenHeader = "Authorization";
    private String tokenParameter = "token";

    @Data
    public static class ExtensionRegistration {
        private String host;
        private String realm;
    }

    static {
        baseUri.add("/error");
        baseUri.add("/healthcheck/liveness");
        baseUri.add("/healthcheck/readiness");
    }
    public List<String> getPassPaths() {

        passPaths.addAll(baseUri);
        return passPaths;
    }

}
