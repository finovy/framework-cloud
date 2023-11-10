package tech.finovy.framework.redis.entity.cache.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
public class BatchSimpleResult {
    private List<String> errKeys;
    private List<String> successKey;
    private boolean success;

    public List<String> getErrKeys() {
        return errKeys;
    }

    public void setErrKeys(List<String> errKeys) {
        this.errKeys = errKeys;
    }

    public List<String> getSuccessKey() {
        return successKey;
    }

    public void setSuccessKey(List<String> successKey) {
        this.successKey = successKey;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
