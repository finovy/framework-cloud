package tech.finovy.framework.security.auth.core.po;

/**
 * @Author: Ryan Luo
 * @Date: 2024/1/31 11:06
 */
public class TokenPackage {

    private int code;

    private String message;

    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
