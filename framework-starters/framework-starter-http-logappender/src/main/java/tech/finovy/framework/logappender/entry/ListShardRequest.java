package tech.finovy.framework.logappender.entry;

public class ListShardRequest extends Request {
    private static final long serialVersionUID = -1255174831216721645L;
    protected String mLogStore = "";

    public ListShardRequest(String project, String logStore) {
        super(project);
        mLogStore = logStore;
    }

    public String getLogStore() {
        return mLogStore;
    }

    public void setLogStore(String logStore) {
        mLogStore = logStore;
    }
}
