package tech.finovy.framework.security.oidc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.finovy.framework.security.oidc.core.config.AuthorizationExtensionProperties;
import tech.finovy.framework.security.oidc.core.filter.JwtAuthenticationFilter;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtProperties;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtTokenCacheStorage;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtTokenGenerator;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtTokenStorage;

@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "spring.security.oauth2.client", name = "jwt-enable", matchIfMissing = true)
@Configuration
public class JwtConfiguration {
    @Bean
    public JwtTokenStorage jwtTokenStorage() {
        return new JwtTokenCacheStorage();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenGenerator jwtTokenGenerator, JwtTokenStorage jwtTokenStorage, AuthorizationExtensionProperties properties) {
        return new JwtAuthenticationFilter(jwtTokenGenerator, jwtTokenStorage, properties);
    }
}
