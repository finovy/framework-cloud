package tech.finovy.framework.oss.client.config;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class OssClientConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = -2942611622642242259L;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private List<OssClientEntity> config;
}
