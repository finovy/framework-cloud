package tech.finovy.framework.security.auth.core.oidc;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import tech.finovy.framework.security.auth.core.config.AuthorizationExtensionProperties;

import java.net.URI;
import java.util.Objects;

/**
 * auth providers
 */
public enum AuthorizationProvider {

    /**
     * keycloak oidc
     */
    KEYCLOAK {
        boolean enable = false;
        // DEFAULT URL
        String HOST_PREFIX = "https://www.keycloak.org";
        String REALM = "master";

        @Override
        public ClientRegistration getBuilder(OAuth2ClientProperties properties, AuthorizationExtensionProperties config) {
            String registrationId = getRegistrationId();
            final ClientRegistration.Builder builder = getClientRegistrationBuilder(properties, registrationId);
            builder.scope("openid");
            final AuthorizationExtensionProperties.ExtensionRegistration extensionRegistration = config.getRegistration().get(registrationId);
            if (Objects.nonNull(extensionRegistration) && StringUtils.hasLength(extensionRegistration.getHost())) {
                HOST_PREFIX = extensionRegistration.getHost();
                REALM = extensionRegistration.getRealm();
            }
            builder.authorizationUri(HOST_PREFIX + "/realms/" + REALM + "/protocol/openid-connect/auth");
            builder.userInfoUri(HOST_PREFIX + "/realms/" + REALM + "/protocol/openid-connect/userinfo");
            builder.tokenUri(HOST_PREFIX + "/realms/" + REALM + "/protocol/openid-connect/token");
            builder.jwkSetUri(HOST_PREFIX + "/realms/" + REALM + "/protocol/openid-connect/certs");
            builder.issuerUri(HOST_PREFIX + "/realms/" + REALM);
            builder.userNameAttributeName("email");
            // Configuration Precedence Principle
            enable = true;
            return fromProperties(registrationId, properties, builder);
        }

        @Override
        public String getRegistrationId() {
            return KEYCLOAK.name().toLowerCase();
        }

        @Override
        public boolean isEnable() {
            return enable;
        }

        @Override
        public Converter<OAuth2UserRequest, RequestEntity<?>> userInfoUriRequestEntityConverter() {
            return this.UserInfoConverter();
        }

        @Override
        public Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> tokenUriRequestEntityConverter() {
            return new CustomAuthorizationCodeGrantRequestEntityConverter();
        }
    },


    /**
     * oauth2
     */
    FEISHU {
        boolean enable = false;
        @Override
        public ClientRegistration getBuilder(OAuth2ClientProperties properties, AuthorizationExtensionProperties config) {
            String registrationId = getRegistrationId();
            final ClientRegistration.Builder builder = getClientRegistrationBuilder(properties, registrationId);
            builder.authorizationUri("https://passport.feishu.cn/suite/passport/oauth/authorize");
            builder.userInfoUri("https://passport.feishu.cn/suite/passport/oauth/userinfo");
            builder.tokenUri("https://passport.feishu.cn/suite/passport/oauth/token");
            builder.userNameAttributeName("name");
            // Configuration Precedence Principle
            enable = true;
            return fromProperties(registrationId, properties, builder);
        }

        @Override
        public String getRegistrationId() {
            return FEISHU.name().toLowerCase();
        }

        @Override
        public boolean isEnable() {
            return enable;
        }

        @Override
        public Converter<OAuth2UserRequest, RequestEntity<?>> userInfoUriRequestEntityConverter() {
            return this.UserInfoConverter();
        }

        @Override
        public Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> tokenUriRequestEntityConverter() {
            return new FeiShuOAuth2AuthorizationCodeGrantRequestEntityConverter();
        }
    },

    /**
     * oauth2
     */
    OFFICE {
        boolean enable = false;
        @Override
        public ClientRegistration getBuilder(OAuth2ClientProperties properties, AuthorizationExtensionProperties config) {
            String registrationId = getRegistrationId();
            ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
            OAuth2ClientProperties.Registration registration = properties.getRegistration().get(registrationId);
            builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
            builder.redirectUri(DEFAULT_REDIRECT_URL);
            builder.clientId(registration.getClientId());
            builder.clientSecret(registration.getClientSecret());
            builder.scope("user.read", "offline_access");
            builder.authorizationUri("https://login.microsoftonline.com/organizations/oauth2/v2.0/authorize");
            builder.userInfoUri("https://graph.microsoft.com/v1.0/me");
            builder.tokenUri("https://login.microsoftonline.com/organizations/oauth2/v2.0/token");
            builder.userNameAttributeName("mail");
            enable = true;
            return fromProperties(registrationId, properties, builder);
        }

        @Override
        public String getRegistrationId() {
            return OFFICE.name().toLowerCase();
        }

        @Override
        public boolean isEnable() {
            return enable;
        }

        @Override
        public Converter<OAuth2UserRequest, RequestEntity<?>> userInfoUriRequestEntityConverter() {
            return this.UserInfoConverter();
        }

        @Override
        public Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> tokenUriRequestEntityConverter() {
            return new CustomAuthorizationCodeGrantRequestEntityConverter();
        }
    },

