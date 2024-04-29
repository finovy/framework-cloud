package tech.finovy.framework.token.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EncryConfiguration implements Serializable {
    private String secret;
    private String iv;
    private String errMsg;
}
