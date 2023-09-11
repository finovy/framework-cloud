package tech.finovy.framework.logappender.exception;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ExceptionFactory {
    private ExceptionFactory() {
    }

    public static ClientException createNetworkException(IOException ex) {
        String requestId = "Unknown";
        String errorCode = ErrorCodes.ClientErrorCode.UNKNOWN;
        if (ex instanceof SocketTimeoutException) {
            errorCode = ErrorCodes.ClientErrorCode.SOCKET_TIMEOUT;
        } else if (ex instanceof SocketException) {
            errorCode = ErrorCodes.ClientErrorCode.SOCKET_EXCEPTION;
        } else if (ex instanceof ConnectTimeoutException) {
            errorCode = ErrorCodes.ClientErrorCode.CONNECTION_TIMEOUT;
        } else if (ex instanceof UnknownHostException) {
            errorCode = ErrorCodes.ClientErrorCode.UNKNOWN_HOST;
        } else if (ex instanceof NoHttpResponseException) {
            errorCode = ErrorCodes.ClientErrorCode.CONNECTION_TIMEOUT;
        } else if (ex instanceof ClientProtocolException) {
            Throwable cause = ex.getCause();
            if (cause instanceof NonRepeatableRequestException) {
                errorCode = ErrorCodes.ClientErrorCode.NONREPEATABLE_REQUEST;
                return new ClientException(cause.getMessage(), errorCode, requestId, cause);
            }
        }
        return new ClientException(ex.getMessage(), errorCode, requestId, ex);
    }
}
