package tech.finovy.framework.logappender.exception;

/**
 * @author dtype.huang
 */
public final class ErrorCodes {

    public static final String BAD_RESPONSE = "BadResponse";
    public static final String ENDPOINT_INVALID = "EndpointInvalid";
    public static final String ENCODING_EXCEPTION = "EncodingException";
    public static final String INVALID_CURSOR = "InvalidCursor";
    public static final String REQUEST_TIMEOUT = "RequestTimeout";

    private ErrorCodes() {
    }

    public static class ClientErrorCode {
        public static final String UNKNOWN = "Unknown";
        public static final String UNKNOWN_HOST = "UnknownHost";
        public static final String CONNECTION_TIMEOUT = "ConnectionTimeout";
        public static final String SOCKET_TIMEOUT = "SocketTimeout";
        public static final String SOCKET_EXCEPTION = "SocketException";
        public static final String CONNECTION_REFUSED = "ConnectionRefused";
        public static final String NONREPEATABLE_REQUEST = "NonRepeatableRequest";
    }

    public static class Errors {
        public static final String PROJECT_CONFIG_NOT_EXIST = "ProjectConfigNotExist";
        public static final String PROJECT_NOT_EXIST = "ProjectNotExist";
        public static final String SIGNATURE_NOT_MATCH = "SignatureNotMatch";
        public static final String MISS_ACCESS_KEY_ID = "MissAccessKeyId";
        public static final String REQUEST_TIME_TOO_SKEWED = "RequestTimeTooSkewed";
        public static final String PRODUCER_EXCEPTION = "ProducerException";
    }

    public static class RetriableErrors {
        public static final String REQUEST_ERROR = "RequestError";
        public static final String UNAUTHORIZED = "Unauthorized";
        public static final String WRITE_QUOTA_EXCEED = "WriteQuotaExceed";
        public static final String SHARD_WRITE_QUOTA_EXCEED = "ShardWriteQuotaExceed";
        public static final String EXCEED_QUOTA = "ExceedQuota";
        public static final String INTERNAL_SERVER_ERROR = "InternalServerError";
        public static final String SERVER_BUSY = "ServerBusy";
        public static final String BAD_RESPONSE = "BadResponse";
        public static final String PROJECT_NOT_EXISTS = "ProjectNotExists";
        public static final String LOGSTORE_NOT_EXISTS = "LogstoreNotExists";
        public static final String SOCKET_TIMEOUT = "SocketTimeout";
        public static final String SIGNATURE_NOT_MATCH = "SignatureNotMatch";
    }
}
