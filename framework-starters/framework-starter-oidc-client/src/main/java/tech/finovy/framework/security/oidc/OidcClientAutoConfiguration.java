package tech.finovy.framework.security.oidc;

import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.finovy.framework.security.oidc.core.account.CustomDaoAuthenticationProvider;
import tech.finovy.framework.security.oidc.core.account.CustomPasswordEncoderEnhance;
import tech.finovy.framework.security.oidc.core.account.UserDetailsRepository;
import tech.finovy.framework.security.oidc.core.config.AuthorizationExtensionProperties;
import tech.finovy.framework.security.oidc.core.exception.SimpleAccessDeniedHandler;
import tech.finovy.framework.security.oidc.core.exception.SimpleAuthenticationEntryPoint;
import tech.finovy.framework.security.oidc.core.filter.CodeModeAuthenticationFilter;
import tech.finovy.framework.security.oidc.core.filter.JwtAuthenticationFilter;
import tech.finovy.framework.security.oidc.core.filter.PreLoginFilter;
import tech.finovy.framework.security.oidc.core.filter.TokenAuthenticationFilter;
import tech.finovy.framework.security.oidc.core.handler.*;
import tech.finovy.framework.security.oidc.core.oidc.CustomOidcUserService;
import tech.finovy.framework.security.oidc.core.oidc.DelegateOAuth2AuthorizationCodeGrantRequestEntityConverter;
import tech.finovy.framework.security.oidc.core.oidc.DelegateOAuth2UserRequestEntityConverter;
import tech.finovy.framework.security.oidc.core.oidc.OidcAuthorizationProvider;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtProperties;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtTokenCacheStorage;
import tech.finovy.framework.security.oidc.core.token.jwt.JwtTokenGenerator;
import tech.finovy.framework.security.oidc.core.token.normal.TokenManager;
import tech.finovy.framework.security.oidc.extend.AuthorizationCallbackHandler;
import tech.finovy.framework.security.oidc.extend.CustomPasswordEncoder;
import tech.finovy.framework.security.oidc.extend.UserDetailService;
import tech.finovy.framework.security.oidc.extend.UsernameAndPasswordService;
import tech.finovy.framework.security.oidc.service.UserDetailServiceImpl;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@EnableWebSecurity
@EnableConfigurationProperties({AuthorizationExtensionProperties.class, JwtProperties.class})
@Configuration(proxyBeanMethods = false)
public class OidcClientAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(OidcClientAutoConfiguration.class);

    @Bean
    public OidcUserService customOidcUserService() {
        return new CustomOidcUserService();
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler(@Nullable AuthorizationCallbackHandler callbackHandler, UserDetailService userDetailService) {
        return new CustomAuthenticationFailureHandler(callbackHandler, userDetailService);
    }


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(CodeModeAuthenticationFilter codeModeAuthenticationFilter, @Nullable JwtTokenCacheStorage jwtTokenCacheStorage, @Nullable TokenManager tokenManager, TokenAuthenticationFilter tokenAuthenticationFilter, @Nullable JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationSuccessHandler successHandler, PreLoginFilter preLoginFilter, HttpSecurity http, OidcUserService oidcUserService, AuthorizationExtensionProperties extensionConfig, DefaultAuthorizationCodeTokenResponseClient authorizationCodeTokenResponseClient, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) throws Exception {
        if (jwtAuthenticationFilter != null) {
            http.addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class);
        }
        http.addFilterBefore(tokenAuthenticationFilter, LogoutFilter.class);
        http.addFilterAfter(codeModeAuthenticationFilter, TokenAuthenticationFilter.class);
        http.csrf(csrf -> {
                    csrf.disable();
                })
                .cors(Customizer.withDefaults())
                .addFilterBefore(preLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> {
                    String[] passPaths;
                    try {
                        passPaths = extensionConfig.getPassPaths().toArray(new String[0]);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    authorize
                            .requestMatchers(passPaths).permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(formlogin -> {
                    formlogin.loginProcessingUrl(extensionConfig.getDefaultAccountLoginUrl())
                            .successHandler(successHandler)
                            .failureHandler(customAuthenticationFailureHandler);
                })
                .oauth2Login(oauth2Login ->
                        oauth2Login.successHandler(successHandler)
                                .failureHandler(customAuthenticationFailureHandler)
                                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService))
                                .loginProcessingUrl("/login/oidc/code/*")
                                .tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig.accessTokenResponseClient(authorizationCodeTokenResponseClient)).authorizationEndpoint().authorizationRedirectStrategy(new CustomRedirectStrategy()))
                .oauth2Client(Customizer.withDefaults())
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher(extensionConfig.getLogoutUrl()))
                        .addLogoutHandler(new CustomLogoutHandler())
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler(extensionConfig, jwtTokenCacheStorage, tokenManager)))
                .sessionManagement(maganger -> {
                    maganger.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });
        http.exceptionHandling().accessDeniedHandler(new SimpleAccessDeniedHandler()).authenticationEntryPoint(new SimpleAuthenticationEntryPoint());
        return http.getOrBuild();
    }

    @Bean
    public CustomPasswordEncoderEnhance customPasswordEncoderEnhance(@Nullable CustomPasswordEncoder passwordEncoder) {
        return new CustomPasswordEncoderEnhance(passwordEncoder);
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(UserDetailsManager userDetailsManager, UserDetailsRepository userDetailsRepository, CustomPasswordEncoderEnhance passwordEncoder) {
        final CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider(userDetailsManager, userDetailsRepository, passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public DelegateOAuth2UserRequestEntityConverter delegateOAuth2UserRequestEntityConverter() {
        DelegateOAuth2UserRequestEntityConverter oAuth2UserRequestEntityConverter = new DelegateOAuth2UserRequestEntityConverter();
        Arrays.stream(OidcAuthorizationProvider.values()).forEach(oAuth2Provider -> {
            String registrationId = oAuth2Provider.getRegistrationId();
            Converter<OAuth2UserRequest, RequestEntity<?>> userRequestEntityConverter = oAuth2Provider.userInfoUriRequestEntityConverter();
            oAuth2UserRequestEntityConverter.addConverter(registrationId, userRequestEntityConverter);
        });
        return oAuth2UserRequestEntityConverter;
    }

    @Bean
    @Primary
    InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties, AuthorizationExtensionProperties config) {
        Set<String> registrationIds = Arrays.stream(OidcAuthorizationProvider.values())
                .map(OidcAuthorizationProvider::getRegistrationId)
                .collect(Collectors.toSet());
        OAuth2ClientProperties oAuth2ClientProperties = new OAuth2ClientProperties();
        properties.getRegistration().keySet().forEach(registrationId -> {
            if (!registrationIds.contains(registrationId)) {
                oAuth2ClientProperties.getRegistration().put(registrationId, properties.getRegistration().get(registrationId));
                oAuth2ClientProperties.getProvider().put(registrationId, properties.getProvider().get(registrationId));
            }
        });
        List<ClientRegistration> clientRegistrationList = new ArrayList<>(
                new OAuth2ClientPropertiesMapper(oAuth2ClientProperties).asClientRegistrations().values());
        List<ClientRegistration> registrations = Arrays.stream(OidcAuthorizationProvider.values())
                .map(oAuth2Provider -> {
                    try {
                        return oAuth2Provider.getBuilder(properties, config);
                    } catch (Exception e) {
                        LOGGER.warn(" ClientRegistration @ {} is not registered , result {}",
                                oAuth2Provider.getRegistrationId(),
                                e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        registrations.addAll(clientRegistrationList);
        return new InMemoryClientRegistrationRepository(registrations);
    }


    /**
     * @return the delegate oauth2 authorization code grant request entity converter
     */
    @Bean
    public DelegateOAuth2AuthorizationCodeGrantRequestEntityConverter oAuth2AuthorizationCodeGrantRequestEntityConverter() {
        DelegateOAuth2AuthorizationCodeGrantRequestEntityConverter converter = new DelegateOAuth2AuthorizationCodeGrantRequestEntityConverter();
        Arrays.stream(OidcAuthorizationProvider.values()).forEach(customOAuth2Provider -> {
            String registrationId = customOAuth2Provider.getRegistrationId();
            Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> grantRequestRequestEntityConverter = customOAuth2Provider.tokenUriRequestEntityConverter();
            converter.addConverter(registrationId, grantRequestRequestEntityConverter);
        });
        return converter;
    }


    /**
     * Oauth2 user service default oauth2 user service.
     *
     * @param converter the converter
     * @return the default o auth 2 user service
     */
    @Bean
    public DefaultOAuth2UserService oAuth2UserService(DelegateOAuth2UserRequestEntityConverter converter) {
        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
        defaultOAuth2UserService.setRequestEntityConverter(converter);
        return defaultOAuth2UserService;
    }

    /**
     * @param converter the converter
     * @return the default authorization code token response client
     */
    @Bean
    public DefaultAuthorizationCodeTokenResponseClient oAuth2AccessTokenResponseClient(DelegateOAuth2AuthorizationCodeGrantRequestEntityConverter converter) {
        DefaultAuthorizationCodeTokenResponseClient defaultAuthorizationCodeTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        defaultAuthorizationCodeTokenResponseClient.setRequestEntityConverter(converter);
        return defaultAuthorizationCodeTokenResponseClient;
    }


    @Bean
    public UserDetailService userDetailService() {
        return new UserDetailServiceImpl();
    }

    @Bean
    public UserDetailsManager userDetailsManager(UserDetailsRepository userDetailsRepository) {
        return new UserDetailsManager() {
            @Override
            public void createUser(UserDetails user) {
                LOGGER.info("createUser function deny");
            }

            @Override
            public void updateUser(UserDetails user) {
                LOGGER.info("updateUser function deny");
            }

            @Override
            public void deleteUser(String username) {
                LOGGER.info("deleteUser function deny");
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                LOGGER.info("changePassword function closed");
            }

            @Override
            public boolean userExists(String username) {
                return userDetailsRepository.userExists(username);
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userDetailsRepository.loadUserByUsername(username);
            }
        };
    }

    @Bean
    public PreLoginFilter preLoginFilter(AuthorizationExtensionProperties config) {
        return new PreLoginFilter(config.getDefaultAccountLoginUrl(), Collections.emptyList());
    }

    @Bean
    public UserDetailsRepository userDetailsRepository(@Nullable UsernameAndPasswordService usernameAndPasswordService) {
        return new UserDetailsRepository(usernameAndPasswordService);
    }


    final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
        private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
            /*
             * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
             * the CsrfToken when it is rendered in the response body.
             */
            this.delegate.handle(request, response, csrfToken);
        }

        @Override
        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
            /*
             * If the request contains a request header, use CsrfTokenRequestAttributeHandler
             * to resolve the CsrfToken. This applies when a single-page application includes
             * the header value automatically, which was obtained via a cookie containing the
             * raw CsrfToken.
             */
            if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
                return super.resolveCsrfTokenValue(request, csrfToken);
            }
            /*
             * In all other cases (e.g. if the request contains a request parameter), use
             * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
             * when a server-side rendered form includes the _csrf request parameter as a
             * hidden input.
             */
            return this.delegate.resolveCsrfTokenValue(request, csrfToken);
        }
    }

    final class CsrfCookieFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
            // Render the token value to a cookie by causing the deferred token to be loaded
            csrfToken.getToken();

            filterChain.doFilter(request, response);
        }
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(@Nullable TokenManager tokenManager, @Nullable JwtTokenGenerator jwtTokenGenerator, @Nullable AuthorizationCallbackHandler callbackHandler, UserDetailService userDetailService, AuthorizationExtensionProperties properties) {
        final CustomAuthenticationSuccessHandler handler = new CustomAuthenticationSuccessHandler(tokenManager, jwtTokenGenerator, callbackHandler, userDetailService, properties);
        return handler;
    }


    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(@Nullable TokenManager tokenManager, AuthorizationExtensionProperties properties) {
        if (!properties.isJwtEnable() && tokenManager == null) {
            throw new RuntimeException("Jwt is disable,tokenManager is needed!");
        }
        return new TokenAuthenticationFilter(tokenManager, properties);
    }

    @Bean
    public CodeModeAuthenticationFilter codeModeAuthenticationFilter(AuthorizationExtensionProperties properties) {
        return new CodeModeAuthenticationFilter(properties);
    }
}
