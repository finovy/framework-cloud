package tech.finovy.framework.security.auth.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.finovy.framework.security.auth.core.config.AuthorizationExtensionProperties;
import tech.finovy.framework.security.auth.core.token.normal.TokenManager;
import tech.finovy.framework.security.auth.util.RequestUtil;

import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final AuthorizationExtensionProperties properties;
    private final TokenManager tokenManager;

    public TokenAuthenticationFilter(TokenManager tokenManager, AuthorizationExtensionProperties properties) {
        this.tokenManager = tokenManager;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (properties.isJwtEnable()) {
            chain.doFilter(request, response);
            return;
        }
        String token = RequestUtil.obtainAuthorization(request, properties.getTokenHeader(), properties.getTokenParameter());
        tech.finovy.framework.security.auth.core.po.UserDetails userDetails = null;
        if (StringUtils.hasText(token)) {
            userDetails = tokenManager.get(token);
        }
        if (!ObjectUtils.isEmpty(userDetails)) {
            final UserDetails securityUser = org.springframework.security.core.userdetails.User.builder().username(userDetails.getEmail()).password(userDetails.getPassword()).build();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // refresh token
            if (tokenManager.refresh(token, userDetails) && properties.isDebugEnable()) {
                LOGGER.info("[{}] refresh token success!", userDetails.getEmail());
            }
        }
        chain.doFilter(request, response);
    }


}
