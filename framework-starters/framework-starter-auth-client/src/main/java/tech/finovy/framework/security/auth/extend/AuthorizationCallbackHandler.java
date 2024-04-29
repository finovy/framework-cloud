package tech.finovy.framework.security.auth.extend;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

/**
 * If you intend to perform actions following authorization, proceed with implementing this.
 */
public interface AuthorizationCallbackHandler {
    void handleAuthorizationCallback(boolean success, HttpServletRequest request, HttpServletResponse response, OidcUserInfo userInfo);
}