    GITEE {
        boolean enable = false;
        @Override
        public ClientRegistration getBuilder(OAuth2ClientProperties properties, AuthorizationExtensionProperties config) {
            String registrationId = getRegistrationId();
            final ClientRegistration.Builder builder = getClientRegistrationBuilder(properties, registrationId);
            builder.scope("user_info");
            builder.authorizationUri("https://gitee.com/oauth/authorize");
            builder.userInfoUri("https://gitee.com/api/v5/user");
            builder.tokenUri("https://gitee.com/oauth/token");
            builder.userNameAttributeName("name");
            enable = true;
            return fromProperties(registrationId, properties, builder);
        }

        @Override
        public String getRegistrationId() {
            return GITEE.name().toLowerCase();
        }

        @Override
        public boolean isEnable() {
            return enable;
        }

        @Override
        public Converter<OAuth2UserRequest, RequestEntity<?>> userInfoUriRequestEntityConverter() {
            return this.UserInfoConverter();
        }

        @Override
        public Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> tokenUriRequestEntityConverter() {
            return new CustomAuthorizationCodeGrantRequestEntityConverter();
        }
    };




    private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/auth/code/{registrationId}";
    private static final Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> TOKEN_REQUEST_ENTITY_CONVERTER = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    private static final Converter<OAuth2UserRequest, RequestEntity<?>> USER_INFO_REQUEST_ENTITY_CONVERTER = new OAuth2UserRequestEntityConverter();

    AuthorizationProvider() {
    }


    protected ClientRegistration.Builder getClientRegistrationBuilder(OAuth2ClientProperties properties,String registrationId) {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
        OAuth2ClientProperties.Registration registration = properties.getRegistration().get(registrationId);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.redirectUri(DEFAULT_REDIRECT_URL);
        builder.clientId(registration.getClientId());
        builder.clientSecret(registration.getClientSecret());
        return builder;
    }

    protected Converter<OAuth2UserRequest, RequestEntity<?>> UserInfoConverter(){
        return userRequest -> {
            ClientRegistration clientRegistration = userRequest.getClientRegistration();
            HttpMethod httpMethod = HttpMethod.GET;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
            headers.add("Authorization", "Bearer" + " " + userRequest.getAccessToken().getTokenValue());
            clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri();
            URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                    .build()
                    .toUri();
            return new RequestEntity<>(headers, httpMethod, uri);
        };
    }
    /**
     * Gets builder.
     *
     * @param properties the properties
     * @return the builder
     */
    public abstract ClientRegistration getBuilder(OAuth2ClientProperties properties, AuthorizationExtensionProperties config);

    /**
     * Gets registration id.
     *
     * @return the registration id
     */
    public abstract String getRegistrationId();
    public abstract boolean isEnable();

    public abstract Converter<OAuth2UserRequest, RequestEntity<?>> userInfoUriRequestEntityConverter();

    public abstract Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> tokenUriRequestEntityConverter();


    /**
     * load properties
     *
     * @param registrationId the registration id
     * @param properties     the properties
     * @param builder        the builder
     * @return the client registration
     */
    private static ClientRegistration fromProperties(String registrationId, OAuth2ClientProperties properties, ClientRegistration.Builder builder) {
        if (Objects.isNull(properties)) {
            return builder.build();
        }
        OAuth2ClientProperties.Registration registration = properties.getRegistration().get(registrationId);
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        if (Objects.nonNull(registration)) {
            map.from(registration::getClientId).to(builder::clientId);
            map.from(registration::getClientSecret).to(builder::clientSecret);
            map.from(registration::getClientAuthenticationMethod).as(ClientAuthenticationMethod::new)
                    .to(builder::clientAuthenticationMethod);
            map.from(registration::getAuthorizationGrantType).as(AuthorizationGrantType::new)
                    .to(builder::authorizationGrantType);
            map.from(registration::getRedirectUri).to(builder::redirectUri);
            map.from(registration::getScope).as(StringUtils::toStringArray).to(builder::scope);
            map.from(registration::getClientName).to(builder::clientName);
        }
        OAuth2ClientProperties.Provider provider = properties.getProvider().get(registrationId);
        if (Objects.nonNull(provider)) {
            map.from(provider::getAuthorizationUri).to(builder::authorizationUri);
            map.from(provider::getTokenUri).to(builder::tokenUri);
            map.from(provider::getUserInfoUri).to(builder::userInfoUri);
            map.from(provider::getUserInfoAuthenticationMethod).as(AuthenticationMethod::new)
                    .to(builder::userInfoAuthenticationMethod);
            map.from(provider::getJwkSetUri).to(builder::jwkSetUri);
            map.from(provider::getUserNameAttribute).to(builder::userNameAttributeName);
        }
        return builder.build();
    }

}
