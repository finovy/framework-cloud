package tech.finovy.framework.logappender.push;

import tech.finovy.framework.logappender.entry.PutLogsRequest;
import tech.finovy.framework.logappender.entry.PutLogsResponse;
import tech.finovy.framework.logappender.exception.LogException;

public interface LogService {
    PutLogsResponse putLogs(PutLogsRequest request) throws LogException;
}
