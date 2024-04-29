package tech.finovy.framework.security.oidc.extend;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

/**
 * If you want to retrieve user information, inject this bean.
 */
public interface UserDetailService {

    /**
     * userName who has login
     *
     * @return username
     */
    String getCurrentUsername();

    /**
     * userName who has login
     *
     * @return username
     */
    String getCurrentUserEmail();

    /**
     * user info
     *
     * @return username
     */
    OidcUserInfo getUserInfo();
}
