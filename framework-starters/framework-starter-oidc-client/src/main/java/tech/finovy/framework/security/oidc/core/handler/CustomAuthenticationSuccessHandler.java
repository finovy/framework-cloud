package tech.finovy.framework.security.oidc.core.handler;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import tech.finovy.framework.security.oidc.core.config.AuthorizationExtensionProperties;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtTokenGenerator;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtTokenPair;
import tech.finovy.framework.security.oidc.core.token.normal.TokenManager;
import tech.finovy.framework.security.oidc.extend.AuthorizationCallbackHandler;
import tech.finovy.framework.security.oidc.extend.UserDetailService;
import tech.finovy.framework.security.oidc.util.ResponseUtil;
import tech.finovy.framework.security.oidc.util.RestBody;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private final JwtTokenGenerator jwtTokenGenerator;
    private final AuthorizationCallbackHandler callbackHandler;
    private final UserDetailService userDetailService;
    private final AuthorizationExtensionProperties properties;
    private final TokenManager tokenManager;

    public CustomAuthenticationSuccessHandler(@Nullable TokenManager tokenManager, @Nullable JwtTokenGenerator jwtTokenGenerator, @Nullable AuthorizationCallbackHandler callbackHandler, UserDetailService userDetailService, AuthorizationExtensionProperties properties) {
        this.tokenManager = tokenManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.callbackHandler = callbackHandler;
        this.userDetailService = userDetailService;
        this.properties = properties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>(5);
        String username = "";
        if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
            username = principal.getUserInfo().getEmail();
        }
        if (authentication.getPrincipal() instanceof User) {
            User principal = (User) authentication.getPrincipal();
            username = principal.getUsername();
        }
        boolean isSuccess = true;
        if (properties.isJwtEnable()) {
            if (response.isCommitted()) {
                LOGGER.debug("Response has already been committed");
                return;
            }
            map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("flag", "success_login");

            Set<String> roles = new HashSet<>();
//            if (CollectionUtil.isNotEmpty(authorities)) {
//                for (GrantedAuthority authority : authorities) {
//                    String roleName = authority.getAuthority();
//                    roles.add(roleName);
//                }
//            }
            JwtTokenPair jwtTokenPair = jwtTokenGenerator.jwtTokenPair(username, roles, null);
            map.put("access_token", jwtTokenPair.getAccessToken());
            map.put("refresh_token", jwtTokenPair.getRefreshToken());
        } else {
            // 直接使用token
            String token = null ;
            try {
                token = tokenManager.generate(username);
            } catch (Exception e) {
                LOGGER.warn("can not get token:{}", e.getMessage());
                isSuccess = false;
            }
            if (StringUtils.hasLength(token)){
                map.put("token", token);
                tokenManager.put(token, username);
            }
        }
        ResponseUtil.responseJsonWriter(response, isSuccess ? RestBody.okData(map, "SUCCESS") : RestBody.failure(401, "forbidden"));
        // hook
        if (callbackHandler == null) {
            return;
        }
        callbackHandler.handleAuthorizationCallback(true, request, response, userDetailService.getUserInfo());
    }
}
