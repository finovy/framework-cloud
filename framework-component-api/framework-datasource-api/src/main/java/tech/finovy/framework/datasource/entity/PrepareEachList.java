package tech.finovy.framework.datasource.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class PrepareEachList implements Serializable {
    @Serial
    private static final long serialVersionUID = 6141401124085241935L;

    private List<PrepareEach> prepareEaches;


}
