package tech.finovy.framework.security.oidc.core.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import tech.finovy.framework.security.oidc.AuthorizationCallbackHandler;
import tech.finovy.framework.security.oidc.UserDetailService;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final AuthorizationCallbackHandler callbackHandler;
    private final UserDetailService userDetailService;

    public CustomAuthenticationSuccessHandler(AuthorizationCallbackHandler callbackHandler, UserDetailService userDetailService) {
        this.callbackHandler = callbackHandler;
        this.userDetailService = userDetailService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) {
        try {
            super.onAuthenticationSuccess(request, response, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (callbackHandler == null) {
            return;
        }
        callbackHandler.handleAuthorizationCallback(true, request, response, userDetailService.getUserInfo());
    }


}
