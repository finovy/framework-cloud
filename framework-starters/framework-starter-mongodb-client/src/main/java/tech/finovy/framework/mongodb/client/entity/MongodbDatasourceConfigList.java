package tech.finovy.framework.mongodb.client.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class MongodbDatasourceConfigList implements Serializable {

    @Serial
    private static final long serialVersionUID = -2942600123649542259L;

    private List<MongoDbClientConfig> config;

}
