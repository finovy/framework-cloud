package tech.finovy.framework.oss.client.config;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class OssClientEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1721112111177290168L;
    private boolean encrypt;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String key;
}
