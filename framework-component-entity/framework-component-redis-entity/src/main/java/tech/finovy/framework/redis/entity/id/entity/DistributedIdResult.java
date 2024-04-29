package tech.finovy.framework.redis.entity.id.entity;

import java.io.Serializable;

public class DistributedIdResult implements Serializable {
    private static final long serialVersionUID = -7222887433277643892L;
    private long value;
    private String serial;
    private int scale;
    private boolean success;

    public DistributedIdResult() {

    }
    public DistributedIdResult(long value, boolean success) {
        this.value = value;
        this.success = success;
    }

    public long getValue() {
        return value;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial,int scale) {
        this.serial = serial;
        this.scale=scale;
    }

    public int getScale() {
        return scale;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("value=").append(value);
        sb.append(", success=").append(success);
        sb.append('}');
        return sb.toString();
    }
}
