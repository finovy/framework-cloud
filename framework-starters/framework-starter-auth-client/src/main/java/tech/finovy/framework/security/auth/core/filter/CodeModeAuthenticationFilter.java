package tech.finovy.framework.security.auth.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.finovy.framework.security.auth.core.config.AuthorizationExtensionProperties;

import java.io.IOException;
import java.util.Collections;

/**
 * @Author: Ryan Luo
 * @Date: 2023/12/28 14:40
 */
public class CodeModeAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationExtensionProperties.class);

    private final AuthorizationExtensionProperties properties;

    public CodeModeAuthenticationFilter(AuthorizationExtensionProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (properties.isCodeModeEnable()) {
            LOGGER.warn("The development mode has been activated. Please be aware!");
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                var authenticationToken = new UsernamePasswordAuthenticationToken(User.builder().username(properties.getCodeModeUsername()).password("DEFAULT").build(), null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
