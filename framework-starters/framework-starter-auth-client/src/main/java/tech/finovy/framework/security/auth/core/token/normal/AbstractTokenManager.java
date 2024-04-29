package tech.finovy.framework.security.auth.core.token.normal;

import jakarta.servlet.http.HttpServletRequest;
import tech.finovy.framework.security.auth.core.po.UserDetails;

import java.util.UUID;

public abstract class AbstractTokenManager implements TokenManager {

    @Override
    public String generate(HttpServletRequest request, UserDetails userDetails) {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean refresh(String token, UserDetails userDetails) {
        return false;
    }
}
