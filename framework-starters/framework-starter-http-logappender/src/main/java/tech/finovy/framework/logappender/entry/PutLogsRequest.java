package tech.finovy.framework.logappender.entry;

import tech.finovy.framework.logappender.push.internals.Consts;
import tech.finovy.framework.logappender.push.internals.Consts.CompressType;

import java.util.ArrayList;
import java.util.List;

public class PutLogsRequest extends Request {
    private static final long serialVersionUID = -6429485889601490435L;
    private String mLogStore;
    private String mTopic;
    private String mSource;
    private String mHashKey;
    private List<LogItem> mlogItems;
    private List<TagContent> mTags = null;
    private CompressType compressType = CompressType.LZ4;
    private String mContentType = Consts.CONST_PROTO_BUF;
    private byte[] mLogGroupBytes = null;

    public PutLogsRequest(String project, String logStore, String topic, String source, List<LogItem> logItems, String hashKey) {
        super(project);
        mLogStore = logStore;
        mTopic = topic;
        mSource = source;
        mlogItems = new ArrayList<>(logItems);
        mHashKey = hashKey;
    }

    public PutLogsRequest(String project, String logStore, String topic, String source, List<LogItem> logItems) {
        super(project);
        mLogStore = logStore;
        mTopic = topic;
        mSource = source;
        mlogItems = new ArrayList<>(logItems);
        mHashKey = null;
    }

    public PutLogsRequest(String project, String logStore, String topic, List<LogItem> logItems) {
        super(project);
        mLogStore = logStore;
        mTopic = topic;
        mlogItems = new ArrayList<>(logItems);
    }

    public PutLogsRequest(String project, String logStore, String topic, String source, byte[] logGroupBytes, String hashKey) {
        super(project);
        mLogStore = logStore;
        mTopic = topic;
        mSource = source;
        mLogGroupBytes = logGroupBytes;
        mHashKey = hashKey;
    }

    public CompressType getCompressType() {
        return compressType;
    }

    public void setCompressType(CompressType compressType) {
        this.compressType = compressType;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

    public String getLogStore() {
        return mLogStore;
    }

    public void setLogStore(String logStore) {
        mLogStore = logStore;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String topic) {
        mTopic = topic;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public List<LogItem> getLogItems() {
        return mlogItems;
    }

    public List<TagContent> getTags() {
        return mTags;
    }

    public void setTags(List<TagContent> tags) {
        mTags = new ArrayList<>(tags);
    }

    public byte[] getLogGroupBytes() {
        return mLogGroupBytes;
    }

    public void setlogItems(List<LogItem> logItems) {
        mlogItems = new ArrayList<>(logItems);
    }

    public String getRouteKey() {
        return mHashKey;
    }

    public void setRouteKey(String hashKey) {
        setParam(Consts.CONST_ROUTE_KEY, hashKey);
    }
}
