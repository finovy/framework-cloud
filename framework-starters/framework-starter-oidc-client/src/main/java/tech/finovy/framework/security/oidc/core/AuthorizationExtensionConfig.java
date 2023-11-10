package tech.finovy.framework.security.oidc.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import javax.naming.ConfigurationException;
import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class AuthorizationExtensionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationExtensionConfig.class);

    private List<String> passPaths;

    private String loginPage;

    private String logoutUrl = "/logout";

    private String deleteCookiesName = "JSESSIONID";

    private String defaultSuccessUrl = "/";

    private static final List<String> baseUri = new ArrayList<>();

    static {
        baseUri.add("/error");
    }

    public List<String> getPassPaths() throws ConfigurationException {
        passPaths.addAll(baseUri);
        passPaths.add(loginPage);
        return passPaths;
    }

    public void setPassPaths(List<String> passPaths) {
        this.passPaths = passPaths;
    }

    public String getLoginPage() throws ConfigurationException {
        if (!StringUtils.hasLength(loginPage)){
            throw new ConfigurationException("login page must be configure!");
        }
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getDeleteCookiesName() {
        return deleteCookiesName;
    }

    public void setDeleteCookiesName(String deleteCookiesName) {
        this.deleteCookiesName = deleteCookiesName;
    }

    public String getDefaultSuccessUrl() {
        return defaultSuccessUrl;
    }

    public void setDefaultSuccessUrl(String defaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
    }
}
