package tech.finovy.framework.security.oidc.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.finovy.framework.security.oidc.common.SecurityConstants;
import tech.finovy.framework.security.oidc.core.config.AuthorizationExtensionProperties;
import tech.finovy.framework.security.oidc.core.token.normal.TokenStorage;
import tech.finovy.framework.security.oidc.util.RequestUtil;

import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthorizationExtensionProperties properties;
    private final TokenStorage tokenStorage;

    public TokenAuthenticationFilter(TokenStorage tokenStorage, AuthorizationExtensionProperties properties) {
        this.tokenStorage = tokenStorage;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (properties.isJwtEnable()){
            chain.doFilter(request, response);
            return;
        }
        String token = RequestUtil.obtainAuthorization(request, properties.getTokenHeader(), properties.getTokenParameter());
        UserDetails userDetails = null;
        if (StringUtils.hasText(token)) {
            userDetails = tokenStorage.get(token);
        }
        if (!ObjectUtils.isEmpty(userDetails)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }


}
