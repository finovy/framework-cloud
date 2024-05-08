package tech.finovy.framework.config.nacos.listener;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NacosConfigDefinitionListenerTest {


    private String dataId = "DEFAULT";

    @Test
    void testIsAsync() {
        AbstractNacosConfigDefinitionListener listener = new AbstractNacosConfigDefinitionListener<>(String.class,
                dataId, "dataGroup", null) {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, String config, int version) {

            }
        };

        Assertions.assertNotNull(listener);
        Assertions.assertFalse(listener.isAsync());
        Assertions.assertTrue(listener.isEnable());
        Assertions.assertNull(listener.getIndex());
        Assertions.assertEquals("DEFAULT_NAMESPACE", listener.getNameSpace());
        // case:
        Assertions.assertNull(listener.parseObject(null, 1));
        String config = "{}";
        // text
        dataId = "test.text";
        listener = new AbstractNacosConfigDefinitionListener<>(String.class,
                dataId, "dataGroup", "namespace") {
            @Override
            public String getDataId() {
                return null;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, String config, int version) {

            }
        };
        final Object A = listener.parseObject(config, 1);
        Assertions.assertNotNull(A);
        dataId = "test.json";
        listener = new AbstractNacosConfigDefinitionListener<>(String.class,
                dataId, "dataGroup", "namespace") {
            @Override
            public String getDataId() {
                return null;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, String config, int version) {

            }
        };
        final Object B = listener.parseObject(config, 1);
        Assertions.assertNotNull(B);

        dataId = "test.yaml";
        listener = new AbstractNacosConfigDefinitionListener<>(String.class,
                dataId, "dataGroup", "namespace") {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, String config, int version) {

            }
        };
        final Object C = listener.parseObject(config, 1);
        Assertions.assertNotNull(C);

        // onReceive
        listener.onReceive(null, 1);
        config = "[}";
        final Object D = listener.parseObject(config, 1);
        Assertions.assertNull(D);

        config = "[]";
        final String E = (String)listener.parseObject(config, 1);
        Assertions.assertTrue(E.equals(""));
        listener.onError(dataId,"group","","mock exception");
    }


    @Test
    public void testParseObject() throws JSONException {
        // case 1:
        AbstractNacosConfigDefinitionListener A = new AbstractNacosConfigDefinitionListener<>(String.class,
                dataId, "dataGroup", null) {
            @Override
            public String getDataId() {
                return null;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, String config, int version) {

            }
        };
        Assertions.assertNull(A.parseObject(null, 1));
        //
        dataId = "test.text";
        AbstractNacosConfigDefinitionListener<String> B = new AbstractNacosConfigDefinitionListener<>(String.class,
                dataId, "dataGroup", null) {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, String config, int version) {

            }
        };
        Assertions.assertEquals("just test txt", B.parseObject("just test txt", 1));

        dataId = "test.json";
        AbstractNacosConfigDefinitionListener<JSONObject> C= new AbstractNacosConfigDefinitionListener<>(JSONObject.class,
                dataId, "dataGroup", null) {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, JSONObject config, int version) {

            }
        };
        final JSONObject resultC = C.parseObject("{\"name\":\"Alice\"}", 1);
        Assertions.assertEquals("Alice", resultC.getString("name"));

        dataId = "test.yaml";
        AbstractNacosConfigDefinitionListener<JSONObject> D= new AbstractNacosConfigDefinitionListener<>(JSONObject.class,
                dataId, "dataGroup", null) {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, JSONObject config, int version) {

            }
        };
        final JSONObject resultD = D.parseObject("{\"name\":\"Alice\"}", 1);
        Assertions.assertEquals("Alice", resultD.getString("name"));
    }

    @Test
    void testOnReceive() {
        AbstractNacosConfigDefinitionListener<String> listener = new AbstractNacosConfigDefinitionListener<>(String.class,
                "test.text", "dataGroup") {
            @Override
            public String getDataId() {
                return "test.text";
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, String config, int version) {
                throw new RuntimeException("test");
            }
        };
        listener.onReceive(null, 1);
        listener.onReceive("test", 1);
    }

    @Test
    void testParse(){
        AbstractNacosConfigDefinitionListener listener = new AbstractNacosConfigDefinitionListener<>(Integer.class,
                "test.json", "dataGroup") {
            @Override
            public String getDataId() {
                return "test.json";
            }

            @Override
            public String getDataGroup() {
                return null;
            }

            @Override
            public void refresh(String dataId, String dataGroup, Integer config, int version) {

            }
        };
        // error branch
        listener.onReceive("{\"name\":\"Alice\"]",1);
        listener.onReceive("{\"name\":\"Alice\"}",1);
    }
}
