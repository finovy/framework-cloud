package tech.finovy.framework.logappender.push.internals;

import tech.finovy.framework.logappender.entry.Response;
import tech.finovy.framework.logappender.entry.Shard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListShardResponse extends Response {
    private static final long serialVersionUID = -8483367329010665256L;
    protected List<Shard> mShards = null;

    public ListShardResponse(Map<String, String> headers, ArrayList<Shard> shards) {
        super(headers);
        mShards = new ArrayList<>();
        mShards.addAll(shards);
    }

    public List<Shard> getShards() {
        return mShards;
    }
}
