package tech.finovy.framework.datasource.dynamic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.datasource.dynamic.entity.DynamicDatasourceConfig;
import tech.finovy.framework.datasource.dynamic.pools.DynamicDataSourceMap;

import javax.sql.DataSource;
import java.sql.SQLException;

class DynamicDatasourceManagerTest {

    private final DynamicDataSourceMap dynamicDataSourceMap = new DynamicDataSourceMap();

    @Test
    void testDynamicDataSourceMap() throws SQLException {
        String key = "testKey";
        for (int x = 0; x < 50; x++) {
            DynamicDatasourceConfig eachConfig = new DynamicDatasourceConfig();
            eachConfig.setKey(key + x);
            eachConfig.setPassword("h2");
            eachConfig.setUsername("h2");
            eachConfig.setUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000");
            dynamicDataSourceMap.refreshDatasource(eachConfig);
            DataSource datasourceRet = dynamicDataSourceMap.getDatasource(key + x);
            Assertions.assertNotNull(datasourceRet);
        }
        for (int x = 0; x < 50; x++) {
            dynamicDataSourceMap.destroy(key + x);
        }
    }
}
