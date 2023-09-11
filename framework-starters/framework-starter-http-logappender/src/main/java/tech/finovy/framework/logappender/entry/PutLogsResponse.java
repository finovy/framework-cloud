package tech.finovy.framework.logappender.entry;

import java.util.Map;

public class PutLogsResponse extends Response {
    private static final long serialVersionUID = -104644485399134781L;

    public PutLogsResponse(Map<String, String> headers) {
        super(headers);
    }
}
