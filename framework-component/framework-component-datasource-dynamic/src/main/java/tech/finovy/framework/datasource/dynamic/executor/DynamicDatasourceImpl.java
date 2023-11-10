package tech.finovy.framework.datasource.dynamic.executor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tech.finovy.framework.datasource.JavaTypeEnum;
import tech.finovy.framework.datasource.api.DynamicDatasourceApi;
import tech.finovy.framework.datasource.dynamic.pools.DynamicDataSourceMap;
import tech.finovy.framework.datasource.entity.DynamicResultSet;
import tech.finovy.framework.datasource.entity.PrepareDict;
import tech.finovy.framework.datasource.entity.PrepareEach;
import tech.finovy.framework.datasource.entity.PrepareEachList;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class DynamicDatasourceImpl implements DynamicDatasourceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDatasourceImpl.class);
    
    private static final String INSERT = "insert ";
    private static final String DELETE = "delete ";
    private static final String SELECT = "select ";
    private static final String UPDATE = "update ";
    private static final String CALL = "call ";

    private final DynamicDataSourceMap dynamicDataSourceMap;

    public DynamicDatasourceImpl(DynamicDataSourceMap dynamicDataSourceMap) {
        this.dynamicDataSourceMap = dynamicDataSourceMap;
    }

    @Override
    public DynamicResultSet getDynamicResultSet(PrepareEach each, List<Map<String, Object>> input) {
        DynamicResultSet dynamicResultSet;
        Map<String, ResultSet> resultSetMap = new HashMap<>();
        Map<String, PreparedStatement> preparedStatementMap = new HashMap<>();
        Map<String, Connection> connectionMap = new HashMap<>();
        Map<String, Savepoint> savePointMap = new HashMap<>();
        try {
            dynamicResultSet = execute(
                    each,
                    input,
                    resultSetMap,
                    preparedStatementMap,
                    connectionMap,
                    savePointMap);
            commitMap(connectionMap, savePointMap);
        } catch (SQLException e) {
            rollbackMap(connectionMap, savePointMap);
            LOGGER.error("getDynamicResultSet{}", e.getMessage(), e);
            throw new RuntimeException("sql execute error");
        } finally {
            closeResourceMap(resultSetMap, preparedStatementMap, connectionMap);
        }
        return dynamicResultSet;
    }

    /**
     * 多级
     *
     * @param eachs
     * @param input
     * @return
     */
    @Override
    public DynamicResultSet getDynamicResultSet(PrepareEachList eachs, List<Map<String, Object>> input) {
        DynamicResultSet ret = null;
        List<PrepareEach> prepareEaches = eachs.getPrepareEaches();
        if (CollectionUtils.isEmpty(prepareEaches)) {
            return ret;
        }
        Map<String, ResultSet> resultSetMap = new HashMap<>();
        Map<String, PreparedStatement> preparedStatementMap = new HashMap<>();
        Map<String, Connection> connectionMap = new HashMap<>();
        Map<String, Savepoint> savePointMap = new HashMap<>();
        Map<String, List<Map<String, Object>>> eachMap = new HashMap<>();
        try {
            for (int i = 0; i < prepareEaches.size(); i++) {
                PrepareEach each = prepareEaches.get(i);
                if (StringUtils.isNotBlank(each.getInputKey())) {
                    input = eachMap.get(each.getInputKey());
                }
                DynamicResultSet dynamicResultSet = execute(each, input,
                        resultSetMap, preparedStatementMap, connectionMap, savePointMap);
                if (each.isNeedCache()) {
                    eachMap.put(each.getEachKey(), dynamicResultSet.getResult());
                }
                if (i == prepareEaches.size() - 1) {
                    ret = dynamicResultSet;
                }
                LOGGER.info("{} return :{}", i + 1, dynamicResultSet.getResult());
            }
            commitMap(connectionMap, savePointMap);
        } catch (SQLException e) {
            rollbackMap(connectionMap, savePointMap);
            LOGGER.error("getDynamicResultSet{}", e.getMessage(), e);
            throw new RuntimeException("sql execute error");
        } finally {
            closeResourceMap(resultSetMap, preparedStatementMap, connectionMap);
        }
        eachMap.clear();
        return ret;
    }

    /**
     * 执行
     *
     * @param each
     * @param input
     * @param resultSetMap
     * @param preparedStatementMap
     * @param connectionMap
     * @param savePointMap
     * @return
     * @throws SQLException
     */
    private DynamicResultSet execute(PrepareEach each, List<Map<String, Object>> input,
                                     Map<String, ResultSet> resultSetMap,
                                     Map<String, PreparedStatement> preparedStatementMap,
                                     Map<String, Connection> connectionMap,
                                     Map<String, Savepoint> savePointMap

    ) throws SQLException {
        DynamicResultSet dynamicResultSet = verify(each, input);
        if (dynamicResultSet.getErrMsg() != null) {
            return dynamicResultSet;
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int sqlType = sqlType(each);
        connection = dynamicDataSourceMap.getConnection(each.getDatasourceKey());
        //开启事务
        setAutoCommit(connection, each);
        if (each.isProcedure()) {
            //存储过程 调用语句如：{call proc_select(?,?)};
            preparedStatement = exeProc(each, input, connection);
            resultSet = preparedStatement.getResultSet();
        } else {
            preparedStatement = exePst(each, input, connection);
            resultSet = preparedStatement.getResultSet();
        }
        Savepoint savepoint = savePoint(connection, each);
        //处理结果集
        if (sqlType == 4) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<Map<String, Object>> resultMap = getResultMap(resultSet, metaData);
            dynamicResultSet.setResult(resultMap);
        } else {
            dynamicResultSet.setUpdateCount(preparedStatement.getUpdateCount());
        }

        if (!ObjectUtils.isEmpty(resultSet)) {
            resultSetMap.put(each.getDatasourceKey(), resultSet);
        }
        if (!ObjectUtils.isEmpty(savepoint)) {
            savePointMap.put(each.getDatasourceKey(), savepoint);
        }
        connectionMap.put(each.getDatasourceKey(), connection);
        preparedStatementMap.put(each.getDatasourceKey(), preparedStatement);
        return dynamicResultSet;
    }

    /**
     * 构建sql
     * 为占位符设置具体内容
     * insert into api_users(name,email) values(?,?)
     *
     * @param each
     * @param input <field : value> input 一条 查询； 多条 input
     *              多条：insert into api_users(name,email) values      (?,?,?),(?,?,?),(?,?,?)
     * @return
     */
    public PreparedStatement exePst(PrepareEach each, List<Map<String, Object>> input, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        Map<Integer, PrepareDict> indexMap = each.getIndex();
        if (ObjectUtils.isEmpty(indexMap)) {
            preparedStatement = connection.prepareStatement(each.getPrepareSql());
            preparedStatement.execute();

            return preparedStatement;
        }
        int columnNum = indexMap.size();
        String bachInsertSql = "(";
        if (CollectionUtils.isEmpty(input)) {
            //取默认值
            preparedStatement = connection.prepareStatement(each.getPrepareSql());
            for (Integer key : indexMap.keySet()) {
                PrepareDict prepareDict = indexMap.get(key);
                setPreparedStatement(preparedStatement, prepareDict.getColumnType(), prepareDict.getColumnIndex(), prepareDict.getDefaultValue());
            }
        } else if (input.size() == 1) {
            preparedStatement = connection.prepareStatement(each.getPrepareSql());
            for (Integer key : indexMap.keySet()) {
                PrepareDict prepareDict = indexMap.get(key);
                Map<String, Object> map = input.get(0);
                Object val = map.get(prepareDict.getColumnName());
                if (prepareDict.isMustInput() && ObjectUtils.isEmpty(val)) {
                    LOGGER.warn("must input value is null");
                    throw new RuntimeException();
                } else if (!prepareDict.isMustInput() && ObjectUtils.isEmpty(val)) {
                    val = prepareDict.getDefaultValue();
                }
                setPreparedStatement(preparedStatement, prepareDict.getColumnType(), prepareDict.getColumnIndex(), val);
            }

        } else if (!CollectionUtils.isEmpty(input) && input.size() > 1) {
            if (each.getPrepareSql().contains("?")) {
                preparedStatement = connection.prepareStatement(each.getPrepareSql());
            } else {
                //insert into api_users(name,email) values
                for (Integer key : indexMap.keySet()) {
                    bachInsertSql = bachInsertSql + "?,";
                }
                bachInsertSql = bachInsertSql.substring(0, bachInsertSql.length() - 1) + ")";
                StringBuffer sqlBuffer = new StringBuffer(each.getPrepareSql());
                sqlBuffer.append(bachInsertSql);
                for (int j = 2; j <= input.size(); j++) {
                    sqlBuffer.append("," + bachInsertSql);
                }
                sqlBuffer.append(";");
                String sql = new String(sqlBuffer);
                preparedStatement = connection.prepareStatement(sql);
            }
            for (int j = 0; j < input.size(); j++) {
                Map<String, Object> valueMap = input.get(j);
                for (Integer key : indexMap.keySet()) {
                    PrepareDict prepareDict = indexMap.get(key);
                    Object val = valueMap.get(prepareDict.getColumnName());
                    if (prepareDict.isMustInput() && ObjectUtils.isEmpty(val)) {
                        LOGGER.warn("MustInput Value is Null");
                        throw new RuntimeException();
                    } else if (!prepareDict.isMustInput() && ObjectUtils.isEmpty(val)) {
                        val = prepareDict.getDefaultValue();
                    }
                    setPreparedStatement(preparedStatement, prepareDict.getColumnType(), columnNum * j + prepareDict.getColumnIndex(), val);
                }
            }
        }
        preparedStatement.execute();
        return preparedStatement;
    }

    public PreparedStatement exeProc(PrepareEach each, List<Map<String, Object>> input, Connection connection) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(each.getPrepareSql());
        Map<Integer, PrepareDict> indexMap = each.getIndex();
        if (ObjectUtils.isEmpty(indexMap)) {
            callableStatement.execute();
            commit(connection, each);
            return callableStatement;
        }
        int columnNum = indexMap.size();
        if (CollectionUtils.isEmpty(input)) {
            //取默认值
            for (Integer key : indexMap.keySet()) {
                PrepareDict prepareDict = indexMap.get(key);
                setPreparedStatement(callableStatement, prepareDict.getColumnType(), prepareDict.getColumnIndex(), prepareDict.getDefaultValue());
            }
        } else {
            for (Integer key : indexMap.keySet()) {
                PrepareDict prepareDict = indexMap.get(key);
                Object val = input.get(0).get(prepareDict.getColumnName());
                if (prepareDict.isMustInput() && ObjectUtils.isEmpty(val)) {
                    throw new RuntimeException();
                } else if (!prepareDict.isMustInput() && ObjectUtils.isEmpty(val)) {
                    val = prepareDict.getDefaultValue();
                }
                setPreparedStatement(callableStatement, prepareDict.getColumnType(), prepareDict.getColumnIndex(), val);
            }
        }
        callableStatement.execute();
        return callableStatement;

    }

    /**
     * 校验sql参数
     *
     * @param each
     * @param input
     * @return
     */
    private DynamicResultSet verify(PrepareEach each, List<Map<String, Object>> input) throws SQLException {
        DynamicResultSet dynamicResultSet = new DynamicResultSet();
        if (each.indexSize() > 0 && ObjectUtils.isEmpty(input)) {
            dynamicResultSet.setErrMsg("Input Value Not Exists");
            LOGGER.warn("Input Value Not Exists");
            return dynamicResultSet;
        }
        if (StringUtils.isBlank(each.getDatasourceKey())) {
            dynamicResultSet.setErrMsg("Input DataSourceKey IS NULL");
            LOGGER.warn("Execute Dynamic Query Error:{}", dynamicResultSet.getErrMsg());
            return dynamicResultSet;
        }
        if (StringUtils.isBlank(each.getPrepareSql())) {
            dynamicResultSet.setErrMsg("Input PrepareSql IS NULL");
            LOGGER.warn("Execute Dynamic Query Error:{}", dynamicResultSet.getErrMsg());
            return dynamicResultSet;
        }
        DataSource datasource = dynamicDataSourceMap.getDatasource(each.getDatasourceKey());
        if (datasource == null) {
            dynamicResultSet.setErrMsg("DataSource(" + each.getDatasourceKey() + ") NOT EXISTS");
            LOGGER.warn("Execute Dynamic Query Error:{}", dynamicResultSet.getErrMsg());
            return dynamicResultSet;
        }
        return dynamicResultSet;
    }

    /**
     * 解析结果集
     *
     * @param rs 结果集
     * @param md 结果集元数据
     * @return 表数据
     * @throws SQLException
     */
    private static List<Map<String, Object>> getResultMap(ResultSet rs, ResultSetMetaData md) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = md.getColumnLabel(i);
                Object columnVal = getType(rs, md, columnName, i);
                resultMap.put(columnName, columnVal);
            }
            resultList.add(resultMap);
        }
        return resultList;
    }

    /**
     * 构建  PreparedStatement
     *
     * @param preparedStatement
     * @param columnType
     * @param index
     * @param value
     */
    public void setPreparedStatement(PreparedStatement preparedStatement, String columnType, int index, Object value) {
        JavaTypeEnum javaTypeEnum = JavaTypeEnum.typeOf(columnType);
        try {
            switch (javaTypeEnum) {
                case BYTE:
                    preparedStatement.setByte(index, (Byte) value);
                    break;
                case SHORT:
                    preparedStatement.setShort(index, (Short) value);
                    break;
                case INT:
                    preparedStatement.setInt(index, Integer.parseInt(value.toString()));
                    break;
                case LONG:
                    preparedStatement.setLong(index, Long.valueOf(value.toString()));
                    break;
                case FLOAT:
                    preparedStatement.setFloat(index, Float.valueOf(value.toString()));
                    break;
                case DOUBLE:
                    preparedStatement.setDouble(index, Double.valueOf(value.toString()));
                    break;
                case BOOLEAN:
                    preparedStatement.setBoolean(index, Boolean.valueOf(value.toString()));
                    break;
                case CHAR:
                    preparedStatement.setString(index, value.toString());
                    break;
                case STRING:
                    preparedStatement.setString(index, value.toString());
                    break;
                case NULL:
                    preparedStatement.setNull(index, Types.NULL);
                    break;
                default:
                    preparedStatement.setString(index, value.toString());

            }
        } catch (SQLException e) {
            LOGGER.error("set PreparedStatement Error");
        }
    }


    /**
     * 根据字段名称和字段类型获取字段的值
     *
     * @param rs         结果集
     * @param md         结果集元数据
     * @param columnName 字段名称
     * @param index      字段序号
     * @return 字段的值
     * @throws SQLException
     */
    public static Object getType(ResultSet rs, ResultSetMetaData md, String columnName, int index) throws SQLException {
        int columnType = md.getColumnType(index);
        switch (columnType) {
            case Types.ARRAY:
                return rs.getArray(columnName);
            case Types.BIGINT:
                return rs.getLong(columnName);
            case Types.TINYINT:
                return rs.getInt(columnName);
            case Types.SMALLINT:
                return rs.getInt(columnName);
            case Types.BIT:
                return rs.getInt(columnName);
            case Types.INTEGER:
                return rs.getInt(columnName);
            case Types.BOOLEAN:
                return rs.getBoolean(columnName);
            case Types.BLOB:
                return rs.getBlob(columnName);
            case Types.DOUBLE:
                return rs.getDouble(columnName);
            case Types.FLOAT:
                return rs.getFloat(columnName);
            case Types.NVARCHAR:
                return rs.getNString(columnName);
            case Types.VARCHAR:
                return rs.getString(columnName);
            case Types.DATE:
                return rs.getDate(columnName);
            case Types.TIMESTAMP:
                return rs.getTimestamp(columnName);
            default:
                return rs.getObject(columnName);
        }
    }

    /**
     * 关闭结果集
     *
     * @param resultSet 结果集
     */
    public static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * 关闭语句执行器
     *
     * @param preparedStatement 语句执行器
     */
    public static void closeStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * 关闭连接
     *
     * @param connection 连接
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }
    }

    /**
     * 关闭资源
     *
     * @param resultSet         结果集
     * @param preparedStatement 语句执行器
     * @param connection        连接
     */
    public static void closeResource(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        closeResultSet(resultSet);
        closeStatement(preparedStatement);
        closeConnection(connection);
    }

    public static void closeResourceMap(Map<String, ResultSet> resultSetMap,
                                        Map<String, PreparedStatement> preparedStatementMap,
                                        Map<String, Connection> connectionMap) {

        for (String key : resultSetMap.keySet()) {
            closeResultSet(resultSetMap.get(key));
        }
        for (String key : preparedStatementMap.keySet()) {
            closeStatement(preparedStatementMap.get(key));
        }
        for (String key : connectionMap.keySet()) {
            closeConnection(connectionMap.get(key));
        }
        resultSetMap.clear();
        preparedStatementMap.clear();
        connectionMap.clear();
    }

    /**
     * sql语句类型 增 删 改 查 存储过程
     *
     * @param each
     * @return
     */
    public int sqlType(PrepareEach each) {
        String prepareSql = each.getPrepareSql().substring(0, 10).toLowerCase();
        if (prepareSql.contains(INSERT)) {
            return 1;
        } else if (prepareSql.contains(DELETE)) {
            return 2;
        } else if (prepareSql.contains(UPDATE)) {
            return 3;
        } else if (prepareSql.contains(SELECT)) {
            return 4;
        }
        return 5;
    }

    /**
     * 校验是否需要事务
     *
     * @param each
     * @return
     */
    private boolean isCommit(PrepareEach each) {
        int i = sqlType(each);
        boolean commit = each.isCommit();
        if (commit == true || i != 4) {
            return true;
        }
        return false;
    }

    private void setAutoCommit(Connection connection, PrepareEach each) throws SQLException {
        //开启事务
        if (isCommit(each)) {
            connection.setAutoCommit(false);
        }
    }

    private void commit(Connection connection, PrepareEach each) throws SQLException {
        //提交事务
        if (isCommit(each)) {
            connection.commit();
        }
    }

    private void commitMap(Map<String, Connection> connectionMap, Map<String, Savepoint> savePointMap) {
        try {
            if (!CollectionUtils.isEmpty(savePointMap)) {
                for (String key : savePointMap.keySet()) {
                    connectionMap.get(key).commit();
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }
    }

    private Savepoint savePoint(Connection connection, PrepareEach each) throws SQLException {
        if (isCommit(each)) {
            return connection.setSavepoint();
        }
        return null;
    }

    private void rollback(Connection connection, PrepareEach each) throws SQLException {
        if (isCommit(each)) {
            connection.rollback();
        }
    }

    private void rollbackMap(Map<String, Connection> connectionMap, Map<String, Savepoint> savePointMap) {
        try {
            if (!CollectionUtils.isEmpty(savePointMap)) {
                for (String key : connectionMap.keySet()) {
                    connectionMap.get(key).rollback(savePointMap.get(key));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
        }
    }
}
