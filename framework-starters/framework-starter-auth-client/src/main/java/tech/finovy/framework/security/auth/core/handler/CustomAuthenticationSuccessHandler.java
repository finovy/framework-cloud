package tech.finovy.framework.security.auth.core.handler;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import tech.finovy.framework.security.auth.core.po.TokenPackage;
import tech.finovy.framework.security.auth.core.po.UserDetails;
import tech.finovy.framework.security.auth.core.token.jwt.JwtTokenGenerator;
import tech.finovy.framework.security.auth.core.token.jwt.JwtTokenPair;
import tech.finovy.framework.security.auth.core.token.normal.TokenManager;
import tech.finovy.framework.security.auth.core.config.AuthorizationExtensionProperties;
import tech.finovy.framework.security.auth.extend.AuthorizationCallbackHandler;
import tech.finovy.framework.security.auth.extend.UserDetailService;
import tech.finovy.framework.security.auth.util.ResponseUtil;
import tech.finovy.framework.security.auth.util.RestBody;

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

    // 第三方登录成功回调执行到这里
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>(5);
        final UserDetails userDetails = new UserDetails();
        if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
            final OidcUserInfo userInfo = principal.getUserInfo();

            userDetails.setEmail(userInfo.getEmail());
            userDetails.setFamilyName(userInfo.getFamilyName());
            userDetails.setPreferredUsername(userInfo.getPreferredUsername());
            userDetails.setGivenName(userInfo.getGivenName());
            userDetails.setPhone(userInfo.getPhoneNumber());
            userDetails.setSub(userInfo.getSubject());
        }
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            userDetails.setEmail(principal.getName());
            userDetails.setFamilyName((String)principal.getAttributes().get("family_name"));
            userDetails.setPreferredUsername((String)principal.getAttributes().get("preferred_username"));
            userDetails.setGivenName((String)principal.getAttributes().get("given_name"));
            userDetails.setPhone((String)principal.getAttributes().get("phone_number"));
            userDetails.setSub((String)principal.getAttributes().get("sub"));
        }
        if (authentication.getPrincipal() instanceof User) {
            User principal = (User) authentication.getPrincipal();
            userDetails.setEmail(principal.getUsername());
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
            JwtTokenPair jwtTokenPair = jwtTokenGenerator.jwtTokenPair(userDetails.getEmail(), roles, null);
            map.put("access_token", jwtTokenPair.getAccessToken());
            map.put("refresh_token", jwtTokenPair.getRefreshToken());
        } else {
            // 直接使用token
            String tokenStr = null;
            try {
                Object token = tokenManager.generate(request, userDetails);
                if (token instanceof TokenPackage tokenPackage) {
                    tokenStr = tokenPackage.getToken();
                    map.put("token", tokenStr);
                    ResponseUtil.responseJsonWriter(response, tokenPackage.getCode() == 200 ? RestBody.okData(map, "SUCCESS") : RestBody.failure(tokenPackage.getCode(), tokenPackage.getMessage()));
                    return;
                }
                tokenStr = (String) token;
            } catch (Exception e) {
                LOGGER.warn("can not get token:{}", e.getMessage());
                isSuccess = false;
            }
            if (StringUtils.hasLength(tokenStr)) {
                map.put("token", tokenStr);
                tokenManager.put(tokenStr, userDetails);
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
