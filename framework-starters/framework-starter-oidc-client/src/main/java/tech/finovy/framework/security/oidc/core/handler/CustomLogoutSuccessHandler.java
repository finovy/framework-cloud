package tech.finovy.framework.security.oidc.core.handler;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import tech.finovy.framework.security.oidc.core.config.AuthorizationExtensionProperties;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtTokenCacheStorage;
import tech.finovy.framework.security.oidc.core.token.normal.TokenManager;
import tech.finovy.framework.security.oidc.util.RequestUtil;
import tech.finovy.framework.security.oidc.util.ResponseUtil;
import tech.finovy.framework.security.oidc.util.RestBody;

import java.io.IOException;

@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final AuthorizationExtensionProperties properties;
    private final TokenManager tokenManager;
    private final JwtTokenCacheStorage jwtTokenCacheStorage;
    public CustomLogoutSuccessHandler(AuthorizationExtensionProperties properties,@Nullable JwtTokenCacheStorage jwtTokenCacheStorage, @Nullable TokenManager tokenManager) {
        this.properties = properties;
        this.tokenManager = tokenManager;
        this.jwtTokenCacheStorage = jwtTokenCacheStorage;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        if (properties.isJwtEnable()) {
            jwtTokenCacheStorage.expire(username);
        } else {
            String token = RequestUtil.obtainAuthorization(request, properties.getTokenHeader(), properties.getTokenParameter());
            tokenManager.expire(token);
        }
        log.info("username: {}  is offline now", username);
        ResponseUtil.responseJsonWriter(response, RestBody.ok("logout success"));
    }

}
