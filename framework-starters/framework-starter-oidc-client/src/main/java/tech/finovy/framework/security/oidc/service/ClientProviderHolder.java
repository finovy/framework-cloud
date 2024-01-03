package tech.finovy.framework.security.oidc.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.finovy.framework.security.oidc.core.oidc.OidcAuthorizationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Ryan Luo
 * @Date: 2023/12/27 11:28
 */
public class ClientProviderHolder {

    public static List<Provider> get(){
        final List<Provider> providers = new ArrayList<>();
        providers.add(new Provider(OidcAuthorizationProvider.KEYCLOAK.name().toLowerCase(), "/oauth2/authorization/keycloak"));
        return providers;
    }

    @Data
    @AllArgsConstructor
    public static class Provider {
        String type;
        String url;
    }
}
