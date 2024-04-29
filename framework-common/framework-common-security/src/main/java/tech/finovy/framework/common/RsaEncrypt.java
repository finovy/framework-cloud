package tech.finovy.framework.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


@Slf4j
public class RsaEncrypt {
    private static final String ALGORITHM_FOR_KEY ="RSA";
    private static final String ALGORITHM ="RSA/ECB/PKCS1Padding";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String PRIVATE_KEY_NULL="Input PrivateKey Is Null";
    private static final String PUBLIC_KEY_NULL="Input PublicKey Is Null";
    private static final String INPUT_DATA_NULL="Input Data Is Null";

    public EncryptKeyPair createKeyPair(int keysize){
        return createKeyPair(keysize,false);
    }
    public EncryptKeyPair createUrlSafeKeyPair(int keysize){
        return createKeyPair(keysize,true);
    }

    private EncryptKeyPair createKeyPair(int keysize, boolean urlSafe) {
        EncryptKeyPair kp = new EncryptKeyPair();
        try {
            //产生钥匙对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_FOR_KEY, ProviderInstance.getInstance());
            keyPairGenerator.initialize(keysize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            kp.setPrivateKey(privateKey);
            kp.setPublicKey(publicKey);
            byte[] privateKeyBytes = privateKey.getEncoded();
            byte[] publicKeyBytes = publicKey.getEncoded();
            if (urlSafe) {
                kp.setPrivateKeyString(Base64.encodeBase64URLSafeString(privateKeyBytes));
                kp.setPublicKeyString(Base64.encodeBase64URLSafeString(publicKeyBytes));
                return kp;
            }
            kp.setPrivateKeyString(Base64.encodeBase64String(privateKeyBytes));
            kp.setPublicKeyString(Base64.encodeBase64String(publicKeyBytes));
            return kp;
        } catch (NoSuchAlgorithmException e) {
            kp.setErrorMsg(e.toString());
            log.error(e.toString());
            return kp;
        }
    }


    public  boolean privateEncrypt(RSAPrivateKey  privateKey, RsaData rsaData) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {

        if (privateKey == null) {
            rsaData.setErrorMsg(PRIVATE_KEY_NULL);
            return false;
        }
            Cipher cipher = Cipher.getInstance(ALGORITHM,ProviderInstance.getInstance());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            int blo=cipher.getBlockSize();
            byte[] data= rsaData.getData().getBytes(StandardCharsets.UTF_8);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > blo) {
                    cache = cipher.doFinal(data, offSet, blo);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * blo;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            rsaData.setData(Base64.encodeBase64URLSafeString(encryptedData));
            return true;
    }

    private boolean checkPrivateKey(String privateKey,RsaData rsaData){
        if (rsaData == null) {
            return false;
        }
        if (privateKey == null) {
            rsaData.setErrorMsg(PRIVATE_KEY_NULL);
            return false;
        }
        if (rsaData.getData() == null) {
            rsaData.setErrorMsg(INPUT_DATA_NULL);
            return false;
        }
        return true;
    }

    public  boolean privateEncrypt(String privateKey, RsaData rsaData){
        if (!checkPrivateKey(privateKey,rsaData)){
            return false;
        }

        try {
            RSAPrivateKey  priKey = getPrivateKey(privateKey);
            return privateEncrypt(priKey, rsaData);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException | IOException e) {
            rsaData.setErrorMsg(e.toString());
            log.error(e.toString());
        }
        return false;
    }

    public  boolean privateDecrypt(RSAPrivateKey  privateKey, RsaData rsaData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        if (privateKey == null) {
            rsaData.setErrorMsg(PRIVATE_KEY_NULL);
            return false;
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM,ProviderInstance.getInstance());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte [] encryptedData = Base64.decodeBase64(rsaData.getData());
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int blo=cipher.getBlockSize();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > blo) {
                    cache = cipher.doFinal(encryptedData, offSet, blo);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * blo;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            rsaData.setErrorMsg(null);
            rsaData.setData(new String(decryptedData,StandardCharsets.UTF_8));
            return true;
    }
    public  boolean privateDecrypt(String privateKey, RsaData rsaData){

        if (privateKey == null) {
            rsaData.setErrorMsg(PRIVATE_KEY_NULL);
            return false;
        }
        try {
            RSAPrivateKey priKey =getPrivateKey(privateKey);
            return privateDecrypt(priKey, rsaData);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
            rsaData.setErrorMsg(e.toString());
            log.error(e.toString());
        }
        return false;
    }

    private boolean checkPublicKey(String publicKey, RsaData rsaData){
        if(rsaData==null) {
            return false;
        }
        if(publicKey==null){
            rsaData.setErrorMsg(PUBLIC_KEY_NULL);
            return false;
        }
        if(rsaData.getData()==null) {
            rsaData.setErrorMsg(INPUT_DATA_NULL);
            return false;
        }
        return true;
    }

    public  boolean publicEncrypt(RSAPublicKey publicKey, RsaData rsaData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        if(publicKey==null){
            rsaData.setErrorMsg(PUBLIC_KEY_NULL);
            return false;
        }
            Cipher cipher = Cipher.getInstance(ALGORITHM,ProviderInstance.getInstance());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int blo=cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte [] raw = rsaData.getData().getBytes(StandardCharsets.UTF_8);
            int inputLen = raw.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > blo) {
                    cache = cipher.doFinal(raw, offSet, blo);
                } else {
                    cache = cipher.doFinal(raw, offSet, inputLen - offSet);
                }
                bout.write(cache, 0, cache.length);
                i++;
                offSet = i * blo;
            }
            byte[] encryptedData = bout.toByteArray();
            bout.close();
            rsaData.setErrorMsg(null);
            rsaData.setData(Base64.encodeBase64URLSafeString(encryptedData));
            return true;
    }

    public  boolean publicEncrypt(String publicKey, RsaData rsaData){
        if(!checkPublicKey(publicKey,rsaData)) {
            return false;
        }
        try {
            RSAPublicKey pubKey =getRSAPublicKey(publicKey);
           return publicEncrypt(pubKey, rsaData);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
        	log.error(e.toString());
            rsaData.setErrorMsg(e.toString());
        }
        return false;
    }

    private RSAPublicKey getRSAPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_FOR_KEY,ProviderInstance.getInstance());
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_FOR_KEY,ProviderInstance.getInstance());
        return(RSAPrivateKey ) keyFactory.generatePrivate(keySpec);
    }

    public  boolean publicDecrypt(RSAPublicKey publicKey, RsaData rsaData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
            if(publicKey==null) {
                return false;
            }
            Cipher cipher = Cipher.getInstance(ALGORITHM,ProviderInstance.getInstance());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            int blo=cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte [] encryptedData = Base64.decodeBase64(rsaData.getData());
            int inputLen = encryptedData.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet >= blo) {
                    cache = cipher.doFinal(encryptedData, offSet, blo);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                bout.write(cache, 0, cache.length);
                i++;
                offSet = i * blo;
            }
            byte[] decryptedData = bout.toByteArray();
            rsaData.setErrorMsg(null);
            rsaData.setData(new String(decryptedData,StandardCharsets.UTF_8));
            bout.close();
            return true;
    }

    public  boolean publicDecrypt(String publicKey, RsaData rsaData){
        if(publicKey==null){
            rsaData.setErrorMsg(PUBLIC_KEY_NULL);
            return false;
        }
        try {
            RSAPublicKey pubKey =getRSAPublicKey(publicKey);
           return publicDecrypt(pubKey,  rsaData);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
        	log.error(e.toString());
            rsaData.setErrorMsg(e.toString());
        }
        return false;
    }
    public String sign(String requestData, String privateKey){
        try {
            RSAPrivateKey priKey =getPrivateKey(privateKey);
          return  sign(requestData, priKey);
        } catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
            log.error(e.toString());
        }
        return null;
    }

    public String sign(String requestData, PrivateKey privateKey){
        try {
            Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM,ProviderInstance.getInstance());
            sign.initSign(privateKey);
            sign.update(requestData.getBytes());
            byte[] signed = sign.sign();
            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            log.warn(e.toString());
        }
        return null;
    }

    public boolean verifySign(String requestData, String signature, String publicKey ) {
        try {
            RSAPublicKey pubKey = getRSAPublicKey(publicKey);
            return verifySign(requestData, signature, pubKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | SignatureException | InvalidKeyException e) {
            log.warn(e.toString());
        }
        return false;
    }

    public boolean verifySign(String requestData, String signature, PublicKey publicKey ) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM,ProviderInstance.getInstance());
            verifySign.initVerify(publicKey);
            verifySign.update(requestData.getBytes());
            return verifySign.verify(Base64.decodeBase64(signature));
    }
}
