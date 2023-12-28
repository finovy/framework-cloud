package tech.finovy.framework.security.oidc.core.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import tech.finovy.framework.security.oidc.extend.AuthorizationCallbackHandler;
import tech.finovy.framework.security.oidc.extend.UserDetailService;
import tech.finovy.framework.security.oidc.util.ResponseUtil;
import tech.finovy.framework.security.oidc.util.RestBody;

import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final AuthorizationCallbackHandler callbackHandler;
    private final UserDetailService userDetailService;
    public CustomAuthenticationFailureHandler(AuthorizationCallbackHandler callbackHandler, UserDetailService userDetailService) {
        this.callbackHandler = callbackHandler;
        this.userDetailService = userDetailService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) throws IOException {
        ResponseUtil.responseJsonWriter(response, RestBody.failure(401, exception.getMessage()));
        if (callbackHandler == null){
            return;
        }
        callbackHandler.handleAuthorizationCallback(false, request ,response,userDetailService.getUserInfo());
    }

}
