package tech.finovy.framework.security.oidc.core.oidc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("all")
public class DelegateOAuth2AuthorizationCodeGrantRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private final Map<String, Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>>> converters = new HashMap<>();
    private final Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();


    public void addConverter(String registrationId, Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> converter) {
        if (converters.containsKey(registrationId)) {
            throw new IllegalArgumentException("registrationId " + registrationId + "is present");
        } else {
            converters.put(registrationId, converter);
        }
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest oAuth2AuthorizationCodeGrantRequest) {
        String registrationId = oAuth2AuthorizationCodeGrantRequest.getClientRegistration().getRegistrationId();
        Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> codeGrantRequestRequestEntityConverter = converters.get(registrationId);
        if (Objects.isNull(codeGrantRequestRequestEntityConverter)) {
            return defaultConverter.convert(oAuth2AuthorizationCodeGrantRequest);
        }
        return codeGrantRequestRequestEntityConverter.convert(oAuth2AuthorizationCodeGrantRequest);
    }
}
