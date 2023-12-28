package tech.finovy.framework.security.oidc.core.account;


import jakarta.servlet.ServletRequest;

/**
 * login process for POST
 */
public interface LoginPostProcessor {

    /**
     * get login type
     *
     * @return the type
     */
    LoginTypeEnum getLoginTypeEnum();

    /**
     * get userName
     *
     * @param request the request
     * @return the string
     */
    String obtainUsername(ServletRequest request);

    /**
     * get password
     *
     * @param request the request
     * @return the string
     */
    String obtainPassword(ServletRequest request);

}
