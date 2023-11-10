package tech.finovy.framework.config.nacos.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.finovy.framework.config.nacos.listener.entity.ShardingEngineExecutionConstant;
import tech.finovy.framework.config.nacos.listener.entity.TaskCallEntity;
import tech.finovy.framework.config.nacos.listener.entity.TaskFlowEntity;
import tech.finovy.framework.config.nacos.listener.entity.TaskFlowGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CustomConfigDefinitionListener extends AbstractNacosConfigGroupDefinitionListener<TaskFlowGroup, TaskFlowEntity> {

    private final Map<String, TaskCallEntity> allTaskCallEntity = new HashMap<>();
    private final TaskCallEntity defaultTaskCallEntity = new TaskCallEntity();
    private final TaskFlowEntity defaultTaskFlowEntity = new TaskFlowEntity();
    @Autowired
    private CustomConfiguration shardingEngineConfiguration;

    public CustomConfigDefinitionListener() {
        super(TaskFlowGroup.class, TaskFlowEntity.class, new TaskFlowEntity());
    }

    @Override
    public String getDataId() {
        return shardingEngineConfiguration.getShardingExecutionDataId();
    }

    @Override
    public String getDataGroup() {
        return shardingEngineConfiguration.getShardingDataGroup();
    }

    @Override
    public void refresh(String dataId, String dataGroup, TaskFlowGroup config, int version) {
        super.refresh(dataId, dataGroup, config, version);
        for (TaskFlowEntity entity : config.getTaskFlow()) {
            entity.setExists(true);
            if (entity.getTimeout() == 0) {
                entity.setTimeout(10000);
            }
            List<TaskCallEntity> call = entity.getCall();
            if (config.isCache()) {
                entity.setCache(true);
            }
            if (config.isDatabase()) {
                entity.setDatabase(true);
            }
            if (config.isQueue()) {
                entity.setQueue(true);
            }
            if (config.isRocket()) {
                entity.setRocket(true);
            }
            if (call == null) {
                entity.setCall(new ArrayList<>());
                continue;
            }
            for (TaskCallEntity c : call) {
                c.setExists(true);
                if (StringUtils.isBlank(c.getKey())) {
                    c.setKey(DigestUtils.sha3_256Hex(JSON.toJSONBytes(c)));
                }
                if (StringUtils.isBlank(c.getPrepare())) {
                    c.setPrepare(ShardingEngineExecutionConstant.TCC_CALL_PREPARE);
                }
                if (StringUtils.isBlank(c.getCommit())) {
                    c.setCommit(ShardingEngineExecutionConstant.TCC_CALL_COMMIT);
                }
                if (StringUtils.isBlank(c.getRollback())) {
                    c.setRollback(ShardingEngineExecutionConstant.TCC_CALL_ROLLBACK);
                }
                allTaskCallEntity.put(String.join(".", entity.getKey(), c.getKey()), c);
            }

        }
    }

    public Map<String, TaskCallEntity> getAllTaskCallEntity() {
        return allTaskCallEntity;
    }

    public Map<String, TaskFlowEntity> getAllTaskFlowEntity() {
        return ABSTRACT_NACOS_DEFINITION_REPOSITORY_MAP;
    }

    public TaskCallEntity getTaskCallEntity(String flow, String call) {
        return allTaskCallEntity.getOrDefault(String.join(".", flow, call), defaultTaskCallEntity);
    }

    public TaskFlowEntity getTaskFlowEntity(String key) {
        return ABSTRACT_NACOS_DEFINITION_REPOSITORY_MAP.getOrDefault(key, defaultTaskFlowEntity);
    }

    @Override
    public int getOrder() {
        return super.getOrder() + 20;
    }
}
