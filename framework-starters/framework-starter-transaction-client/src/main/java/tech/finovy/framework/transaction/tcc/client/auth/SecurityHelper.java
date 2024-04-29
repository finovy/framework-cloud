package tech.finovy.framework.transaction.tcc.client.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import tech.finovy.framework.transaction.tcc.client.log.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SecurityHelper {

    final static String KEY_ALGORITHM = "AES";
    final static String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    final static String CHARSET_NAME = "UTF-8";
    final static byte[] iv = {0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38};
    private static final String PATTERN = "%s/_/%s";
    private static final String DEFAULT_KEY = "qItKT4BAkSwlR7q3";
    private static final Random RANDOM = new Random();

    @SneakyThrows
    public static <T> SecurityContent encrypt(String key, T t) {
        key = StringUtils.isEmpty(key) ? DEFAULT_KEY : key;
        final long timestamp = System.currentTimeMillis();
        final long nonce = RANDOM.nextInt(100000);
        final String signature = Md5Crypt.apr1Crypt(String.format(PATTERN, timestamp, nonce).getBytes(CHARSET_NAME), key);
        final String content = encrypt(JSON.toJSONString(t), key);
        return new SecurityContent(content, timestamp, nonce, signature);
    }

    @SneakyThrows
    public static <T> T decrypt(String typeName, String key, SecurityContent content, TypeReference<T> typeReference) {
        key = StringUtils.isEmpty(key) ? DEFAULT_KEY : key;
        // 验签
        if (checkSign(key, content)) {
            String parseContent = content.getContent();
            if (content.isEncrypt()) {
                parseContent = decrypt(content.getContent(), key);
            }
            final HashMap<String, Object> map = JSON.parseObject(parseContent, HashMap.class);
            return JSON.parseObject(JSON.toJSONString(map.get(typeName)), typeReference);
        }
        throw new Exception("sign check error");
    }

    @SneakyThrows
    public static boolean checkSign(String key, SecurityContent content) {
        final long timestamp = content.getTimestamp();
        final long nonce = content.getNonce();
        final String signature = Md5Crypt.apr1Crypt(String.format(PATTERN, timestamp, nonce).getBytes(CHARSET_NAME), key);
        return signature.equals(content.getSignature());
    }

    private static Key getKey(byte[] keyBytes) {
        int base = 16;
        if ((keyBytes.length % base) != 0) {
            int groups = (keyBytes.length / base) + ((keyBytes.length % base) != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * 加密方法
     *
     * @param content 要加密的字符串
     * @param key     加密密钥
     * @return
     */
    private static String encrypt(String content, String key) {
        try {
            byte[] enc = encrypt(content.getBytes(CHARSET_NAME), key.getBytes(CHARSET_NAME));
            String base64Str = Base64.encodeBase64String(enc);
            return new String(base64Str.getBytes(), CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            Log.error("encrypt exception:{} ", e.getMessage(), e);
        }
        return null;
    }

    private static byte[] encrypt(byte[] content, byte[] keyBytes) {
        byte[] encryptedText = null;

        try {
            Key key = getKey(keyBytes);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(content);
        } catch (Exception e) {
            Log.error("encrypt exception:{} ", e.getMessage(), e);
        }
        return encryptedText;
    }

    public static String decrypt(String encryptedData, String key) {
        try {
            byte[] enc = encryptedData.getBytes(CHARSET_NAME);
            enc = Base64.decodeBase64(enc);
            byte[] dec = decrypt(enc, key.getBytes(CHARSET_NAME));
            return new String(dec, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            Log.error("decrypt exception:{} ", e.getMessage(), e);
        }

        return null;
    }

    private static byte[] decrypt(byte[] encryptedData, byte[] keyBytes) {
        byte[] encryptedText = null;

        try {
            Key key = getKey(keyBytes);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            Log.error("decrypt exception:{} ", e.getMessage(), e);
        }

        return encryptedText;
    }

}
