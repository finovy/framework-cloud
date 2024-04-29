package tech.finovy.framework.security.auth.core.token.jwt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @program: framework-cloud
 * @description:
 * @author: Zachary.Peng
 * @date: 2024/4/10 11:25
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestJwt.class)
@RunWith(SpringRunner.class)
public class TestJwt {

    //keytool -genkeypair -alias batman -keyalg RSA -keypass batman -keystore batman.keystore -storepass batmanstore
    //创建jwt令牌
    @Test
    public void testCreateJwt() {
        //密钥库文件
        String keystore = "default.jks";
        //密钥库的密码
        String keystore_password = "123456";
        //密钥别名
        String alias = "felordcn";
        KeyPairFactory keyPairFactory = new KeyPairFactory();
        KeyPair keyPair = keyPairFactory.create(keystore, alias, keystore_password);
        //获取私钥
//        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();



        JwtPayloadBuilder jwtPayloadBuilder = new JwtPayloadBuilder();
        //jwt令牌的内容
        String payload = jwtPayloadBuilder
                .iss("felord.cn")
                .sub("all")
                .aud("admin@test.cn")
                .additional(null)
                .roles(new HashSet<>())
                .expDays(30)
                .builder();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RsaSigner signer = new RsaSigner(privateKey);
        String encoded = JwtHelper.encode(payload, signer).getEncoded();
        System.out.println(encoded);

    }

    //校验jwt令牌
    @Test
    public void testVerify() {

        //密钥库文件
        String keystore = "default.jks";
        //密钥库的密码
        String keystore_password = "123456";
        //密钥别名
        String alias = "felordcn";
        //密钥工厂
        //密钥对（公钥和私钥）
        KeyPairFactory keyPairFactory = new KeyPairFactory();
        KeyPair keyPair = keyPairFactory.create(keystore, alias, keystore_password);
        //jwt令牌
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbGwiLCJhdWQiOiJhZG1pbkBmaW5vdnkuY24iLCJyb2xlcyI6IltdIiwiaXNzIjoiZmVsb3JkLmNuIiwiZXhwIjoiMjAyNC0wNS0xMCAxMTo1MjowOCIsImlhdCI6IjIwMjQtMDQtMTAgMTE6NTI6MDgiLCJqdGkiOiI3NGJlNDk4NTAxOTk0MDc1OTExNTcxMGFjZDhkODA4YyJ9.OLsv7S-t7pJB2iHyAcDTQG_cvgzEXfx0H0WE8UrWXH8Fa2CwgGO_imtWxbDXp8_eWs_atmEGTVIWK6bsxm-ZY72rlJl4IJVgpkEuIrVz6BUl6_bPcIXwtXvqeMb2y7F11f_aGPxfYLG3JpWAfIoRTLDKPqFV3hv0-OsiGxWSLCc";

        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        SignatureVerifier rsaVerifier = new RsaVerifier(rsaPublicKey);
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, rsaVerifier);
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
