package tech.finovy.framework.security.api;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SecurityService {

    /**加密银行卡,用户地址,用户身份
     * @param strToEncrypt 明文
     * @param secret 加密秘钥
     * @param iv 传入null时调用系统默认值
     * @return 加密后密文
     */
    String encrypt(String strToEncrypt, String secret, String iv);

    /**解密银行卡,用户地址,用户身份
     * @param strToDecrypt 密文
     * @param secret 解密秘钥
     * @param iv 传入null时调用系统默认值
     * @return 解密后明文
     */
    String decrypt(String strToDecrypt, String secret, String iv);
    String decrypt(byte[] strToDecrypt, byte[] secret, byte[] iv) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException;
    /** 加密邮箱,手机号
     * @param strToEncrypt 明文
     * @param secret 秘钥
     * @return 加密后密文
     */
    String encrypt(String strToEncrypt, String secret);

    /** 解密邮箱,手机号
     * @param strToDecrypt 密文
     * @param secret 秘钥
     * @return 解密后明文
     */
    String decrypt(String strToDecrypt, String secret);
}
