package tech.finovy.framework.base;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;


@Data
@ToString
public class BasePageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 493130157473403773L;
    private String from;
    private String to;
    private Integer page = 1;
    private Integer pagesize = 25;
    private String sqlOrderField;
    private String sqlOrderType;
}






