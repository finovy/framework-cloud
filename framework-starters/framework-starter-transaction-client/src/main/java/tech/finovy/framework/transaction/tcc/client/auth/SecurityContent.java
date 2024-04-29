package tech.finovy.framework.transaction.tcc.client.auth;

import java.io.Serial;
import java.io.Serializable;

public class SecurityContent implements Serializable {

    @Serial
    private static final long serialVersionUID = -783713861408373857L;
    private String content;

    private long timestamp;

    private long nonce;

    private String signature;

    private boolean encrypt;

    public SecurityContent() {
    }

    public SecurityContent(String content, long timestamp, long nonce, String signature) {
        this.content = content;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.signature = signature;
        this.encrypt = true;
    }

    public SecurityContent(String content, long timestamp, long nonce, String signature, boolean encrypt) {
        this.content = content;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.signature = signature;
        this.encrypt = encrypt;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
