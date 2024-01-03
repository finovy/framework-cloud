package tech.finovy.framework.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
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

    public static Claims parseJwt(String jwt) {
        SecretKey key = generalKey(project);
        return Jwts.parser().decryptWith(key).build().parseSignedClaims(jwt).getPayload();
    }


    public static String createJwt(String appid, String userJson, long ttlMillis) {
        final MacAlgorithm signatureAlgorithm = Jwts.SIG.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        String uuid = UUID.randomUUID().toString();
        String signature = DigestUtils.md5DigestAsHex((appid + nowMillis + uuid).getBytes());
        Map<String, Object> claims = new HashMap<>(5);
        claims.put(Constant.TOKEN_HEADER_APPID, appid);
        claims.put(Constant.TOKEN_HEADER_TIMESTAMP, nowMillis);
        claims.put(Constant.TOKEN_HEADER_NONCE, uuid);
        claims.put(Constant.TOKEN_HEADER_SIGNATURE, signature);
        SecretKey key = generalKey(project);
        JwtBuilder builder = Jwts.builder()
                .claims(claims)
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .subject(userJson)
                .signWith(key, signatureAlgorithm);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.expiration(exp);
        }
        return builder.compact();
    }

    public static SecretKey generalKey(String k) {
        byte[] encodedKey = Base64.getDecoder().decode(k);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
