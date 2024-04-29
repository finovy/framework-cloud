package tech.finovy.framework.global.interceptor;

public class MybatisInterceptorException extends RuntimeException{
    private static final long serialVersionUID = -4274178617052660168L;
    public MybatisInterceptorException(Throwable cause) {
        super(cause);
    }
    public MybatisInterceptorException(String msg) {
        super(msg);
    }
}
