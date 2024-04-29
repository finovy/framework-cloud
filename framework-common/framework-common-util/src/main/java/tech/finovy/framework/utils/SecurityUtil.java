package tech.finovy.framework.utils;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DESede;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Slf4j
public class SecurityUtil {
    public static final String PASSWORD_KEY = "IjMxUCMBH6OxJZ6q";
    public static final String PASSWORD_IV = "01234567";
    public static final String KEY = "7sANGR8ypf_aQKkvRikFig";
    public static final String IV = "IB9N75V82Q0KJ3BK";
    public static final String DEFAULT_ENC_NAME = "UTF-8";
    private static final String ALGORITHM_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_NOPADDING = "AES/CBC/NoPadding";
    private static final byte[] PLUS_BYTE = {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};


    public static String javaOpensslEncrypt(String data) {
        return javaOpensslEncrypt(data, IV);
    }

    public static String javaOpensslEncrypt(String data, String iv) {
        try {
            Cipher cipher = createCipher(iv, Cipher.ENCRYPT_MODE);
            return URLEncoder.encode(Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes())), DEFAULT_ENC_NAME);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    public static String javaOpensslDecrypt(String data) {
        return javaOpensslDecrypt(data, IV);
    }

    /**
     * java_openssl_decrypt解密
     */
    public static String javaOpensslDecrypt(String data, String iv) {
        try {
            Cipher cipher = createCipher(iv, Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(Base64.getDecoder().decode(URLDecoder.decode(data, DEFAULT_ENC_NAME))));
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    private static Cipher createCipher(String iv, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] key = KEY.getBytes();
        Cipher cipher = Cipher.getInstance(ALGORITHM_PKCS5PADDING);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(mode, new SecretKeySpec(key, "AES"), ivParameterSpec);
        return cipher;
    }


    public static String javaOpensslEncryptNoPadding(String data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NOPADDING);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(padd(key, 32), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            String ret = Base64.getEncoder().encodeToString(encrypted);
            return saveBase64encode(ret);
        } catch (Exception e) {
            log.debug("{}", e.getMessage(), e);
            return null;
        }
    }

    public static String javaOpensslDecryptNoPadding(String data, String key) {
        data = SecurityUtil.saveBase64Decrypt(data);
        try {
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
            byte[] encrypted1 = Base64.getDecoder().decode(data);
            Cipher cipher = Cipher.getInstance(ALGORITHM_NOPADDING);
            SecretKeySpec keyspec = new SecretKeySpec(padd(key, 32), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(padd(IV, 16));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception e) {
            log.debug("{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据key转换为数组,长度不足补0
     */
    private static byte[] padd(String key, int length) {
        int plus = length - key.length();
        byte[] data = key.getBytes(StandardCharsets.UTF_8);
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            if (data.length > i) {
                raw[i] = data[i];
            } else {
                raw[i] = PLUS_BYTE[plus];
            }
        }
        return raw;
    }

    private static byte[] padd(String key) {
        int plus = 24 - key.length();
        byte[] data = key.getBytes(StandardCharsets.UTF_8);
        byte[] raw = new byte[24];
        for (int i = 0; i < 24; i++) {
            if (data.length > i) {
                raw[i] = data[i];
            } else {
                raw[i] = PLUS_BYTE[plus - 1];
            }
        }
        return raw;
    }

    public static String saveBase64Decrypt(String v) {
        String replace = v.replace("-", "+").replace("_", "/");
        int i = 4 - replace.length() % 4;
        if (1 > replace.length() % 4) {
            return replace;
        }
        String aa = "====";
        return replace + aa.substring(0, i);
    }

    public static String saveBase64encode(String v) {
        String replace = v.replace("+", "-").replace("/", "_").replace("\n", "").replace("\r", "");
        for (int j = 0; j < 16; j++) {
            int i = replace.lastIndexOf("=");
            if (i != replace.length() - 1) {
                break;
            }
            replace = replace.substring(0, replace.length() - 1);
        }
        return replace;
    }

    public static String keyEncode(String content, String key) {
        DESede deSede = new DESede(Mode.CBC, Padding.PKCS5Padding, padd(key), PASSWORD_IV.getBytes());
        return deSede.encryptBase64(content);
    }

    public static String keyEncode(String content) {
        return keyEncode(content, PASSWORD_KEY);
    }

    public static String keyDecode(String content, String key) {
        DESede deSede = new DESede(Mode.CBC, Padding.PKCS5Padding, padd(key), PASSWORD_IV.getBytes());
        return deSede.decryptStr(content);
    }

    public static String keyDecode(String content) {
        return keyDecode(content, PASSWORD_KEY);
    }

}
