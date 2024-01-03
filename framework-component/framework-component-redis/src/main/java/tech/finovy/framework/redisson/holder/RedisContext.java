package tech.finovy.framework.redisson.holder;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import tech.finovy.framework.redisson.config.RedissonConfiguration;

@Getter
@Setter
public class RedisContext {

    private RedissonClient client;
    private RedissonConfiguration configuration;
    private static final String[] REP = new String[]{"tech.finovy.framework.distributed.event.entity.", "::"};
    private static final String[] EMPTY_STRING = new String[]{"", ""};
    private int version;

    public void setRedissonConfiguration(RedissonConfiguration configuration, int version) {
        this.configuration = configuration;
    }

    public boolean isDebug() {
        return configuration.isDebug();
    }

    public int getVersion() {
        return version;
    }

    public String createKey(String key, String type, boolean skipPrefix) {
        if (skipPrefix) {
            return key;
        }
        StringBuilder strBul = new StringBuilder(configuration.getKeyPrefix());
        if (configuration.getKeyVersion() != null) {
            strBul.append(":").append(configuration.getKeyVersion());
        }
        if (type != null) {
            type = StringUtils.replaceEach(type, REP, EMPTY_STRING);
        }
        strBul.append(":")
                .append(type)
                .append(":")
                .append(key);
        return strBul.toString();
    }

    public String createKey(String key, String type) {
        return createKey(key, type, false);
    }

    public String createKey(String key) {
        StringBuilder strBul = new StringBuilder(configuration.getKeyPrefix());
        if (configuration.getKeyVersion() != null) {
            strBul.append(":").append(configuration.getKeyVersion());
        }
        strBul.append(":").append(key);
        return strBul.toString();
    }

    public String createMapKey(String key) {
        StringBuilder strBul = new StringBuilder(configuration.getKeyPrefix());
        if (configuration.getKeyVersion() != null) {
            strBul.append(":").append(configuration.getKeyVersion());
        }
        strBul.append(":RMap:").append(key);
        return strBul.toString();
    }

    public String createLocalCacheMapKey(String key) {
        StringBuilder strBul = new StringBuilder(configuration.getKeyPrefix());
        if (configuration.getKeyVersion() != null) {
            strBul.append(":").append(configuration.getKeyVersion());
        }
        strBul.append(":LocalCacheMap:").append(key);
        return strBul.toString();
    }

    public int calHash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    public int calKeyHash(String key) {
        return calHash(key) % configuration.getKeyHashModSize();
    }
}
