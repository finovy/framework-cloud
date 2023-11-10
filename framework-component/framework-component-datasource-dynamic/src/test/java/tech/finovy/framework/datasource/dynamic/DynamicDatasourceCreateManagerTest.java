package tech.finovy.framework.datasource.dynamic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.datasource.dynamic.entity.DynamicDatasourceConfig;
import tech.finovy.framework.datasource.dynamic.manager.DynamicDatasourceProperties;
import tech.finovy.framework.datasource.dynamic.pools.DruidDataSourceExtend;
import tech.finovy.framework.datasource.dynamic.pools.DynamicDataSourceMap;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

class DynamicDatasourceCreateManagerTest {

    @Test
    void testDynamicDataSourceMap() throws SQLException {
        final DynamicDataSourceMap dynamicDataSourceMap = new DynamicDataSourceMap();
        String key = "testKey";
        DynamicDatasourceConfig eachConfig = new DynamicDatasourceConfig();
        eachConfig.setKey(key);
        eachConfig.setPassword("h2");
        eachConfig.setUsername("h2");
        eachConfig.setUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000");
        dynamicDataSourceMap.refreshDatasource(eachConfig);
        DataSource datasourceRet = dynamicDataSourceMap.getDatasource(key);
        Assertions.assertNotNull(datasourceRet);
        dynamicDataSourceMap.refreshDatasource(eachConfig);
        final Map<String, DruidDataSourceExtend> dynamicDatasource = dynamicDataSourceMap.getDynamicDatasource();
        Assertions.assertNotNull(dynamicDatasource.get(key));
    }

    @Test
    void testDynamicDatasourceProperties(){
        final DynamicDatasourceProperties properties = new DynamicDatasourceProperties();
        Assertions.assertSame("framework-dynamic-datasource", properties.getDataId());
        Assertions.assertSame("DEFAULT_GROUP", properties.getDataGroup());
        Assertions.assertNull(properties.getIv());
        Assertions.assertNull(properties.getSecret());
    }
}
