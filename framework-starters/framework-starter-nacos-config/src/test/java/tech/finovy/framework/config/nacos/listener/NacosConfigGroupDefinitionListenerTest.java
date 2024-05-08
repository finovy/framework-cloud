package tech.finovy.framework.config.nacos.listener;

import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.config.nacos.listener.entity.TaskFlowEntity;
import tech.finovy.framework.config.nacos.listener.entity.TaskFlowGroup;
import tech.finovy.framework.nacos.entity.AbstractNacosConfigGroup;

class NacosConfigGroupDefinitionListenerTest {

    private String dataId = "DEFAULT";

    @Test
    public void testConstructor() {
        AbstractNacosConfigGroupDefinitionListener<TaskFlowGroup, TaskFlowEntity> listener = new AbstractNacosConfigGroupDefinitionListener<>(TaskFlowGroup.class, TaskFlowEntity.class, new TaskFlowEntity(), dataId, "data-group") {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return "data-group";
            }
        };
        Assertions.assertNotNull(listener);
        Assertions.assertFalse(listener.isAsync());
        Assertions.assertTrue(listener.isEnable());
        Assertions.assertNull(listener.getConfigGroup());
        Assertions.assertNotNull(listener.getDefaultEntity());
        Assertions.assertEquals("DEFAULT_NAMESPACE", listener.getNameSpace());
        Assertions.assertNotNull(listener.getNacosDefinitionRepository());
        Assertions.assertFalse(listener.getEntity("test").isExists());
        // test parseObject
        Assertions.assertNull(listener.parseObject(null, 1));
        String config = "{}";
        // text
        dataId = "test.text";
        listener = new AbstractNacosConfigGroupDefinitionListener<>(TaskFlowGroup.class, TaskFlowEntity.class, new TaskFlowEntity(), dataId, "data-group") {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return "data-group";
            }
        };
        final TaskFlowGroup A = listener.parseObject(config, 1);
        Assertions.assertFalse(A.isQueue());
        dataId = "test.json";
        listener = new AbstractNacosConfigGroupDefinitionListener<>(TaskFlowGroup.class, TaskFlowEntity.class, new TaskFlowEntity(), dataId, "data-group") {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return "data-group";
            }
        };
        final TaskFlowGroup B = listener.parseObject(config, 1);
        Assertions.assertFalse(B.isQueue());
        dataId = "test.yaml";
        listener = new AbstractNacosConfigGroupDefinitionListener<>(TaskFlowGroup.class, TaskFlowEntity.class, new TaskFlowEntity(), dataId, "data-group") {
            @Override
            public String getDataId() {
                return dataId;
            }

            @Override
            public String getDataGroup() {
                return "data-group";
            }
        };
        final TaskFlowGroup C = listener.parseObject(config, 1);
        Assertions.assertFalse(C.isQueue());

        // onReceive
        listener.onReceive(null, 1);
        config = "[}";
        final TaskFlowGroup D = listener.parseObject(config, 1);
        Assertions.assertNull(D);

        config = "[]";
        final TaskFlowGroup E = listener.parseObject(config, 1);
        Assertions.assertNotNull(E);
    }

    @Test
    void testParse(){
        AbstractNacosConfigGroupDefinitionListener listener = new AbstractNacosConfigGroupDefinitionListener(String.class, String.class, new TaskFlowEntity(),
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
            public void refresh(String dataId, String dataGroup, AbstractNacosConfigGroup config, int version) {
                throw new RuntimeException("test");
            }
        };
        // error branch
        listener.onReceive("{\"name\":\"Alice\"]",1);
        listener.onReceive("{\"name\":\"Alice\"}",1);

        //
        AbstractNacosConfigGroupDefinitionListener listenerB = new AbstractNacosConfigGroupDefinitionListener(JSONObject.class, String.class, new TaskFlowEntity(),
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
            public void refresh(String dataId, String dataGroup, AbstractNacosConfigGroup config, int version) {
                throw new RuntimeException("test");
            }
        };
        // error branch
        listenerB.onReceive("{\"name\":\"Alice\"}",1);
    }
}


