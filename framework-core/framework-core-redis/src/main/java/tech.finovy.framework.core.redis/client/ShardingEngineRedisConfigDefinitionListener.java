package tech.finovy.framework.core.redis.client;

import tech.finovy.framework.config.nacos.listener.AbstractNacosConfigDefinitionListener;
import tech.finovy.framework.core.redis.ShardingEngineRedisConext;
import tech.finovy.framework.core.redis.holder.ShardingEngineRedisConextHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ShardingEngineRedisConfigDefinitionListener extends AbstractNacosConfigDefinitionListener<Config> {

    @Autowired
    private RedissonConfiguration redissonConfiguration;
    private ShardingEngineRedisConext shardingEngineRedisConext = ShardingEngineRedisConextHolder.get();

    public ShardingEngineRedisConfigDefinitionListener() {
        super(Config.class);
    }

    @Override
    public String getDataId() {
        return redissonConfiguration.getRedisDataId();
    }

    @Override
    public String getDataGroup() {
        return redissonConfiguration.getRedisDataGroup();
    }

    @Override
    public void refresh(String dataId, String dataGroup, Config config, int refreshCount) {
        shardingEngineRedisConext.refreshClient(redissonConfiguration, config, refreshCount);
    }

    @Override
    public Config parseObject(String config, int refreshCount) {
        try {
            return ConfigExtImpl.fromYAML(config);
        } catch (IOException e) {
            log.error(e.toString());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return super.getOrder() + 10;
    }
}
