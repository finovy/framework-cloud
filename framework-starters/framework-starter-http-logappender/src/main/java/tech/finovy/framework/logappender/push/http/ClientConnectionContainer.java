package tech.finovy.framework.logappender.push.http;

import tech.finovy.framework.logappender.entry.Shard;
import tech.finovy.framework.logappender.exception.LogException;
import tech.finovy.framework.logappender.push.internals.ListShardResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientConnectionContainer {
    private final Map<String, ClientConnectionStatus> mShardConnections;
    private final Map<String, Long> mShardLastUpdateTime;
    private final long mGlobalConnectionUpdateInterval = 30L * 1000 * 1000 * 1000L;
    private final long mShardConnectionUpdateInterval = 300L * 1000 * 1000 * 1000L;
    private final long mGlobalConnectionValidInterval = 60L * 1000 * 1000 * 1000L;
    private final long mGlobalConnectionUpdateSendSize = 100 * 1024 * 1024L;
    private ClientImpl mClient;
    private ClientConnectionStatus mGlobalConnection;

    public ClientConnectionContainer() {
        mClient = null;
        mGlobalConnection = null;
        mShardConnections = new ConcurrentHashMap<>();
        mShardLastUpdateTime = new ConcurrentHashMap<>();
    }

    public void init(String endpoint, String accessId, String accessKey) {
        mClient = new ClientImpl(endpoint, accessId, accessKey);
    }

    public ClientConnectionStatus getShardConnection(String project, String logstore, int shardId) {
        String key = project + "#" + logstore;
        if (!mShardLastUpdateTime.containsKey(key)) {
            mShardLastUpdateTime.put(key, (long) 0);
        }
        String keyShard = project + "#" + logstore + "#" + shardId;
        if (!mShardConnections.containsKey(keyShard)) {
            updateShardConnection(key);
        }
        if (mShardConnections.containsKey(keyShard)) {
            return mShardConnections.get(keyShard);
        }
        return null;
    }

    public void resetGlobalConnection() {
        mGlobalConnection = null;
    }


    public ClientConnectionStatus getGlobalConnection() {
        return mGlobalConnection;
    }

    public void updateConnections() {
        updateGlobalConnection();
        updateShardConnections();
    }

    public void updateGlobalConnection() {
        long curTime = System.nanoTime();
        boolean toUpdate = false;
        if (mGlobalConnection == null || !mGlobalConnection.isValidConnection()) {
            toUpdate = true;
        } else if (curTime - mGlobalConnection.getLastUsedTime() < mGlobalConnectionValidInterval
                && (curTime - mGlobalConnection.getCreateTime() > mGlobalConnectionUpdateInterval
                || mGlobalConnection.getSendDataSize() > mGlobalConnectionUpdateSendSize
                || mGlobalConnection.getPullDataSize() > mGlobalConnectionUpdateSendSize)) {
            toUpdate = true;
        }
        if (toUpdate) {
            String ipAddress = mClient.getServerIpAddress("");
            if (!(ipAddress == null || ipAddress.isEmpty())) {
                mGlobalConnection = new ClientConnectionStatus(ipAddress);
            }
        }
    }

    public void updateShardConnections() {
        for (Map.Entry<String, Long> entry : mShardLastUpdateTime.entrySet()) {
            String key = entry.getKey();
            updateShardConnection(key);
        }
    }

    private void updateShardConnection(String projectLogstore) {
        if (!mShardLastUpdateTime.containsKey(projectLogstore)) {
            return;
        }
        Long lastUpdateTime = mShardLastUpdateTime.get(projectLogstore);
        long curTime = System.nanoTime();
        if (curTime - lastUpdateTime < mShardConnectionUpdateInterval) {
            return;
        }
        String[] items = projectLogstore.split("#");
        if (items.length == 2) {
            try {
                ListShardResponse res = mClient.listShard(items[0], items[1]);
                List<Shard> allShards = res.getShards();
                for (Shard shard : allShards) {
                    String serverIp = shard.getServerIp();
                    if (serverIp != null && !serverIp.isEmpty()) {
                        int shardId = shard.getShardId();
                        String key = projectLogstore + "#" + shardId;
                        mShardConnections.put(key, new ClientConnectionStatus(serverIp));
                    }
                }
            } catch (LogException e) {
            }
        }
        mShardLastUpdateTime.put(projectLogstore, curTime);
    }

}
