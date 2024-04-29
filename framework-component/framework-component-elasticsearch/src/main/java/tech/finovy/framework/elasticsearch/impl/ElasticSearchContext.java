package tech.finovy.framework.elasticsearch.impl;

import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.client.RestClient;

@Getter
@Setter
public class ElasticSearchContext {

    private RestClient client;

}
