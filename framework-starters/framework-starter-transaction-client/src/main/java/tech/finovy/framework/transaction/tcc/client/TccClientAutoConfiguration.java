package tech.finovy.framework.transaction.tcc.client;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import tech.finovy.framework.transaction.tcc.client.api.TccActionService;
import tech.finovy.framework.transaction.tcc.client.api.TccClientService;
import tech.finovy.framework.transaction.tcc.client.api.event.HeartBeatEvent;
import tech.finovy.framework.transaction.tcc.client.core.*;
import tech.finovy.framework.transaction.tcc.client.fence.TCCFenceConfig;
import tech.finovy.framework.transaction.tcc.client.listener.EventDispatcher;
import tech.finovy.framework.transaction.tcc.client.loadbalance.Instance;
import tech.finovy.framework.transaction.tcc.client.loadbalance.InstanceTable;
import tech.finovy.framework.transaction.tcc.client.log.Log;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static tech.finovy.framework.transaction.tcc.client.constant.Constants.RPC_GROUP_KEY;


@Configuration(proxyBeanMethods = false)
@Import(TccClientProperties.class)
public class TccClientAutoConfiguration<T> {

    @Value("${tcc.client.health-check-enable:true}")
    private boolean isEnableHealthCheck;

    private final List<TccClientService<T>> services;
    private final DataSource datasource;

    public TccClientAutoConfiguration(@Nullable List<TccClientService<T>> services, @Nullable DataSource datasource) {
        this.services = services;
        this.datasource = datasource;
    }

    @Bean
    public ServiceContainer<T> serviceContainer() {
        return new ServiceContainer<>(services);
    }

    @Bean
    @SneakyThrows
    public TccActionService tccActionService(NacosConfigManager nacosConfigManager, TccClientProperties properties) {
        // init
        final String config = nacosConfigManager.getConfigService().getConfigAndSignListener(properties.getClusterDataId(), properties.getClusterGroup(), 3000L, new Listener() {
            @Override
            public void receiveConfigInfo(String config) {
                refresh(config);
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });
        refresh(config);
        Log.info("finish init tcc-server-clusters : {}", config);
        // open trigger
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // 开启tcc-server cluster健康监测
            EventDispatcher.dispatch(new HeartBeatEvent());
        }, properties.getHealthCheckInitialDelay(), properties.getHealthCheckPeriod(), TimeUnit.SECONDS);
        Log.info("finish init tcc-server-clusters healthcheck...");
        return new TccActionServiceImpl();
    }

    private void refresh(String config) {
        if (StringUtils.isNotEmpty(config)) {
            if (!JSONValidator.from(config).validate()) {
                Log.warn("this config is not legal json-str:{}", config);
                return;
            }
            final List<Instance> instances = JSON.parseArray(config, Instance.class);
            InstanceTable.init(instances);
            Log.info("refresh tcc-server instances:{}", JSON.toJSONString(instances));
        }
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public TCCFenceConfig tccFenceConfig(
            PlatformTransactionManager transactionManager) {
        return new TCCFenceConfig(datasource, transactionManager);
    }

    @Bean
    public RpcReferenceHolder rpcReferenceHolder() {
        return new RpcReferenceHolder();
    }

    @Bean
    public InnerDispatchHandler innerDispatchHandler(ServiceContainer<T> container) {
        return new InnerDispatchHandler(container);
    }

    @Bean
    @ConditionalOnProperty(RPC_GROUP_KEY)
    @ConditionalOnBean({DataSource.class})
    public RpcDispatchHandler rpcDispatchHandler(TccClientProperties properties, ServiceContainer<T> container) {
        final RpcDispatchHandler dispatchHandler = new RpcDispatchHandler(container);
        // 注册为dubbo接口
        ServiceConfig<DispatchHandler> service = new ServiceConfig<>();
        service.setInterface(DispatchHandler.class);
        service.setRef(dispatchHandler);
        service.setGroup(properties.getGroup());
        final DubboBootstrap instance = DubboBootstrap.getInstance();
        instance.service(service);
        return dispatchHandler;
    }

    @Bean
    @ConditionalOnBean({InnerDispatchHandler.class, RpcReferenceHolder.class})
    public ServletRegistrationBean<TccClientServlet> servletRegistrationBean(@Nullable DataSource datasource, InnerDispatchHandler innerDispatcher, RpcReferenceHolder rpcReferenceHolder) {
        final TccClientServlet tccClientServlet = new TccClientServlet(datasource, innerDispatcher, rpcReferenceHolder);
        return new ServletRegistrationBean<>(tccClientServlet, "/tcc/*");
    }

}
