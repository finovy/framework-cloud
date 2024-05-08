package tech.finovy.framework.mongodb.client;

import com.alibaba.fastjson2.JSON;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.mongodb.client.entity.MongoDbClientConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MongodbClientSourceMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongodbClientSourceMap.class);

    private static final Map<String, MongoClient> MONGO_CLIENT_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> KEY_CONFIGS = new HashMap<>();

    public Map<String, MongoClient> getMongodbClientSource() {
        return MONGO_CLIENT_CONCURRENT_HASH_MAP;
    }

    public synchronized void refreshMongodbSource(MongoDbClientConfig clientConfig) {
        if (MONGO_CLIENT_CONCURRENT_HASH_MAP.containsKey(clientConfig.getDatabase())) {
            return;
        }
        if (StringUtils.isEmpty(clientConfig.getHost())) {
            return;
        }
        List<String> arr = Arrays.asList(clientConfig.getHost().split(";"));
        List<ServerAddress> arrHost = new ArrayList<>();
        arr.forEach(host -> {
            // 兼容这种方式配置 192.168.31.31:27017;192.168.31.32:27018
            String[] split = host.split(":");
            if (split.length == 2) {
                // 此时端口将不使用 clientConfig.getPort() 方式获取
                arrHost.add(new ServerAddress(split[0], Integer.parseInt(split[1])));
            } else {
                arrHost.add(new ServerAddress(host, clientConfig.getPort()));
            }
        });
        MongoCredential credential = MongoCredential.createCredential(clientConfig.getUsername(), clientConfig.getDatabase(), clientConfig.getPassword().toCharArray());
        MongoClient client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(arrHost))
                        .credential(credential)
                        .build());
        MONGO_CLIENT_CONCURRENT_HASH_MAP.put(clientConfig.getDatabase(), client);
        KEY_CONFIGS.put(clientConfig.getDatabase(), JSON.toJSONString(clientConfig));
        this.testClient(client, clientConfig.getDatabase());
        LOGGER.info("init mongDbClientSource({}) success ", JSON.toJSONString(clientConfig));
    }

    private void testClient(MongoClient client, String database) {
        try {
            LOGGER.debug("test connect database {}", database);
            client.getDatabase(database).getCollection("system");
        } catch (Exception e) {
            LOGGER.error("database {} init fail {}", database, e.getMessage(), e);
        }
    }

    public synchronized boolean checkDatasourceExists(MongoDbClientConfig eachConfig) {
        String key = eachConfig.getDatabase();
        return MONGO_CLIENT_CONCURRENT_HASH_MAP.containsKey(key);
    }

    public boolean destroy(String key) {
        if (!MONGO_CLIENT_CONCURRENT_HASH_MAP.containsKey(key)) {
            return true;
        }
        MongoClient client = MONGO_CLIENT_CONCURRENT_HASH_MAP.get(key);
        if (client != null) {
            client.close();
        }
        MONGO_CLIENT_CONCURRENT_HASH_MAP.remove(key);
        KEY_CONFIGS.remove(key);
        return true;
    }

    public MongoClient getMongodbClient(String key) {
        MongoClient mongoClient = MONGO_CLIENT_CONCURRENT_HASH_MAP.get(key);
        if (mongoClient == null && KEY_CONFIGS.containsKey(key)) {
            refreshMongodbSource(JSON.parseObject(KEY_CONFIGS.get(key), MongoDbClientConfig.class));
            LOGGER.warn("getMongodbClient--refreshDatasource");
        }
        return MONGO_CLIENT_CONCURRENT_HASH_MAP.get(key);
    }

    public MongoDatabase getMongodbDatabase(String key) {
        return this.getMongodbClient(key).getDatabase(key);
    }

}
