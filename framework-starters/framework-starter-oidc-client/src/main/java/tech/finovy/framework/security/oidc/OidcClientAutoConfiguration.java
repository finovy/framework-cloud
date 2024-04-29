package tech.finovy.framework.security.oidc;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tech.finovy.framework.security.oidc.core.*;
import tech.finovy.framework.security.oidc.core.handler.CustomAuthenticationFailureHandler;
import tech.finovy.framework.security.oidc.core.handler.CustomAuthenticationSuccessHandler;
import tech.finovy.framework.security.oidc.service.UserDetailServiceImpl;

import javax.naming.ConfigurationException;
import java.util.*;
import java.util.stream.Collectors;

@EnableWebSecurity
@EnableConfigurationProperties(AuthorizationExtensionConfig.class)
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
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler(@Nullable AuthorizationCallbackHandler callbackHandler, UserDetailService userDetailService, AuthorizationExtensionConfig config) {
        return new CustomAuthenticationSuccessHandler(callbackHandler, userDetailService, config);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OidcUserService oidcUserService, AuthorizationExtensionConfig extensionConfig, DefaultAuthorizationCodeTokenResponseClient authorizationCodeTokenResponseClient, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        // success redirect url
        customAuthenticationSuccessHandler.setDefaultTargetUrl(extensionConfig.getDefaultSuccessUrl());
        http.csrf().disable()
                .cors()
                .and().authorizeHttpRequests(
                        (authorize) -> {
                            String[] passPaths;
                            try {
                                passPaths = extensionConfig.getPassPaths().toArray(new String[0]);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            authorize
                                    .requestMatchers(passPaths).permitAll()
                                    .anyRequest().authenticated();

                        }
                )
                .oauth2Login()
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .userInfoEndpoint().oidcUserService(oidcUserService)
                .and()
                .loginPage(extensionConfig.getLoginPage())
                .loginProcessingUrl("/login/oidc/code/*")
                .tokenEndpoint()
                .accessTokenResponseClient(authorizationCodeTokenResponseClient);
        http.oauth2Client();
        http.httpBasic().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(extensionConfig.getLogoutUrl()))
                .logoutSuccessUrl(extensionConfig.getLoginPage())
                .deleteCookies(extensionConfig.getDeleteCookiesName())
                .invalidateHttpSession(true);
        return http.getOrBuild();
    }


    @Bean
    public DelegateOAuth2UserRequestEntityConverter delegateOAuth2UserRequestEntityConverter() {
        DelegateOAuth2UserRequestEntityConverter oAuth2UserRequestEntityConverter = new DelegateOAuth2UserRequestEntityConverter();
        // 遍历所有转化器
        Arrays.stream(OidcAuthorizationProvider.values()).forEach(oAuth2Provider -> {
            String registrationId = oAuth2Provider.getRegistrationId();
            Converter<OAuth2UserRequest, RequestEntity<?>> userRequestEntityConverter = oAuth2Provider.userInfoUriRequestEntityConverter();
            oAuth2UserRequestEntityConverter.addConverter(registrationId, userRequestEntityConverter);
        });
        return oAuth2UserRequestEntityConverter;
    }

    @Bean
    InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties, AuthorizationExtensionConfig config) {
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
        Map<String, ClientRegistration> clientRegistrations = OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(oAuth2ClientProperties);
        List<ClientRegistration> clientRegistrationList = new ArrayList<>(clientRegistrations.values());
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
}
