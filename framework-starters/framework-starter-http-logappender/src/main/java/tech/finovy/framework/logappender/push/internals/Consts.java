package tech.finovy.framework.logappender.push.internals;

public class Consts {
    public static final String DEFAULT_API_VESION = "0.6.0";
    public static final String CONST_HEADSIGNATURE_PREFIX = "LOG ";
    public static final String CONST_ACCESS_ID = "AccessKeyId";
    public static final String CONST_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String CONST_GZIP_ENCODING = "deflate";
    public static final String CONST_CONTENT_TYPE = "Content-Type";
    public static final String CONST_PROTO_BUF = "application/x-protobuf";
    public static final String CONST_CONTENT_LENGTH = "Content-Length";
    public static final String CONST_CONTENT_MD5 = "Content-MD5";
    public static final String CONST_AUTHORIZATION = "Authorization";
    public static final String CONST_SLS_JSON = "application/json";
    public static final String CONST_X_SLS_APIVERSION = "x-log-apiversion";
    public static final String CONST_X_SLS_COMPRESSTYPE = "x-log-compresstype";
    public static final String CONST_X_SLS_BODYRAWSIZE = "x-log-bodyrawsize";
    public static final String CONST_X_SLS_SIGNATUREMETHOD = "x-log-signaturemethod";
    public static final String CONST_X_SLS_REQUESTID = "x-log-requestid";
    public static final String CONST_X_SLS_HOSTIP = "x-log-hostip";
    public static final String CONST_X_SLS_IP = "x-log-ip";
    public static final String CONST_X_SLS_SSL = "x-log-ssl";
    public static final String CONST_X_SLS_PREFIX = "x-log-";
    public static final String CONST_X_ACS_PREFIX = "x-acs-";
    public static final String CONST_X_SLS_CURSOR = "x-log-cursor";
    public static final String CONST_X_SLS_COUNT = "x-log-count";
    public static final String CONST_X_SLS_PROCESS = "x-log-progress";
    public static final String CONST_X_SLS_NEXT_TOKEN = "x-log-nexttoken";
    public static final String CONST_X_SLS_CONTENTENCODING = "x-log-contentencoding";
    public static final String CONST_X_ACS_SECURITY_TOKEN = "x-acs-security-token";
    public static final String CONST_X_LOG_RESOURCEOWNERACCOUNT = "x-log-resourceowneraccount";
    public static final String CONST_X_LOG_AGGQUERY = "x-log-agg-query";
    public static final String CONST_X_LOG_WHEREQUERY = "x-log-where-query";
    public static final String CONST_X_LOG_HASSQL = "x-log-has-sql";
    public static final String CONST_X_LOG_PROCESSEDROWS = "x-log-processed-rows";
    public static final String CONST_X_LOG_ELAPSEDMILLISECOND = "x-log-elapsed-millisecond";
    public static final String CONST_X_LOG_QUERY_INFO = "x-log-query-info";
    public static final String CONST_HOST = "Host";
    public static final String CONST_DATE = "Date";
    public static final String CONST_USER_AGENT = "User-Agent";
    public static final String CONST_ROUTE_KEY = "key";
    public static final String CONST_ERROR_CODE = "errorCode";
    public static final String CONST_ERROR_MESSAGE = "errorMessage";
    public static final String CONST_MD5 = "MD5";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String HMAC_SHA1 = "hmac-sha1";
    public static final String HMAC_SHA1_JAVA = "HmacSHA1";
    public static final String CONST_LOCAL_IP = "127.0.0.1";
    public static final int CONST_MAX_PUT_SIZE = 50 * 1024 * 1024;
    public static final int CONST_MAX_PUT_LINES = 40960;
    public static final int CONST_UN_AUTHORIZATION_CODE = 401;
    public static final int CONST_HTTP_OK = 200;
    public static final int HTTP_CONNECT_MAX_COUNT = 1000;
    public static final int HTTP_CONNECT_TIME_OUT = 5 * 1000;
    public static final int HTTP_SEND_TIME_OUT = 60 * 1000;
    public static final String CONST_GZIP = "gzip";
    public static final String CONST_LZ4 = "lz4";
    public static final String CONST_HTTP_ACCEPT = "accept";

    public enum CompressType {
        NONE(""),
        LZ4(Consts.CONST_LZ4),
        GZIP(Consts.CONST_GZIP_ENCODING);

        private final String strValue;

        CompressType(String strValue) {
            this.strValue = strValue;
        }

        public static CompressType fromString(final String compressType) {
            for (CompressType type : values()) {
                if (type.strValue.equals(compressType)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("invalid CompressType: " + compressType + ", should be (" + CompressType.NONE + ", " + CompressType.GZIP + ", " + CompressType.LZ4 + ")");
        }

        @Override
        public String toString() {
            return strValue;
        }
    }
}
