package tech.finovy.framework.security.oidc.core.token.normal;

import org.springframework.security.core.userdetails.UserDetails;
import tech.finovy.framework.security.oidc.common.CacheTypeEnum;

public interface TokenManager {

    /**
     * generate token
     * @param username username
     * @return token
     */
    String generate(String username);

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
     * @param token token
     * @param username username
     * @return true/false
     */
    boolean put(String token,String username);

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
