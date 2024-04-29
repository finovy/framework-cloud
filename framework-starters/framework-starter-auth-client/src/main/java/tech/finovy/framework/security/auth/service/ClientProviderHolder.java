package tech.finovy.framework.security.auth.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import tech.finovy.framework.security.auth.core.config.AuthorizationExtensionProperties;
import tech.finovy.framework.security.auth.core.oidc.AuthorizationProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientProviderHolder {

    public static ClientRegistrationRepository clientRegistrationRepository = null;
    public static AuthorizationExtensionProperties extensionProperties = null;

    public static List<Provider> get() {
        List<Provider> providers = new ArrayList<>();
        for (AuthorizationProvider provider : AuthorizationProvider.values()) {
            if (provider.isEnable()) {
                String url;
                if (extensionProperties.isStateEnable()) {
                    url = "/oauth2/authorization/" + provider.name().toLowerCase();
                } else {
                    final ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(provider.name().toLowerCase());
                    url = registration.getProviderDetails().getAuthorizationUri() + "?response_type=code&client_id=" + registration.getClientId();
                    if (registration.getScopes() != null && !registration.getScopes().isEmpty()) {
                        url = url + "&scope=" + registration.getScopes().stream()
                                .map(Object::toString)
                                .collect(Collectors.joining("%20"));
                    }
                    url = url + "&redirect_uri=" + registration.getRedirectUri();
                }
                providers.add(new Provider(provider.name().toLowerCase(), url));
            }
        }
        return providers;
    }

    public static void setClientRegistrationRepository(ClientRegistrationRepository repository) {
        clientRegistrationRepository = repository;
    }

    public static void setAuthorizationExtensionProperties(AuthorizationExtensionProperties properties) {
        extensionProperties = properties;
    }

    @Data
    @AllArgsConstructor
    public static class Provider {
        String type;
        String url;
    }
}
