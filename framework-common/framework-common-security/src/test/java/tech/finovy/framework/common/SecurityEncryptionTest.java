package tech.finovy.framework.common;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Slf4j
public class SecurityEncryptionTest {

    private static final String secret = "WtuFIq2gOLhC972mH7xBC4mB4zjg9dUi";
    private static final String iv = "IB9N75V82Q0KJ3BK";
    private static final String text = "I'm fine";
    private static final int keysize = 1024;

    @Test
    void testAesIvEncryption() throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        String encryText = SecurityEncryption.encrypt(text, secret, iv);
        log.info("encry:{}", encryText);
        String textDec = SecurityEncryption.decrypt(encryText, secret, iv);
        log.info("textDec:{}", textDec);
        Assertions.assertEquals(text, textDec);
    }

    @Test
    void testAesEncryption() throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        String encryptText = SecurityEncryption.encrypt(text, secret);
        log.info("encrypt:{}", encryptText);
        String textDec = SecurityEncryption.decrypt(encryptText, secret);
        log.info("decrypt:{}", textDec);
        Assertions.assertEquals(text, textDec);
    }

    @Test
    void testAesEncryptionCtr() throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        String encryptText = SecurityEncryption.ctrEncrypt(text.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8), iv.getBytes(StandardCharsets.UTF_8));
        log.info("ctrEncrypt:{}", encryptText);
        String textDec = SecurityEncryption.ctrDecrypt(encryptText, secret.getBytes(StandardCharsets.UTF_8), iv.getBytes(StandardCharsets.UTF_8));
        log.info("decrypt:{}", textDec);
        Assertions.assertEquals(text, textDec);
    }
    @Test
    void testAesEncryptionCfb() throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        String encryptText = SecurityEncryption.cfbEncrypt(text.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8), iv.getBytes(StandardCharsets.UTF_8));
        log.info("ctrEncrypt:{}", encryptText);
        String textDec = SecurityEncryption.cfbDecrypt(encryptText, secret.getBytes(StandardCharsets.UTF_8), iv.getBytes(StandardCharsets.UTF_8));
        log.info("decrypt:{}", textDec);
        Assertions.assertEquals(text, textDec);
    }

    @Test
    void testAesEncryptionPt() throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        byte[] encryText = SecurityEncryption.encrypt(text.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8), iv.getBytes(StandardCharsets.UTF_8));
        log.info("ctrEncrypt:{}", encryText);
        String textDec = SecurityEncryption.decrypt(encryText, secret.getBytes(StandardCharsets.UTF_8), iv.getBytes(StandardCharsets.UTF_8));
        log.info("decrypt:{}", textDec);
        Assertions.assertEquals(text, textDec);
        byte[] deTxt = SecurityEncryption.decrypt(encryText, secret, iv);
        log.info("decrypt:{}", new String(deTxt));
        Assertions.assertEquals(text, textDec);
    }

    @Test
    void testHexToByte() {
        byte[] encryText = SecurityEncryption.hexToByte(DigestUtils.md5Hex(text));
        log.info("ctrEncrypt:{}", encryText);
        String toHext = SecurityEncryption.byteToHex(encryText);
        SecurityEncryption.padd("0", 32);
        Assertions.assertNotNull(toHext);
    }


    @Test
    void javaOpensslEncryptNoPaddingTest() {
        byte[] encryText = SecurityEncryption.javaOpensslEncryptNoPadding(text.getBytes(StandardCharsets.UTF_8), secret, iv);
        byte[] textDec = SecurityEncryption.javaOpensslDecryptNoPadding(encryText, secret, iv);
        String value = new String(textDec);
        log.info("decrypt:{}", value);
        Assertions.assertNotNull(textDec);
    }


    @Test
    void testRsaPublicEncryption() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException {
        RsaEncrypt rsa = new RsaEncrypt();
        EncryptKeyPair pair = rsa.createKeyPair(keysize);
        EncryptKeyPair pairSafe = rsa.createUrlSafeKeyPair(keysize);
        pairSafe.setErrorMsg("testError");
        Assertions.assertNotNull(pair.getPublicKey());
        Assertions.assertNotNull(pair.getPrivateKey());
        log.info("PrivateKeyString:{},{}", pair.getPrivateKeyString(), pair.getCreateDate());
        log.info("PublicKeyString:{},{}", pair.getPublicKeyString(), pair.getCreateDate());
        RsaData rsaData = new RsaData(text);
        rsa.publicEncrypt(pair.getPublicKeyString(), rsaData);
        Assertions.assertNull(rsaData.getErrorMsg());
        log.info("encry:{}", rsaData.getData());
        rsa.privateDecrypt(pair.getPrivateKeyString(), rsaData);
        Assertions.assertNull(rsaData.getErrorMsg());
        log.info("Decrypt:{}", rsaData.getData());
        Assertions.assertEquals(text, rsaData.getData());
        rsaData.setData(null);
        rsaData.setErrorMsg(null);
        rsa.publicEncrypt(pair.getPublicKeyString(), rsaData);
        // error branch
        Assertions.assertFalse(rsa.publicEncrypt("error-test", rsaData));
        Assertions.assertFalse(rsa.privateDecrypt("error-test", rsaData));

        String privateKey = null;
        Assertions.assertFalse(rsa.privateDecrypt(privateKey, rsaData));
        RSAPrivateKey testPrivateKey = null;
        Assertions.assertFalse(rsa.privateDecrypt(testPrivateKey, rsaData));

    }


    @Test
    void testRsaPrivateEncryption() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException {
        RsaEncrypt rsa = new RsaEncrypt();
        EncryptKeyPair pair = rsa.createKeyPair(keysize);
        RsaData rsaData = new RsaData(text);
        RSAPrivateKey key = null;
        Assertions.assertFalse(rsa.privateEncrypt(key, rsaData));
        rsaData.setErrorMsg(null);
        rsa.privateEncrypt(pair.getPrivateKeyString(), null);
        rsa.privateEncrypt(pair.getPrivateKeyString(), rsaData);
        Assertions.assertNull(rsaData.getErrorMsg());
        log.info("encry:{}", rsaData.getData());
        rsa.publicDecrypt(pair.getPublicKeyString(), rsaData);
        Assertions.assertNull(rsaData.getErrorMsg());
        log.info("Decrypt:{}", rsaData.getData());
        Assertions.assertEquals(text, rsaData.getData());
        // error branch
        String publicKey = null;
        Assertions.assertFalse(rsa.publicDecrypt(publicKey, rsaData));
        Assertions.assertFalse(rsa.publicDecrypt("error-test", rsaData));
        RSAPublicKey testKey = null;
        Assertions.assertFalse(rsa.publicDecrypt(testKey, rsaData));
        //
        Assertions.assertFalse(rsa.privateEncrypt("testKey", rsaData));
    }

    @Test
    void testCreateKeyPair() {
        RsaEncrypt rsa = new RsaEncrypt();
        EncryptKeyPair pair = rsa.createKeyPair(keysize);
        Assertions.assertNull(pair.getErrorMsg());
        log.info("PrivateKeyString:{}", pair.getPrivateKeyString());
        log.info("PublicKeyString:{}", pair.getPublicKeyString());
    }


    @Test
    void testCreateKeyPairEncryptTest() {
        RsaEncrypt rsa = new RsaEncrypt();
        EncryptKeyPair pair = rsa.createKeyPair(keysize);
        Assertions.assertNull(pair.getErrorMsg());
        log.info("PrivateKeyString:{}", pair.getPrivateKeyString());
        log.info("PublicKeyString:{}", pair.getPublicKeyString());
    }

    @Test
    void testVerifySign() {
        RsaEncrypt rsa = new RsaEncrypt();
        EncryptKeyPair pair = rsa.createKeyPair(2048);
        Assertions.assertNull(pair.getErrorMsg());
        log.info("PrivateKeyString:{}", pair.getPrivateKeyString());
        log.info("PublicKeyString:{}", pair.getPublicKeyString());
        String a = rsa.sign(text, pair.getPrivateKeyString());
        log.info("sign:{}", pair.getPublicKeyString());
        boolean check = rsa.verifySign(text, a, pair.getPublicKeyString());
        Assertions.assertTrue(check);
        // error branch
        rsa.sign(text, "error-test");
        rsa.sign(text, new PrivateKey() {
            @Override
            public String getAlgorithm() {
                return "ERROR";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }
        });

        boolean checkResult = rsa.verifySign(text, a, "error-test");
        Assertions.assertFalse(checkResult);
    }


    @Test
    void testBranch() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException {
        RsaEncrypt rsa = new RsaEncrypt();
        String key = null;
        final RsaData rsaData = new RsaData();
        Assertions.assertFalse(rsa.privateEncrypt(key, rsaData));
        Assertions.assertFalse(rsa.privateEncrypt("test-key", rsaData));
        Assertions.assertFalse(rsa.publicEncrypt(key, rsaData));
        Assertions.assertFalse(rsa.publicEncrypt("test-key", null));

        RSAPublicKey publicKey = null;
        Assertions.assertFalse(rsa.publicEncrypt(publicKey, rsaData));
    }
}
