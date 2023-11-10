package tech.finovy.framework.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SecurityEncryption {
    private static final byte[] PLUS_BYTE = {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CTR_ALGORITHM = "AES/CTR/NoPadding";
    private static final String CFB_ALGORITHM = "AES/CFB/NoPadding";
    private static final String ALGORITHM_NOPADDING = "AES/CBC/NoPadding";

    private SecurityEncryption() {
    }

    public static byte[]  encrypt(byte[] text, byte[] secret, byte[] iv) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec ivSpec = new IvParameterSpec(padd(iv,16));
        SecretKeySpec secretKey = new SecretKeySpec(padd(secret,32), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM,ProviderInstance.getInstance());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        return cipher.doFinal(text);
    }

    public static String ctrEncrypt(byte[] text, byte[] secret, byte[] iv) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec ivSpec = new IvParameterSpec(padd(iv,16));
        SecretKeySpec secretKey = new SecretKeySpec(padd(secret,32), "AES");
        Cipher cipher = Cipher.getInstance(CTR_ALGORITHM,ProviderInstance.getInstance());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        return byteToHex(cipher.doFinal(text));
    }

    public static String cfbEncrypt(byte[] text, byte[] secret, byte[] iv) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(padd(secret,32), "AES");
        IvParameterSpec paramSpec = new IvParameterSpec(padd(iv,16));
        Cipher ecipher;
        ecipher = Cipher.getInstance(CFB_ALGORITHM);
        ecipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
        byte[] result = ecipher.doFinal(text);
        return byteToHex(result);
    }

    public static String encrypt(String strToEncrypt, String secret, String iv) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return Base64.encodeBase64String(encrypt(strToEncrypt.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8),iv.getBytes(StandardCharsets.UTF_8)));
    }

    public static byte[] javaOpensslEncryptNoPadding(byte[] dataBytes, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NOPADDING,ProviderInstance.getInstance());
            int blockSize = cipher.getBlockSize();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(padd(key,32), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            log.error(e.toString());
            return new byte[0];
        }
    }

    public static byte[] javaOpensslDecryptNoPadding(byte[] dataBytes, String key, String iv) {
        try
        {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NOPADDING,ProviderInstance.getInstance());
            SecretKeySpec keyspec = new SecretKeySpec(padd(key,32), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(padd(iv,16));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            return cipher.doFinal(dataBytes);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static String encrypt(String strToEncrypt, String secret) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeyException, BadPaddingException {
        byte[] strBtye=strToEncrypt.getBytes(StandardCharsets.UTF_8);
        String ivStr= RandomStringUtils.randomAlphanumeric(16);
        byte[] iv=padd(ivStr,16);
        byte[] ecodeByte=encrypt(strBtye,padd(secret,32),iv);
        byte[] cByte=new byte[16+ecodeByte.length];
        System.arraycopy(iv,0,cByte,0,16);
        System.arraycopy(ecodeByte,0,cByte,16,ecodeByte.length);
        return Base64.encodeBase64String(cByte);
    }
    public static byte[] decrypt(byte[] strToDecrypt, String secret, String iv) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec ivspec = new IvParameterSpec(padd(iv,16));
        SecretKeySpec secretKey = new SecretKeySpec(padd(secret,32), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM,ProviderInstance.getInstance());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return cipher.doFinal(strToDecrypt);
    }

    public static String decrypt(byte[] strToDecrypt, byte[] secret, byte[] iv) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM,ProviderInstance.getInstance());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(strToDecrypt));
    }

    public static String ctrDecrypt(String strToDecrypt, byte[] secret, byte[] iv) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
        Cipher cipher = Cipher.getInstance(CTR_ALGORITHM,ProviderInstance.getInstance());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(hexToByte(strToDecrypt)));
    }

    public static String cfbDecrypt(String strToDecrypt, byte[] secret, byte[] iv) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
        Cipher cipher = Cipher.getInstance(CFB_ALGORITHM,ProviderInstance.getInstance());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(hexToByte(strToDecrypt)));
    }

    public static String decrypt(String strToDecrypt, String secret, String iv) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        byte[] enByte=Base64.decodeBase64(strToDecrypt);
        return  decrypt(enByte,padd(secret,32),padd(iv,16));
    }

    public static String decrypt(String strToDecrypt, String secret) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        byte[] enByte=Base64.decodeBase64(strToDecrypt);
        byte[] srcByte=new byte[enByte.length-16];
        byte[] iv=new byte[16];
        System.arraycopy(enByte,0,iv,0,16);
        System.arraycopy(enByte,16,srcByte,0,enByte.length-16);
        return  decrypt(srcByte,padd(secret,32),iv);
    }

    /** 根据key转换为数组,长度不足补0
     * @param key
     * @param length
     * @return byte[]
     */
    public static byte[] padd(byte[] key,int length)  {
        int plus = length - key.length;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            if (key.length > i) {
                raw[i] = key[i];
            } else {
                raw[i] = PLUS_BYTE[plus];
            }
        }
        return raw;
    }

    public static byte[] padd(String key,int length)  {
        byte[] data = key.getBytes(StandardCharsets.UTF_8);
        return padd(data,length) ;
    }

    /**
     * byte数组转hex
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes){
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }

    /**
     * hex转byte数组
     * @param hex
     * @return
     */
    public static byte[] hexToByte(String hex){
        int m = 0;
        int n = 0;
        int byteLen = hex.length() / 2; // 每两个字符描述一个字节
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = (byte) intVal;
        }
        return ret;
    }
}
