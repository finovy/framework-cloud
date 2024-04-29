package tech.finovy.framework.security.auth.core.token.normal;

import jakarta.servlet.http.HttpServletRequest;
import tech.finovy.framework.security.auth.common.CacheTypeEnum;
import tech.finovy.framework.security.auth.core.po.UserDetails;

public interface TokenManager<T> {

    /**
     * generate token
     *
     * @param user username
     * @return token
     */
    T generate(HttpServletRequest request, UserDetails user);

    /**
     * get info,if user not exist just return null
     *
     * @param token token
     * @return info
     */
    UserDetails get(String token);

    /**
     * save token
     *
     * @param token    token
     * @param user username
     * @return true/false
     */
    boolean put(String token, UserDetails user);

    /**
     * refresh token
     *
     * @param token token
     * @return true/false
     */
    boolean refresh(String token, UserDetails user);

    /**
     * deny
     *
     * @param token token
     * @return true/false
     */
    boolean expire(String token);

    /**
     * type
     *
     * @return type
     */
    CacheTypeEnum getType();
}
