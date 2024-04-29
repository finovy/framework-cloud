package tech.finovy.framework.token.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccessToken implements Serializable {
    private static final long serialVersionUID = -6544919663947204138L;
    private String token;
    private int expire;
    private long expireAt;
}
