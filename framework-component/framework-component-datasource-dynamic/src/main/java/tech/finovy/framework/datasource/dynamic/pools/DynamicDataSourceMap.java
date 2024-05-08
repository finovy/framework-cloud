package tech.finovy.framework.datasource.dynamic.pools;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import tech.finovy.framework.datasource.dynamic.entity.DynamicDatasourceConfig;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSourceMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceMap.class);
    private static final Map<String, DruidDataSourceExtend> DYNAMIC_DATASOURCE = new ConcurrentHashMap<>();
    private static final Map<String, String> KEY_CONFIGS = new HashMap<>();

    public Map<String, DruidDataSourceExtend> getDynamicDatasource() {
        return DYNAMIC_DATASOURCE;
    }

    public synchronized void refreshDatasource(DynamicDatasourceConfig eachConfig) throws SQLException {
        String md5 = md5Hex(eachConfig.toString());
        if (DYNAMIC_DATASOURCE.containsKey(eachConfig.getKey()) && md5.equals(DYNAMIC_DATASOURCE.get(eachConfig.getKey()).getValueMd5())) {
            return;
        }
        DruidDataSourceExtend dataSourceExtend = new DruidDataSourceExtend();
        dataSourceExtend.setUrl(eachConfig.getUrl());
        dataSourceExtend.setUsername(eachConfig.getUsername());
        dataSourceExtend.setPassword(eachConfig.getPassword());
        dataSourceExtend.setValueMd5(md5);
        dataSourceExtend.setPoolPreparedStatements(eachConfig.isPoolPreparedStatements());
        dataSourceExtend.setAsyncInit(eachConfig.isAsyncInit());
        dataSourceExtend.setValidationQuery("select 1");
        dataSourceExtend.init();
        DYNAMIC_DATASOURCE.put(eachConfig.getKey(), dataSourceExtend);
        KEY_CONFIGS.put(eachConfig.getKey(), JSON.toJSONString(eachConfig));
        LOGGER.info("init datasource({}) success ", eachConfig.getKey());
    }

    public synchronized boolean checkDatasourceExists(DynamicDatasourceConfig eachConfig) {
        String key = eachConfig.getKey();
        String newValueMd5 = md5Hex(eachConfig.toString());
        if (DYNAMIC_DATASOURCE.containsKey(key)) {
            DruidDataSourceExtend dataSourceExtend = DYNAMIC_DATASOURCE.get(key);
            String oldValueMd5 = dataSourceExtend.getValueMd5();
            return newValueMd5.equals(oldValueMd5);
        }
        return false;
    }

    public boolean destroy(String key) {
        try {
            if (!DYNAMIC_DATASOURCE.containsKey(key)) {
                return true;
            }
            DruidDataSource dataSource = DYNAMIC_DATASOURCE.get(key);
            if (!dataSource.isClosed()) {
                dataSource.close();
            }
            DYNAMIC_DATASOURCE.remove(key);
            KEY_CONFIGS.remove(key);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return false;
    }

    public DruidDataSource getDatasource(String key) throws SQLException {
        DruidDataSourceExtend druidDataSourceExtend = DYNAMIC_DATASOURCE.get(key);
        if (druidDataSourceExtend == null && KEY_CONFIGS.containsKey(key)) {
            DynamicDatasourceConfig dynamicDatasourceConfig = JSON.parseObject(KEY_CONFIGS.get(key), DynamicDatasourceConfig.class);
            refreshDatasource(dynamicDatasourceConfig);
            LOGGER.warn("getDatasource--refreshDatasource");
        }
        return DYNAMIC_DATASOURCE.get(key);
    }

    public Connection getConnection(String key) throws SQLException {
        return DYNAMIC_DATASOURCE.get(key).getConnection();
    }

    public static String md5Hex(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
    }
}
