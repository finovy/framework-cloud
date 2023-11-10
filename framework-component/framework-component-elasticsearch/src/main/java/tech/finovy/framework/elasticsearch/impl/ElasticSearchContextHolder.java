package tech.finovy.framework.elasticsearch.impl;

public class ElasticSearchContextHolder {

    private ElasticSearchContextHolder() {
        // deny reflect instance
    }

    private static final ElasticSearchContext INSTANCE = new ElasticSearchContext();

    public static ElasticSearchContext get() {
        return INSTANCE;
    }
}
