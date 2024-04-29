package tech.finovy.framework.security.oidc.core.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import tech.finovy.framework.security.oidc.AuthorizationCallbackHandler;
import tech.finovy.framework.security.oidc.OidcClientAutoConfiguration;
import tech.finovy.framework.security.oidc.UserDetailService;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    private final AuthorizationCallbackHandler callbackHandler;
    private final UserDetailService userDetailService;
    public CustomAuthenticationFailureHandler(AuthorizationCallbackHandler callbackHandler, UserDetailService userDetailService) {
        this.callbackHandler = callbackHandler;
        this.userDetailService = userDetailService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) {
        LOGGER.warn("CustomAuthenticationFailureHandler exception:{}", exception.getMessage(), exception);
        if (callbackHandler == null){
            return;
        }
        callbackHandler.handleAuthorizationCallback(false, request ,response,null);
    }

}
