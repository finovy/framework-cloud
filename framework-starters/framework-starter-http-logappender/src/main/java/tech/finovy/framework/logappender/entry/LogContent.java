package tech.finovy.framework.logappender.entry;

import java.io.Serializable;

public class LogContent implements Serializable {
    private static final long serialVersionUID = 3667592570148778410L;
    public String mKey;
    public String mValue;

    public LogContent() {
    }

    public LogContent(String key, String value) {
        this.mKey = key;
        this.mValue = value;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getValue() {
        return this.mValue;
    }
}
