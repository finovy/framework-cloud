package tech.finovy.framework.logappender.entry;

import java.io.Serializable;

public class TagContent implements Serializable {

    private static final long serialVersionUID = 2195194971349514285L;
    public String key;
    public String value;

    public TagContent(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
