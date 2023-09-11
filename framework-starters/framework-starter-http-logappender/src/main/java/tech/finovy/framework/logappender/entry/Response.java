package tech.finovy.framework.logappender.entry;

import tech.finovy.framework.logappender.push.internals.Consts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    private static final long serialVersionUID = -7657447336920614920L;
    private Map<String, String> mHeaders = new HashMap<>();

    public Response(Map<String, String> headers) {
        setAllHeaders(headers);
    }

    public String getRequestId() {
        return getHeader(Consts.CONST_X_SLS_REQUESTID);
    }

    public String getHeader(String key) {
        if (mHeaders.containsKey(key)) {
            return mHeaders.get(key);
        } else {
            return "";
        }
    }

    public Map<String, String> getAllHeaders() {
        return mHeaders;
    }

    private void setAllHeaders(Map<String, String> headers) {
        mHeaders = new HashMap<>(headers);
    }
}
