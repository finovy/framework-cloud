package tech.finovy.framework.security.auth.core.token.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt config
 */
@Data
@ConfigurationProperties(prefix = JwtProperties.JWT_PREFIX)
public class JwtProperties {
    static final String JWT_PREFIX = "jwt.config";
    /**
     * 是否可用
     */
    private boolean enabled;
    /**
     * jks 路径
     */
    private String keyLocation = "default.jks";
    /**
     * key alias
     */
    private String keyAlias;
    /**
     * key store pass
     */
    private String keyPass;
    /**
     * jwt签发者
     **/
    private String iss;
    /**
     * jwt所面向的用户
     **/
    private String sub;
    /**
     * access jwt token 有效天数
     */
    private int accessExpDays;
    /**
     * refresh jwt token 有效天数
     */
    private int refreshExpDays;
}
