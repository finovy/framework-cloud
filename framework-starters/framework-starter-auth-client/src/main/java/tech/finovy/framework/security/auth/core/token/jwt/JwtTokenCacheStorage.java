package tech.finovy.framework.security.auth.core.token.jwt;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import tech.finovy.framework.security.auth.common.CacheTypeEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Jwt token cache storage.
 */
public class JwtTokenCacheStorage implements JwtTokenStorage {
    /**
     * 查看缓存配置文件 ehcache.xml 定义 过期时间与 refresh token 过期一致.
     */
    private static final String TOKEN_CACHE = "usrTkn";

    private static final Map<String, JwtTokenPair> map = new ConcurrentHashMap<>();

    @CachePut(value = TOKEN_CACHE, key = "#userId")
    @Override
    public JwtTokenPair put(JwtTokenPair jwtTokenPair, String userId) {
        map.put(userId, jwtTokenPair);
        return jwtTokenPair;
    }

    @CacheEvict(value = TOKEN_CACHE, key = "#userId")
    @Override
    public void expire(String userId) {
        map.remove(userId);
    }

    @Cacheable(value = TOKEN_CACHE, key = "#userId")
    @Override
    public JwtTokenPair get(String userId) {
        return map.get(userId);
    }

    @Override
    public CacheTypeEnum getType() {
        return CacheTypeEnum.MEMORY;
    }
}
