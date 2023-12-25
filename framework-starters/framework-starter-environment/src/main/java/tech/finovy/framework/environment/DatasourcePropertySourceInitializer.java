package tech.finovy.framework.environment;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import tech.finovy.framework.common.SecurityEncryption;

import java.io.Serial;
import java.util.*;

public class DatasourcePropertySourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatasourcePropertySourceInitializer.class);

    protected static final Set<String> EN_KEY = new HashSet<>() {
        @Serial
        private static final long serialVersionUID = 8044060882721124682L;

        {
            add("spring.datasource.url");
            add("spring.datasource.username");
            add("spring.datasource.password");
            add("spring.shardingsphere.datasource.fund.url");
            add("spring.shardingsphere.datasource.fund.username");
            add("spring.shardingsphere.datasource.fund.password");
            add("spring.shardingsphere.datasource.rebate.url");
            add("spring.shardingsphere.datasource.rebate.username");
            add("spring.shardingsphere.datasource.rebate.password");
        }
    };

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 11;
    }

    @SneakyThrows
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String secret = (String) environment.getSystemEnvironment().getOrDefault("DCONF_SECRET", "");
        String iv = (String) environment.getSystemEnvironment().getOrDefault("DCONF_IV", "");
        if (StringUtils.isEmpty(secret) || StringUtils.isEmpty(iv)) {
            LOGGER.info("DCONF_SECRET/IV NOT EXIST");
            return;
        }
        for (PropertySource<?> p : environment.getPropertySources()) {
            if (p.getSource() instanceof LinkedHashMap || p.getSource() instanceof HashMap) {
                for (String enKey : EN_KEY) {
                    if (p.containsProperty(enKey)) {
                        Map<String, Object> property = (Map<String, Object>) p.getSource();
                        try {
                            property.put(enKey, SecurityEncryption.decrypt(String.valueOf(p.getProperty(enKey)), secret, iv));
                            LOGGER.info("DESCRIPTION " + enKey);
                        } catch (Exception e) {
                            LOGGER.warn("Please pay attention! {} decrypt is error:{}, May your source is not encrypted.", enKey, e.getMessage());
                        }
                    }
                }
            }
        }
    }

}
