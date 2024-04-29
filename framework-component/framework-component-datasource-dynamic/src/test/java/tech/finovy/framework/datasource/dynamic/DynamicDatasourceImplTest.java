package tech.finovy.framework.datasource.dynamic;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.finovy.framework.datasource.dynamic.entity.DynamicDatasourceConfig;
import tech.finovy.framework.datasource.dynamic.executor.DynamicDatasourceImpl;
import tech.finovy.framework.datasource.dynamic.pools.DynamicDataSourceMap;
import tech.finovy.framework.datasource.entity.DynamicResultSet;
import tech.finovy.framework.datasource.entity.PrepareDict;
import tech.finovy.framework.datasource.entity.PrepareEach;
import tech.finovy.framework.datasource.entity.PrepareEachList;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class DynamicDatasourceImplTest {

    private final DynamicDataSourceMap dynamicDataSourceMap = new DynamicDataSourceMap();
    private final DynamicDatasourceImpl dynamicDatasourceApi = new DynamicDatasourceImpl(dynamicDataSourceMap);
    private static final String h2dbKey = "db-h2";

    @BeforeEach
    public void init() throws SQLException {
        DynamicDatasourceConfig eachConfig = new DynamicDatasourceConfig();
        eachConfig.setKey(h2dbKey);
        eachConfig.setPassword("h2");
        eachConfig.setUsername("h2");
        eachConfig.setUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000");
        dynamicDataSourceMap.refreshDatasource(eachConfig);
    }

    @AfterEach
    public void destroy() {
        dynamicDataSourceMap.destroy(h2dbKey);
    }

    @Test
    void testDynamicDataSourceMap() {
        PrepareEach each = new PrepareEach();
        List<Map<String, Object>> input = new ArrayList<>();
        // case 1: datasourceKey = null
        DynamicResultSet dynamicResultSet = dynamicDatasourceApi.getDynamicResultSet(each, input);
        Assertions.assertNotNull(dynamicResultSet.getErrMsg());
        // case 2: Input PrepareSql IS NULL
        each.setDatasourceKey(h2dbKey + "-0");
        dynamicResultSet = dynamicDatasourceApi.getDynamicResultSet(each, input);
        Assertions.assertNotNull(dynamicResultSet.getErrMsg());
        // case 3: DataSource(db-h2-0) NOT EXISTS
        each.setDatasourceKey(h2dbKey + "-0");
        each.setPrepareSql("SELECT * FROM A");
        dynamicResultSet = dynamicDatasourceApi.getDynamicResultSet(each, input);
        Assertions.assertNotNull(dynamicResultSet.getErrMsg());

        // case 4: need params
        each.setDatasourceKey(h2dbKey + "-0");
        each.setPrepareSql("SELECT * FROM A");
        each.addIndex(1, new PrepareDict());
        dynamicResultSet = dynamicDatasourceApi.getDynamicResultSet(each, input);
        Assertions.assertNotNull(dynamicResultSet.getErrMsg());

        // case 5: create table
        each = new PrepareEach();
        each.setDatasourceKey(h2dbKey);
        each.setPrepareSql("create table a(a int)");
        final HashMap<String, Object> fields = new HashMap<>();
        fields.putIfAbsent("name", "Bob");
        fields.putIfAbsent("age", 18);
        input.add(fields);
        Map<Integer, PrepareDict> placeHolds = new HashMap<>();
        placeHolds.put(0, new PrepareDict("name", 1, "STRING"));
        each.setIndex(placeHolds);

        dynamicResultSet = dynamicDatasourceApi.getDynamicResultSet(each, input);
        Assertions.assertNull(dynamicResultSet.getErrMsg());
        each = new PrepareEach();
        each.setDatasourceKey(h2dbKey);
        each.setPrepareSql("INSERT INTO a (a) VALUES (1);");
        dynamicResultSet = dynamicDatasourceApi.getDynamicResultSet(each, input);
        Assertions.assertNull(dynamicResultSet.getErrMsg());
        each = new PrepareEach();
        each.setDatasourceKey(h2dbKey);
        each.setPrepareSql("SELECT * FROM a");
        dynamicResultSet = dynamicDatasourceApi.getDynamicResultSet(each, input);
        log.info("query--{}", dynamicResultSet.getResult());
        Assertions.assertNull(dynamicResultSet.getErrMsg());


        // case 6: error branch
        each = new PrepareEach();
        each.setDatasourceKey(h2dbKey);
        each.setPrepareSql("create table_error a(a int)");
        PrepareEach finalEach = each;
        Assertions.assertThrows(RuntimeException.class, () -> dynamicDatasourceApi.getDynamicResultSet(finalEach, input));

        // tech.finovy.framework.datasource.api.DynamicDatasourceApi.getDynamicResultSet(tech.finovy.framework.datasource.entity.PrepareEachList, java.util.List<java.util.Map<java.lang.String,java.lang.Object>>)
        // case 7:
        PrepareEach eachB = new PrepareEach();
        eachB.setDatasourceKey(h2dbKey);
        eachB.setPrepareSql("create table B1(a int)");
        PrepareEach eachC = new PrepareEach();
        eachC.setDatasourceKey(h2dbKey);
        eachC.setPrepareSql("create table C1(a int)");
        final PrepareEachList prepareEachList = new PrepareEachList();
        eachB.setNeedCache(true);
        eachB.setInputKey("test");
        prepareEachList.setPrepareEaches(Lists.newArrayList(eachB, eachC));
        dynamicResultSet = dynamicDatasourceApi.getDynamicResultSet(prepareEachList, input);
        Assertions.assertNull(dynamicResultSet.getErrMsg());
        // case 8: error branch
        PrepareEach eachD = new PrepareEach();
        eachD.setDatasourceKey(h2dbKey);
        eachD.setPrepareSql("create table_error a(a int)");
        prepareEachList.setPrepareEaches(Lists.newArrayList(eachB, eachC, eachD));
        Assertions.assertThrows(RuntimeException.class, () -> dynamicDatasourceApi.getDynamicResultSet(prepareEachList, input));
        prepareEachList.setPrepareEaches(Lists.newArrayList());
        dynamicDatasourceApi.getDynamicResultSet(prepareEachList, input);
    }

    @Test
    public void TestExePst() throws SQLException {
        final Connection connection = Mockito.mock(Connection.class);
        final PrepareEach each = Mockito.mock(PrepareEach.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Map<Integer, PrepareDict> indexMap = new HashMap<>();
        final PrepareDict dict = new PrepareDict();
        dict.setColumnIndex(0);
        dict.setColumnType("STRING");
        dict.setColumnName("name");
        dict.setDefaultValue("test");
        indexMap.put(0, dict);
        when(each.getIndex()).thenReturn(indexMap);
        when(each.getPrepareSql()).thenReturn("select * from a");
        when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        // empty branch
        dynamicDatasourceApi.exePst(each, new ArrayList<>(), connection);
        //
        final HashMap<String, Object> mapA = new HashMap<>();
        mapA.put("A", "A");
        dynamicDatasourceApi.exePst(each, Lists.newArrayList(mapA), connection);
        final HashMap<String, Object> mapB = new HashMap<>();
        mapB.put("B", "B");
        dynamicDatasourceApi.exePst(each, Lists.newArrayList(mapA, mapB), connection);
        dict.setMustInput(true);
        Assertions.assertThrows(RuntimeException.class, () -> dynamicDatasourceApi.exePst(each, Lists.newArrayList(mapA), connection));
        Assertions.assertThrows(RuntimeException.class, () -> dynamicDatasourceApi.exePst(each, Lists.newArrayList(mapA, mapB), connection));

    }

    @Test
    public void TestExeProc() throws SQLException {
        final Connection connection = Mockito.mock(Connection.class);
        final PrepareEach each = Mockito.mock(PrepareEach.class);
        final CallableStatement callableStatement = Mockito.mock(CallableStatement.class);
        String sql = "SELECT * FROM user where id = ?;";
        when(each.getPrepareSql()).thenReturn(sql);
        when(connection.prepareCall(sql)).thenReturn(callableStatement);
        when(callableStatement.execute()).thenReturn(true);
        // empty
        final PreparedStatement A = dynamicDatasourceApi.exeProc(each, null, connection);
        Assertions.assertNotNull(A);


        Map<Integer, PrepareDict> indexMap = new HashMap<>();
        final PrepareDict dict = new PrepareDict();
        dict.setColumnIndex(0);
        dict.setColumnType("STRING");
        dict.setColumnName("name");
        dict.setDefaultValue("test");
        indexMap.put(0, dict);
        when(each.getIndex()).thenReturn(indexMap);
        final PreparedStatement B = dynamicDatasourceApi.exeProc(each, new ArrayList<>(), connection);
        Assertions.assertNotNull(B);

        List<Map<String, Object>> input = new ArrayList<>();
        final HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Alice");
        input.add(map);
        final PreparedStatement C = dynamicDatasourceApi.exeProc(each, input, connection);
        Assertions.assertNotNull(C);

        map.put("name", null);
        dict.setMustInput(true);
        boolean error = false;
        try {
            dynamicDatasourceApi.exeProc(each, input, connection);
        } catch (Exception e) {
            error = true;
        }
        Assertions.assertTrue(error);

        dict.setMustInput(false);
        final PreparedStatement F = dynamicDatasourceApi.exeProc(each, input, connection);
        Assertions.assertNotNull(F);

        // for commit
        when(each.isCommit()).thenReturn(true);
        when(each.getIndex()).thenReturn(new HashMap<>());
        Assertions.assertNotNull(dynamicDatasourceApi.exeProc(each, null, connection));
    }

    @Test
    public void testSetPrepareStatement() {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        // byte
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "BYTE", 0, Byte.valueOf("12"));
        // short
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "SHORT", 0, Short.valueOf("12"));
        // int
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "INT", 0, Integer.valueOf("12"));
        // long
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "LONG", 0, Long.valueOf("12"));
        // float
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "FLOAT", 0, Float.valueOf("12"));
        // double
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "DOUBLE", 0, Double.valueOf("12"));
        // boolean
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "BOOLEAN", 0, Boolean.valueOf("false"));
        // char
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "CHAR", 0, "f");
        // string
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "STRING", 0, "f");
        // null
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "NULL", 0, "f");
        // default
        dynamicDatasourceApi.setPreparedStatement(preparedStatement, "DATE", 0, "2023-01-01");
    }


    @Test
    public void testInit() {
        PrepareEach each = new PrepareEach();
        List<Map<String, Object>> input = new ArrayList<>();

        each.setDatasourceKey(h2dbKey);
        each.setPrepareSql("create table B(a varchar(20))");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO B (a) VALUES ('1');");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM B");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // BIGINT
        each.setPrepareSql("create table C(a BIGINT)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO C (a) VALUES (1);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM C");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // TINYINT
        each.setPrepareSql("create table D(a TINYINT)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO D (a) VALUES (1);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM D");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // SMALLINT
        each.setPrepareSql("create table E(a SMALLINT)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO E (a) VALUES (1);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM E");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // BIT
        each.setPrepareSql("create table F(a BIT)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO F (a) VALUES (1);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM F");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // BOOLEAN
        each.setPrepareSql("create table G(a Boolean)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO G (a) VALUES (false);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM G");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // BLOB
        each.setPrepareSql("create table H(a BLOB)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO H (a) VALUES (1);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM H");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // DOUBLE
        each.setPrepareSql("create table I(a DOUBLE)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO I (a) VALUES (1);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM I");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // FLOAT
        each.setPrepareSql("create table J(a FLOAT)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO J (a) VALUES (1);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM J");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // NVARCHAR
        each.setPrepareSql("create table K(a NVARCHAR)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO K (a) VALUES ('1');");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM K");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // VARCHAR
        each.setPrepareSql("create table L(a VARCHAR)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO L (a) VALUES (1);");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM L");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // DATE
        each.setPrepareSql("create table M(a DATE)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO M (a) VALUES ('2023-10-30');");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM M");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        // TIMESTAMP
        each.setPrepareSql("create table N(a TIMESTAMP)");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("INSERT INTO N (a) VALUES ('2023-10-30');");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("SELECT * FROM N");
        each.setNeedCache(true);
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("UPDATE N set a = '2023-10-31';");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("DELETE FROM N where a = '2023-10-31';");
        dynamicDatasourceApi.getDynamicResultSet(each, input);

        // test procedure
        each.setPrepareSql("CREATE ALIAS my_procedure AS $$\n" +
                "String myProcedure(String input) {\n" +
                "    return \"Hello, \" + input;\n" +
                "}\n" +
                "$$;\n");
        dynamicDatasourceApi.getDynamicResultSet(each, input);
        each.setPrepareSql("call my_procedure('test')");
        each.setProcedure(true);
        dynamicDatasourceApi.getDynamicResultSet(each, input);
    }

    @Test
    public void TestErrorBranch() throws SQLException {
        // test commit error
        PrepareEach each = new PrepareEach();
        List<Map<String, Object>> input = new ArrayList<>();
        each.setDatasourceKey(h2dbKey);
        each.setPrepareSql("DELETE FROM N where a == '2023-10-31';");
        Assertions.assertThrows(RuntimeException.class, () -> dynamicDatasourceApi.getDynamicResultSet(each, input));

        // close error
        ResultSet resultSet = mock(ResultSet.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final Connection connection = mock(Connection.class);
        when(resultSet.isClosed()).thenThrow(new SQLException());
        DynamicDatasourceImpl.closeResource(resultSet, preparedStatement, connection);
        when(preparedStatement.isClosed()).thenThrow(new SQLException());
        DynamicDatasourceImpl.closeResource(resultSet, preparedStatement, connection);
        when(connection.isClosed()).thenThrow(new SQLException());
        DynamicDatasourceImpl.closeResource(resultSet, preparedStatement, connection);
    }
}
