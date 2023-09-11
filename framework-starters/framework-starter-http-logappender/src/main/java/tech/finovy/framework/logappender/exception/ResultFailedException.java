package tech.finovy.framework.logappender.exception;


import tech.finovy.framework.logappender.entry.Attempt;
import tech.finovy.framework.logappender.entry.Result;

import java.util.List;

public class ResultFailedException extends ProducerException {
    private static final long serialVersionUID = -4165865178631967894L;
    private final Result result;

    public ResultFailedException(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public String getErrorCode() {
        return result.getErrorCode();
    }

    public String getErrorMessage() {
        return result.getErrorMessage();
    }

    public List<Attempt> getReservedAttempts() {
        return result.getReservedAttempts();
    }
}
