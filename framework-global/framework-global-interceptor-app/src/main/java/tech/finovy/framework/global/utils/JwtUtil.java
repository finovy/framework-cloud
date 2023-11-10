package tech.finovy.framework.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.DigestUtils;
import tech.finovy.framework.global.Constant;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;


/**
 * JwtUtil
 */
public class JwtUtil {

    private static final String project = "project";

    private JwtUtil() {
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJwt(String jwt) throws Exception {
        SecretKey key = generalKey(project);
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
    }


    public static String createJwt(String appid, String userJson,long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        String uuid = UUID.randomUUID().toString();
        String signature = DigestUtils.md5DigestAsHex((appid + nowMillis.toString() + uuid).getBytes());
        Map<String, Object> claims = new HashMap<>(5);
        claims.put(Constant.TOKEN_HEADER_APPID, appid);
        claims.put(Constant.TOKEN_HEADER_TIMESTAMP, nowMillis);
        claims.put(Constant.TOKEN_HEADER_NONCE, uuid);
        claims.put(Constant.TOKEN_HEADER_SIGNATURE, signature);
        SecretKey key = generalKey(project);
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(userJson)
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static SecretKey generalKey(String k) {
        byte[] encodedKey = Base64.getDecoder().decode(k);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
