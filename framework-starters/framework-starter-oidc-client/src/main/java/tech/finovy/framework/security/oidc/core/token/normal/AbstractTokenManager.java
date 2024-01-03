package tech.finovy.framework.security.oidc.core.token.normal;

import java.util.UUID;

public abstract class AbstractTokenManager implements TokenManager {

    @Override
    public String generate(String username) {
        return UUID.randomUUID().toString();
    }
}
