package tech.finovy.framework.logappender;


import tech.finovy.framework.logappender.entry.LogItem;
import tech.finovy.framework.logappender.entry.Result;
import tech.finovy.framework.logappender.push.Callback;

import java.util.List;


public class LogHttpAppenderCallback<E> implements Callback {

    protected LogHttpAppender<E> logHttpAppender;

    protected String project;

    protected String logstore;

    protected String topic;

    protected String source;

    protected List<LogItem> logItems;

    public LogHttpAppenderCallback(LogHttpAppender<E> logHttpAppender, String project, String logstore, String topic,
                                   String source, List<LogItem> logItems) {
        super();
        this.logHttpAppender = logHttpAppender;
        this.project = project;
        this.logstore = logstore;
        this.topic = topic;
        this.source = source;
        this.logItems = logItems;
    }

    @Override
    public void onCompletion(Result result) {
    }
}
