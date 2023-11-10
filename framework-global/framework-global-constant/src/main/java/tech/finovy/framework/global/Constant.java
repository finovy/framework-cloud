package tech.finovy.framework.global;

public class Constant {
    private Constant() {
    }

    public static final String APPID = "appid";
    public static final String TRACEID = "traceid";
    public static final String X_TRACEID = "x-trace-id";
    public static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2L;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 2L;
    public static final long REFRESH_TOKEN_AUTO_TIME = 1000 * 60 * 10L;
    public static final String JWT_REFRESH_TOKEN_KEY_FORMAT = "JWT_REFRESH_TOKEN_%s";
    public static final String JWT_BLACK_LIST_KEY_FORMAT = "JWT_BLACKLIST_%s";
    public static final String TOKEN_HEADER = "Authorization-token";
    public static final String TOKEN_HEADER_APPID = "X-Auth-Appid";
    public static final String TOKEN_HEADER_TIMESTAMP = "X-Auth-Timestamp";
    public static final String TOKEN_HEADER_NONCE = "X-Auth-Nonce";
    public static final String TOKEN_HEADER_SIGNATURE = "X-Auth-Signature";


}
