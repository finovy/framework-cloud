package tech.finovy.framework.security.auth.core.oidc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("all")
public class DelegateOAuth2UserRequestEntityConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {

    private final Map<String, Converter<OAuth2UserRequest, RequestEntity<?>>> converters = new HashMap<>();
    private final Converter<OAuth2UserRequest, RequestEntity<?>> defaultConverter = new OAuth2UserRequestEntityConverter();

    public void addConverter(String registrationId, Converter<OAuth2UserRequest, RequestEntity<?>> converter) {
        if (converters.containsKey(registrationId)) {
            throw new IllegalArgumentException("registrationId " + registrationId + "is present");
        } else {
            converters.put(registrationId, converter);
        }
    }

    @Override
    public RequestEntity<?> convert(OAuth2UserRequest oAuth2UserRequest) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Converter<OAuth2UserRequest, RequestEntity<?>> oAuth2UserRequestRequestEntityConverter = converters.get(registrationId);
        if (Objects.isNull(oAuth2UserRequestRequestEntityConverter)) {
            return defaultConverter.convert(oAuth2UserRequest);
        }
        return oAuth2UserRequestRequestEntityConverter.convert(oAuth2UserRequest);
    }
}
