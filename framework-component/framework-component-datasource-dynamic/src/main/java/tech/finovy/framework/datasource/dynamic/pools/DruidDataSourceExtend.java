package tech.finovy.framework.datasource.dynamic.pools;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.Serial;

/**
 * extend DruidDataSource
 */
public class DruidDataSourceExtend extends DruidDataSource {

    @Serial
    private static final long serialVersionUID = 8005806565249217571L;
    /**
     * MD5 of value, just for decide to reload or not
     */
    private String valueMd5;


    public String getValueMd5() {
        return valueMd5;
    }

    public void setValueMd5(String valueMd5) {
        this.valueMd5 = valueMd5;
    }
}
