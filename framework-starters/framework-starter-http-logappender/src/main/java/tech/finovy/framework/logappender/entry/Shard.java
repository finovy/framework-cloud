package tech.finovy.framework.logappender.entry;

import java.io.Serializable;

public class Shard implements Serializable {
    private static final long serialVersionUID = -7542891312258477613L;
    protected int shardId;
    protected String status;
    protected String inclusiveBeginKey;
    protected String exclusiveEndKey;
    protected String serverIp = null;
    protected int createTime;

    public Shard(int shardId, String status, String begin, String end, int createTime) {
        this.shardId = shardId;
        this.status = status;
        this.inclusiveBeginKey = begin;
        this.exclusiveEndKey = end;
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInclusiveBeginKey() {
        return inclusiveBeginKey;
    }

    public void setInclusiveBeginKey(String inclusiveBeginKey) {
        this.inclusiveBeginKey = inclusiveBeginKey;
    }

    public String getExclusiveEndKey() {
        return exclusiveEndKey;
    }

    public void setExclusiveEndKey(String exclusiveEndKey) {
        this.exclusiveEndKey = exclusiveEndKey;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getShardId() {
        return shardId;
    }
}
