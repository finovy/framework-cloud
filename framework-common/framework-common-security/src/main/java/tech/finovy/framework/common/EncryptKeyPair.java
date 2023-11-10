package tech.finovy.framework.common;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class EncryptKeyPair {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String errorMsg=null;
    private String privateKeyString=null;
    private String publicKeyString=null;
    private Date createDate;
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getCreateDate() {
        return createDate;
    }
    public void setPublicKeyString(String pulibcKeyString) {
        this.publicKeyString = pulibcKeyString;
    }

    public void setPrivateKeyString(String privateKeyString) {
        this.privateKeyString = privateKeyString;
    }

    public String getPrivateKeyString() {
        return privateKeyString;
    }

    public String getPublicKeyString() {
        return publicKeyString;
    }
}
