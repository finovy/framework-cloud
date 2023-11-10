package tech.finovy.framework.elasticsearch.client;


import lombok.Data;

@Data
public class ElasticClientConfig {
    private String[] nodes;
    private String username;
    private String password;
    private int socketTimeout;
}
