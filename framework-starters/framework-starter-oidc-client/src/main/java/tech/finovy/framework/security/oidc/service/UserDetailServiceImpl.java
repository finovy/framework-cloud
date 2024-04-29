package tech.finovy.framework.security.oidc.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import tech.finovy.framework.security.oidc.UserDetailService;

public class UserDetailServiceImpl implements UserDetailService {
    @Override
    public String getCurrentUsername() {
        return getUserInfo().getFullName();
    }

    @Override
    public String getCurrentUserEmail() {
        return getUserInfo().getEmail();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser userDetails) {
            return userDetails.getUserInfo();
        }
        throw new IllegalStateException("can not find user");
    }
}
