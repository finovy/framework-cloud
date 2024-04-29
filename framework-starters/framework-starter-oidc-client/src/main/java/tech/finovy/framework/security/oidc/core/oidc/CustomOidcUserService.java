package tech.finovy.framework.security.oidc.core.oidc;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.HashSet;
import java.util.Set;

public class CustomOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser defaultOidcUser = super.loadUser(userRequest);
        OidcIdToken idToken = defaultOidcUser.getIdToken();
        OidcUserInfo userInfo = defaultOidcUser.getUserInfo();
        Set<GrantedAuthority> authorities = new HashSet<>(defaultOidcUser.getAuthorities());
        authorities.add(new OidcUserAuthority(idToken, userInfo));
        return new DefaultOidcUser(authorities, idToken, userInfo, "preferred_username");
    }

}
