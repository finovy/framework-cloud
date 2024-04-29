package tech.finovy.framework.mongodb.client;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.mongodb.client.entity.MongoDbClientConfig;
import tech.finovy.framework.mongodb.client.entity.MongodbDatasourceConfigList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MongodbClientSourceMapTest {

    private MongodbClientSourceMap mongodbClientSourceMapUnderTest;

    private MongodbDatasourceConfigList mongodbDatasourceConfigList;

    @BeforeEach
    void setUp() {
        mongodbDatasourceConfigList = new MongodbDatasourceConfigList();
        final MongoDbClientConfig configA = new MongoDbClientConfig();
        configA.setDatabase("A");
        configA.setHost("10.7.0.25");
        configA.setPort(27017);
        configA.setPassword("zLYP0e7BI4sdvJfYQDPED3e345fk");
        configA.setUsername("user_dev_v5");
        final MongoDbClientConfig configB = new MongoDbClientConfig();
        configB.setDatabase("B");
        configB.setHost("10.7.0.25:27017;10.7.0.25:27017");
        configB.setPort(27017);
        configB.setPassword("zLYP0e7BI4sdvJfYQDPED3e345fk");
        configB.setUsername("user_dev_v5");
        final MongoDbClientConfig configC= new MongoDbClientConfig();
        configC.setDatabase("C");
        configC.setHost("10.7.0.25:27017;10.7.0.25:27017");
        configC.setPort(27017);
        configC.setPassword("zLYP0e7BI4sdvJfYQDPED3e345fk");
        configC.setUsername("user_dev_v5");
        List<MongoDbClientConfig> configs = new ArrayList<>();
        configs.add(configA);
        configs.add(configB);
        configs.add(configC);
        mongodbDatasourceConfigList.setConfig(configs);

        mongodbClientSourceMapUnderTest = new MongodbClientSourceMap();
    }

    @Test
    void testGetMongodbClientSource() {
        // Setup
        // Run the test
        final Map<String, MongoClient> result = mongodbClientSourceMapUnderTest.getMongodbClientSource();

        // Verify the results
    }

    @Test
    void testRefreshMongodbSource() {
        final MongoDbClientConfig configA = mongodbDatasourceConfigList.getConfig().get(0);
        // normal
        mongodbClientSourceMapUnderTest.refreshMongodbSource(configA);
        // this client is existed, will return
        mongodbClientSourceMapUnderTest.refreshMongodbSource(configA);

        final MongoDbClientConfig configB = mongodbDatasourceConfigList.getConfig().get(1);
        // error branch
        final String host = configB.getHost();
        configB.setHost(null);
        // no host ,return
        mongodbClientSourceMapUnderTest.refreshMongodbSource(configB);
        configB.setHost(host);
        mongodbClientSourceMapUnderTest.refreshMongodbSource(configB);
    }

    @Test
    void testCheckDatasourceExists() {
        final boolean result = mongodbClientSourceMapUnderTest.checkDatasourceExists(mongodbDatasourceConfigList.getConfig().get(0));
        assertFalse(result);
    }

    @Test
    void testDestroy() {
        mongodbClientSourceMapUnderTest.refreshMongodbSource(mongodbDatasourceConfigList.getConfig().get(0));
        assertTrue(mongodbClientSourceMapUnderTest.destroy("A"));
    }

    @Test
    void testGetMongodbClient() {
        Assertions.assertNull(mongodbClientSourceMapUnderTest.getMongodbClient("A"));
    }

    @Test
    void testGetMongodbDatabase() {
        Assertions.assertNotNull(mongodbClientSourceMapUnderTest.getMongodbDatabase("A"));
    }
}
