package tech.finovy.framework.elasticsearch.client;

import java.io.Serial;

public class ElasticClientConfigurationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4742526183212613158L;

    public ElasticClientConfigurationException(String message) {
        super(message);
    }
}
