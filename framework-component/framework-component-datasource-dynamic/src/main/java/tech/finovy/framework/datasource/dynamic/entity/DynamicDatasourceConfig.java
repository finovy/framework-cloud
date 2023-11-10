package tech.finovy.framework.datasource.dynamic.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DynamicDatasourceConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 1722312337077290168L;
    private String key;
    private String url;
    private String username;
    private String password;
    private boolean asyncInit = false;
    private boolean poolPreparedStatements = true;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{key=");
        sb.append(key)
                .append(",encrypt=")
                .append(",username=")
                .append(username)
                .append(",password=")
                .append(password)
                .append(",asyncInit=")
                .append(asyncInit)
                .append(",poolPreparedStatements=")
                .append(poolPreparedStatements);
        return sb.toString();
    }
}
