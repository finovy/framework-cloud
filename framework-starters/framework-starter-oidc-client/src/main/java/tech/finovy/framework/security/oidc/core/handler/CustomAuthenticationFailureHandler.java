package tech.finovy.framework.security.oidc.core.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import tech.finovy.framework.security.oidc.AuthorizationCallbackHandler;
import tech.finovy.framework.security.oidc.UserDetailService;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final AuthorizationCallbackHandler callbackHandler;
    private final UserDetailService userDetailService;
    public CustomAuthenticationFailureHandler(AuthorizationCallbackHandler callbackHandler, UserDetailService userDetailService) {
        this.callbackHandler = callbackHandler;
        this.userDetailService = userDetailService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) {
        if (callbackHandler == null){
            return;
        }
        callbackHandler.handleAuthorizationCallback(false, request ,response,userDetailService.getUserInfo());
    }

}
