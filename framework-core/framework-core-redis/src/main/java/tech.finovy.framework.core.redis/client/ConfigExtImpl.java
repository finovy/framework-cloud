package tech.finovy.framework.core.redis.client;

import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;

import java.io.IOException;

/**
 * @author dtype.huang
 */
public class ConfigExtImpl extends Config {

    public static Config fromYAML(String content) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromYAML(content, Config.class);
    }

}
