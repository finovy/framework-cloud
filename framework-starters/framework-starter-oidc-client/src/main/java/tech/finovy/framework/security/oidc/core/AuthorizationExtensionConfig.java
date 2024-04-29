package tech.finovy.framework.security.oidc.core;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import javax.naming.ConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class AuthorizationExtensionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationExtensionConfig.class);

    private List<String> passPaths = new ArrayList<>();

    private boolean customResponseEnable = false;

    private String logoutUrl = "/logout";

    private String loginPage;

    private String deleteCookiesName = "JSESSIONID";

    private String defaultSuccessUrl = "/";

    private static final List<String> baseUri = new ArrayList<>();

    private final Map<String, ExtensionRegistration> registration = new HashMap<>(8);

    private String defaultAccountLoginUrl = "/login/account";

    private boolean jwtEnable = true;
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
    }

    public List<String> getPassPaths() {
        passPaths.addAll(baseUri);
        return passPaths;
    }
}
