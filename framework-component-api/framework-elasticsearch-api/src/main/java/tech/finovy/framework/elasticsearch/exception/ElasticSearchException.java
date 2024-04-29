package tech.finovy.framework.elasticsearch.exception;

public class ElasticSearchException extends RuntimeException{

    private static final long serialVersionUID = 5581518920384256546L;

    public ElasticSearchException(Throwable cause) {
        super(cause);
    }
}
