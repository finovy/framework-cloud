package tech.finovy.framework.logappender.entry;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
    private static final long serialVersionUID = -1329383224784875341L;
    private final Map<String, String> mParams = new HashMap<>();
    private final String mProject;

    public Request(String project) {
        mProject = project;
    }

    public String getProject() {
        return mProject;
    }

    public String getParam(String key) {
        if (mParams.containsKey(key)) {
            return mParams.get(key);
        } else {
            return "";
        }
    }

    public void setParam(String key, String value) {
        if (value == null) {
            mParams.put(key, "");
        } else {
            mParams.put(key, value);
        }
    }

    public Map<String, String> getAllParams() {
        return mParams;
    }

}
