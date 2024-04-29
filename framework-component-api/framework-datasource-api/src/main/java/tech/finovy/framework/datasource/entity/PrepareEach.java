package tech.finovy.framework.datasource.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class PrepareEach implements Serializable {
    private static final long serialVersionUID = 615140623085341935L;

    /**
     * 是否需要缓存
     */
    private boolean needCache = false;
    /**
     * 多级查询 本地缓存key
     */
    private String eachKey;
    /**
     * 连接池key
     */
    private String datasourceKey;
    /**
     * 多级查询 依赖的的缓存  与 eachKey 对应
     */
    private String inputKey = null;
    /**
     * sql
     */
    private String prepareSql;

    /**
     * 占位符下标，及值
     */
    private Map<Integer, PrepareDict> index;

    /**
     * 是否存储过程
     */
    private boolean procedure = false;
    /**
     * 是否需要commit 事务处理
     */
    private boolean commit = false;

    public int indexSize() {
        if (index == null) {
            return 0;
        }
        return index.size();
    }

    public PrepareEach addIndex(int ix, PrepareDict dict) {
        if (index == null) {
            index = new HashMap<>();
        }
        index.put(ix, dict);
        return this;
    }

    public PrepareEach() {
    }

    public PrepareEach(String datasourceKey, String prepareSql) {
        this.datasourceKey = datasourceKey;
        this.prepareSql = prepareSql;
    }

    public PrepareEach(String datasourceKey, String prepareSql, boolean commit) {
        this.datasourceKey = datasourceKey;
        this.prepareSql = prepareSql;
        this.commit = commit;
    }
}
