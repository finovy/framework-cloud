package tech.finovy.framework.logappender.entry;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author dtype.huang
 */
public class LogItem implements Serializable {
    private static final long serialVersionUID = 4223669320701159704L;
    private long mLogTime;
    private List<LogContent> mContents = new ArrayList();

    public LogItem() {
        this.mLogTime = (new Date()).getTime();
    }

    public LogItem(int logTime) {
        this.mLogTime = logTime;
    }

    public LogItem(int logTime, List<LogContent> contents) {
        this.mLogTime = logTime;
        this.setLogContents(contents);
    }

    public long getTime() {
        return this.mLogTime;
    }

    public void setTime(long logTime) {
        this.mLogTime = logTime;
    }

    public void pushBack(String key, String value) {
        this.pushBack(new LogContent(key, value));
    }

    public void pushBack(LogContent content) {
        this.mContents.add(content);
    }

    public List<LogContent> getLogContents() {
        return this.mContents;
    }

    public void setLogContents(List<LogContent> contents) {
        this.mContents = new ArrayList(contents);
    }

    public String toJsonString() {
        JSONObject obj = new JSONObject();
        obj.put("logtime", this.mLogTime);
        Iterator var2 = this.mContents.iterator();
        while (var2.hasNext()) {
            LogContent content = (LogContent) var2.next();
            obj.put(content.getKey(), content.getValue());
        }
        return obj.toString();
    }
}
