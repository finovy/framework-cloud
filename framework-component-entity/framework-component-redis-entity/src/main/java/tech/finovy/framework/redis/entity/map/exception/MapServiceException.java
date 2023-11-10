package tech.finovy.framework.redis.entity.map.exception;

public class MapServiceException extends RuntimeException{

    private static final long serialVersionUID = 6096968812977522091L;

    public MapServiceException(Throwable cause) {
        super(cause);
    }
    public MapServiceException(String message) {
        super(message);
    }
}
