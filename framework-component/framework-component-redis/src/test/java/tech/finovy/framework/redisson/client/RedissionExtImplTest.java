package tech.finovy.framework.redisson.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import tech.finovy.framework.redisson.config.RedissonConfiguration;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RedissionExtImplTest {

    @Mock
    private Config mockConfig;

    private RedissonClient client;

    private Config config;


    @BeforeEach
    void setUp() throws IOException {
        String configStr = "sentinelServersConfig:\n" +
                "  idleConnectionTimeout: 10000\n" +
                "  checkSentinelsList: false\n" +
                "  connectTimeout: 10000\n" +
                "  timeout: 3000\n" +
                "  retryAttempts: 3\n" +
                "  retryInterval: 1500\n" +
                "  password: ^6<C#Kc^XZPd4[>D0I:lAM\n" +
                "  subscriptionsPerConnection: 5\n" +
                "  clientName: null\n" +
                "  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}  \n" +
                "  slaveConnectionMinimumIdleSize: 32\n" +
                "  slaveConnectionPoolSize: 64\n" +
                "  masterConnectionMinimumIdleSize: 32\n" +
                "  masterConnectionPoolSize: 64\n" +
                "  readMode: \"SLAVE\"\n" +
                "  sentinelAddresses:\n" +
                "  - \"redis://10.7.0.18:26379\"\n" +
                "  - \"redis://10.7.0.19:26379\"\n" +
                "  - \"redis://10.7.0.20:26379\"\n" +
                "  masterName: \"redismaster\"\n" +
                "  database: 0\n" +
                "threads: 0\n" +
                "nettyThreads: 0\n" +
                "codec: !<org.redisson.codec.JsonJacksonCodec> {}";
        ConfigSupport support = new ConfigSupport();
        config = support.fromYAML(configStr, Config.class);
        client = Redisson.create(config);
        RedisContextHolder.get().setClient(client);
        final RedissonConfiguration redissonConfiguration = new RedissonConfiguration();
        redissonConfiguration.setKeyHashModSize(1024);
        redissonConfiguration.setKeyVersion("1.0");
        redissonConfiguration.setKeyPrefix("test");
        RedisContextHolder.get().setRedissonConfiguration(redissonConfiguration,1);
    }


    @Test
    void testSetRedissonConfiguration() {
        // Setup
        final RedissonConfiguration configuration = new RedissonConfiguration();
        configuration.setKeyVersion("keyVersion");
        configuration.setKeyPrefix("keyPrefix");
        configuration.setKeyDefaultTtl(0L);
        configuration.setKeyHashModSize(0);
        configuration.setEncry(false);
        configuration.setDebug(false);
        configuration.setConnectDelay(0L);

        // Run the test
        RedisContextHolder.get().setRedissonConfiguration(configuration, 0);

        // Verify the results
    }

    @Test
    void testIsDebug() {
        // Setup
        // Run the test
        final boolean result = RedisContextHolder.get().isDebug();

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testCreateKey1() {
        // Setup
        // Run the test
        final String result = RedisContextHolder.get().createKey("key", "type", false);

        // Verify the results
        assertEquals("test:1.0:type:key", result);
    }

    @Test
    void testCreateKey2() {
        // Setup
        // Run the test
        final String result = RedisContextHolder.get().createKey("key", "type");

        // Verify the results
        assertEquals("test:1.0:type:key", result);
    }

    @Test
    void testCreateKey3() {
        // Setup
        // Run the test
        final String result = RedisContextHolder.get().createKey("key");

        // Verify the results
        assertEquals("test:1.0:key", result);
    }

    @Test
    void testCreateMapKey() {
        // Setup
        // Run the test
        final String result = RedisContextHolder.get().createMapKey("key");

        // Verify the results
        assertEquals("test:1.0:RMap:key", result);
    }

    @Test
    void testCreateLocalCacheMapKey() {
        // Setup
        // Run the test
        final String result = RedisContextHolder.get().createLocalCacheMapKey("key");

        // Verify the results
        assertEquals("test:1.0:LocalCacheMap:key", result);
    }

    @Test
    void testCalHash() {
        assertEquals(-1180979736, RedisContextHolder.get().calHash("key"));
    }

    @Test
    void testCalKeyHash() {
        // Setup
        // Run the test
        final int result = RedisContextHolder.get().calKeyHash("key");

        // Verify the results
        assertEquals(-536, result);
    }

    @Test
    void testCreate() {
        // Run the test
        final RedissonClient result = Redisson.create(config);
        Assertions.assertNotNull(result);
    }
}
