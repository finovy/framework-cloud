package tech.finovy.framework.redisson.holder;


public class RedisContextHolder {

    private RedisContextHolder() {
        // deny reflect instance
    }

    private static final RedisContext INSTANCE = new RedisContext();

    public static RedisContext get() {
        return INSTANCE;
    }

}
