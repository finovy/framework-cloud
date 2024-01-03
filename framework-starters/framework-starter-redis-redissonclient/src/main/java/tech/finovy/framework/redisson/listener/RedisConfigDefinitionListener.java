package tech.finovy.framework.redisson.listener;

import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.config.nacos.listener.AbstractNacosConfigDefinitionListener;
import tech.finovy.framework.redisson.RedisClientFactory;
import tech.finovy.framework.redisson.autoconfigure.RedissonProperties;
import tech.finovy.framework.redisson.config.RedissonConfiguration;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.io.IOException;

public class RedisConfigDefinitionListener extends AbstractNacosConfigDefinitionListener<Config> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final RedisContext redisContext = RedisContextHolder.get();
    private final RedissonProperties redissonProperties;
    private final RedissonConfiguration redissonConfiguration;

    public RedisConfigDefinitionListener(RedissonProperties redissonProperties, RedissonConfiguration redissonConfiguration) {
        super(Config.class);
        this.redissonProperties = redissonProperties;
        this.redissonConfiguration = redissonConfiguration;
    }

    @Override
    public String getDataId() {
        return redissonProperties.getNacosDataId();
    }

    @Override
    public String getDataGroup() {
        return redissonProperties.getNacosDataGroup();
    }

    @Override
    public void refresh(String dataId, String dataGroup, Config config, int refreshCount) {
        final RedisContext redisContext = RedisContextHolder.get();
         RedisClientFactory.init(redisContext,redissonConfiguration, config, refreshCount);
    }

    @Override
    public Config parseObject(String config, int refreshCount) {
        try {
            return fromYAML(config);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return super.getOrder() + 10;
    }

    public static Config fromYAML(String content) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromYAML(content, Config.class);
    }
}
