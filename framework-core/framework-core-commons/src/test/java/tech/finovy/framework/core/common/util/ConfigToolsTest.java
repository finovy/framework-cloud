package tech.finovy.framework.core.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

public class ConfigToolsTest {

    @Test
    public void test() throws Exception {
        KeyPair keyPair = ConfigTools.getKeyPair();
        String publicKeyStr = ConfigTools.getPublicKey(keyPair);
        String privateKeyStr = ConfigTools.getPrivateKey(keyPair);
        System.out.println("publicKeyStr:" + publicKeyStr);
        System.out.println("privateKeyStr:" + privateKeyStr);
        String password = "123456";
        String byte2Base64 = ConfigTools.privateEncrypt(password, privateKeyStr);
        System.out.println("byte2Base64：" + byte2Base64);
        String pw = ConfigTools.publicDecrypt(byte2Base64, publicKeyStr);
        Assertions.assertEquals(pw, password);
    }

}
