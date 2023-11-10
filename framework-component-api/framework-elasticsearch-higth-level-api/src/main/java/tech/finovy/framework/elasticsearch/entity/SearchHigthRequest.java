package tech.finovy.framework.elasticsearch.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SearchHigthRequest implements Serializable {

    private static final long serialVersionUID = -2611321265111024974L;


    /**
     * 动态数据源的key
     */
    @NotNull
    private String clientKey;
    /**
     * 索引名称
     */
    @NotNull
    private String index;

    /**
     * type  es7版本默认_doc
     */
    @Deprecated
    private String type = "_doc";

    /**
     * id
     */
    private String id;

    /**
     * 默认超时时间
     */
    private long timeOut = 1L;

}
