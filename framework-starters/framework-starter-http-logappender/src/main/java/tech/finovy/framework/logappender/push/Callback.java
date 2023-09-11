package tech.finovy.framework.logappender.push;

import tech.finovy.framework.logappender.entry.Result;

public interface Callback {
    void onCompletion(Result result);
}
