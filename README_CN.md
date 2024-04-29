

[TOC]

### framework-cloud-parent

##### 作用:

作为项目父依赖，用于管理通用组件依赖版本

##### 使用方式:

```xml
<parent>
   <groupId>tech.finovy</groupId>
   <artifactId>frameowkr-cloud-parent</artifactId>
   <version>0.1.0</version>
</parent>
```

### framework-global

#### framework-global-constant

##### 作用:

定义了项目常用的常量，如 traceid。如果是跨项目使用，需要统一整理到此处，避免多处定义。

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-constant</artifactId>
</dependency>
```

#### framework-global-exception

##### 作用:

定义了项目常用的异常。

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-exception</artifactId>
</dependency>
```

#### framework-global-interceptor-app

##### 作用:

定义了接口层常用的全局拦截器，参数解析器等。

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-interceptor-app</artifactId>
</dependency>
```

#### framework-global-interceptor-mybatis

##### 作用:

定义了Mybatis-Plus租户全局拦截器等，用于改写多租户Sql。

##### 使用方式

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-interceptor-mybatis</artifactId>
</dependency>
```

#### framework-global-interceptor-tenant

##### 作用:

定义了常用租户拦截器，用于隐式传递租户id

##### 使用方式

###### 引入依赖

```java
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-interceptor-tenant</artifactId>
</dependency>
```

#### framework-global-tenant-context

##### 作用:

定义全局租户上下文

##### 使用方式

引入依赖

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-tenant-context</artifactId>
</dependency>
```

备注: 一般由其他依赖引入，业务项目基本不需要单独引入。

### framework-starters

#### 1. framework-starter-elasticsearch-client

##### 作用:

用于管理es客户端连接信息

##### 依赖文件:

framework-core-elasticsearch.yaml

##### 使用方法:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-elasticsearch-client</artifactId>
</dependency>
```

###### 注入客户端:

```java
@Autowired
private RestClient client;
```

#### 2. framework-starter-healthcheck

##### 作用:

用于启动及运行时健康检查

##### 原理:

内置Rest接口，用于检查服务是否存活

##### 使用方法:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-healthcheck</artifactId>
</dependency>
```

###### K8s配置健康检查:

```xml
liveness: /healthcheck/liveness
readiness: /healthcheck/readiness
```

#### 3. framework-starter-http

##### 作用:

用于配置http客户端, 集成基于服务发现的负载均衡功能

##### 使用方法:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-http</artifactId>
</dependency>
```

###### 注入依赖:

```java
@Autowired
private HttpTemplateService<RestTemplate> httpTemplateService;
```

###### 代码调用:

```java
HttpHeaders headers = new HttpHeaders();
headers.set("TX_ID", "123");
headers.setContentType(MediaType.APPLICATION_JSON);
headers.set(HttpHeaders.ACCEPT_ENCODING, MediaType.ALL_VALUE);
HttpEntity httpEntity = new HttpEntity<>("{}", headers);
String api = "https://www.finovy.tech/check";
HttpTemplatePack<RestTemplate> httpTemplatePack = httpTemplateService.choice(api);
ResponseEntity<String> response = httpTemplatePack.getRestTemplate().exchange(httpTemplatePack.getHost() + "/" + "rollback", HttpMethod.POST, httpEntity, String.class);
```

#### 4. framework-starter-http-logappender

##### 作用:

集成服务日志统一收集功能，通过http发送至收集端

##### 原理:

基于logback，自定义 `tech.finovy.framework.logappender.LogHttpAppender`，实现个性化日志收集

##### 使用方法:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-http-logappender</artifactId>
</dependency>
```

###### 修改logback-spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <springProperty scope="context" name="application" source="spring.application.name" defaultValue="application"/>
    <springProperty scope="context" name="endpointUrl" source="logging.endpoint-url" defaultValue=""/>
    <springProperty scope="context" name="accessKeyId" source="logging.access-key" defaultValue=""/>
    <springProperty scope="context" name="accessKeySecret" source="logging.access-secret" defaultValue=""/>
    <springProperty scope="context" name="logStore" source="logging.log-store" defaultValue="log-store"/>
    <springProperty scope="context" name="project" source="logging.project" defaultValue="app-project"/>
    <springProperty scope="context" name="topic" source="logging.topic" defaultValue="app-log"/>
    <springProperty scope="context" name="consoleLevel" source="logging.console-level" defaultValue="WARN"/>
    <springProperty scope="context" name="fileLevel" source="logging.file-level" defaultValue="WARN"/>
    <springProperty scope="context" name="hostName" source="logging.host-name" defaultValue=""/>
    <contextName>${application}</contextName>
    <shutdownHook class="ch.qos.logback.core.hook.DelayingShutdownHook"/>
		<!-- 自定义appender -->
    <appender name="http" class="tech.finvoy.framework.logappender.LogHttpAppender">
        <endpoint>${endpointUrl}</endpoint>
        <accessKeyId>${accessKeyId}</accessKeyId>
        <accessKeySecret>${accessKeySecret}</accessKeySecret>
        <project>${project}</project>
        <logStore>${application}</logStore>
        <appName>${application}</appName>
        <topic>${topic}</topic>
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} %line- %msg%n</pattern>
        </encoder>
        <mdcFields>appid,traceid</mdcFields>
    </appender>
  	<appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
						<!-- 控制台输出级别限制 -->
            <level>${consoleLevel}</level>
        </filter>
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} %line- %msg%n</pattern>
        </encoder>
    </appender>
    <logger name="tech.finovy.framework.*" level="INFO" additivity="false">
        <appender-ref ref="console"/>
        <appender-ref ref="http"/>
    </logger>
    <root level="WARN" additivity="false">
        <appender-ref ref="http"/>
        <appender-ref ref="console"/>
    </root>
</configuration>
```

#### 5. framework-starter-mongodb-client

##### 作用:

用于统一管理MongoDB客户端

##### 依赖文件:

framework-core-mongdb.json

###### 文件示例:

```json
{
  "config": [
    {
      "database": "export-system",
      "host": "10.7.0.25",
      "password": "password",
      "port": 27017,
      "username": "uname"
    },
    {
      "database": "import-system",
      "host": "10.7.0.25",
      "password": "password",
      "port": 27017,
      "username": "uname"
    }
  ]
}
```

##### 使用方法:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-mongodb-client</artifactId>
</dependency>
```

代码使用:

```java
@Autowired
private MongodbClientSourceMap clientSourceMap;

        void mongClient() {
        MongoDatabase database = clientSourceMap.getMongodbDatabase("export-system");
        // with API call
        }
```

#### 6. framework-starter-nacos-config

##### 作用:

用于配置中心自动装配，提供更优雅的扩展点

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-nacos-config</artifactId>
</dependency>
```

###### 扩展点:

- 如何动态获取配置变动?

1. 继承 AbstractNacosConfigDefinitionListener.
2. 将 CustomeConfigurationDefinitionListener 注入到Spring容器中.

```java
public class CustomeConfigurationDefinitionListener extends AbstractNacosConfigDefinitionListener<CustomeConfiguration> {

    public CustomeConfigurationDefinitionListener() {
        super(CustomeConfiguration.class);
    }

    @Override
    public String getDataId() {
        return "data-id";
    }

    @Override
    public String getDataGroup() {
        return "data-group";
    }

    @Override
    public void refresh(String dataId, String dataGroup, ProducerConfiguration config, int version) {
        // Get and refresh
    }
}
```



#### 7. framework-starter-nacos-discovery

##### 作用:

用于注册中心自动装配

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-nacos-discovery</artifactId>
</dependency>
```



#### 8. framework-starter-oss-client

##### 作用:

管理oss客户端配置

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-oss-client</artifactId>
</dependency>
```

代码使用:

```java
@Autowired
private OssClientService ossClient;

public void ossClientTest() {
        // Reference API for Get/Put/Delete
        ossClient.getObject("bucketName","ossFileName");
        }
```



#### 9. framework-starter-rabbitmq-client

##### 作用:

管理rabbitmq客户端连接

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-rabbitmq-client</artifactId>
</dependency>
```



#### 10. framework-starter-ratelimiter

##### 作用:

集成guava ratelimiter，用于访问限流

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-ratelimiter</artifactId>
</dependency>
```

###### 代码使用

```java
@Autowired
private DistributedRateLimiterFactoryManager manager;

public void ratelimiterTest(){
// resource ,qps
final boolean test = manager.tryAcquire("resource", 1);
        }
```



#### 11. framework-starter-redis-redissonclient

##### 作用:

用于管理redis客户端配置，并提供多种扩展方式

##### 依赖配置:

###### 配置名(默认):

framework-core-redis

备注: 可参考 `tech.finovy.framework.redisson.autoconfigure.RedissonProperties` 进行自定义配置.

###### 配置示例:

```yaml
sentinelServersConfig:
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  password: password
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  slaveConnectionMinimumIdleSize: 32
  slaveConnectionPoolSize: 64
  masterConnectionMinimumIdleSize: 32
  masterConnectionPoolSize: 64
  readMode: "SLAVE"
  sentinelAddresses:
    - "redis://10.16.2.64:26379"
    - "redis://10.16.2.65:26379"
    - "redis://10.16.2.66:26379"
  masterName: "master"
  database: 0
threads: 0
nettyThreads: 0
```

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-redis-redissonclient</artifactId>
</dependency>
```

###### 代码使用:

参阅 `package tech.finovy.framework.redisson.api`, 如下仅举例说明。

- RedissonClientInterface  : 扩展了 RedissonClient
- CacheApi                            : 基于Redis的缓存API
- DistributedIdApi               : 基于Redis的分布式ID实现(兼容旧项目，新项目建议迁移至专用服务)，已停止维护
- DistributedLockApi          : 基于Redisson的分布式锁实现
- LocalCacheMapApi          : 本地缓存实现
- MapApi                              : 基于Redis的Map操作实现

```java
// Example 1
@Autowired
private CacheApi cacheApi;

        void test(){
        String cacheKey = "testCache-00";
        List<String> keys = new ArrayList<>();
        List<SerialCache> batchCache = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
        SerialCache cachePack = new SerialCache(cacheKey + x);
        keys.add(cacheKey + x);
        cachePack.setCacheData(txt + "-----------" + x);
        batchCache.add(cachePack);
        }
        cacheApi.putSerialCache(batchCache);
        }

// Example 2
@Autowired
private RedissonClientInterface redissonClientInterface;

        void test(){
        int x = redissonClientInterface.calHash("test");
        }
```

如果要获取redis client，请使用 RedisContextHolder.get().getClient()

#### 12. framework-starter-rocketmq-consumer

##### 作用:

管理rocketmq consumer

##### 依赖配置:

```yaml
rocketmq:
  nameserver: 10.16.2.60:9876;10.16.2.61:9876
```

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-rocketmq-consumer</artifactId>
</dependency>
```

###### 代码使用:

```java
@Autowired
private RocketMqConsumer rocketMqConsumer;

        void testRocketMqConsumerClient() {
final DefaultMQPushConsumer consumer = rocketMqConsumer.getConsumer("TEST","MQ-TEST","*");
        consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> ConsumeConcurrentlyStatus.CONSUME_SUCCESS);
        consumer.start();
        }
```

#### 13. framework-starter-rocketmq-producer

##### 作用:

管理rocketmq producer

##### 依赖配置:

```yaml
rocketmq:
  nameserver: 10.16.2.60:9876;10.16.2.61:9876
```

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-rocketmq-producer</artifactId>
</dependency>
```

###### 代码使用

```
@Autowired
private QueueService queueService;

void test() {
    // only init connection
    final QueueSerialMessage message = new QueueSerialMessage();
    message.setTransactionId("TX001);
    message.setTopic("MQ-TEST");
    message.setTags("*");
    message.setBody("for test");
    final PushResult result = queueService.pushSerial(message);
}
```

#### 14. framework-starter-cloud

##### 作用:

快速接入spring-cloud，集成了nacos 配置、服务发现，服务健康检查，日志收集，服务容器等依赖

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-cloud</artifactId>
</dependency>
```

#### 15. framework-starter-dubbo

##### 作用:

快速接入dubbo，集成了dubbo，及dubbo和nacos集成的依赖

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-dubbo</artifactId>
</dependency>
```

#### 16. framework-starter-environment

##### 作用:

用于数据库字段解密，设置到spring环境中。

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-enviroment</artifactId>
</dependency>
```

#### 17. framework-starter-mybatis

##### 作用:

快速集成mybatis依赖

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-mybatis</artifactId>
</dependency>
```

#### 18. framework-starter-oidc-client

##### 作用:

快速对接oidc协议的三方登录服务端。如keycloak。

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-oidc-client</artifactId>
</dependency>
```

###### 代码使用:

```yaml
spring:
  security:
    oauth2:
      client:
        # (必需)自定义登录页地址
        login-page: /login
        default-success-url: /host-management/resource-list
        logout-url: /logout
        # 不需要鉴权的接口
        pass-paths:
          - /test1
        registration:
          # 目前仅支持keycloak，可以提需求扩展
          keycloak:
            # (必需)id & secret (需要向keycloak管理员申请)
            client-id: local_test_openid
            client-secret: hqO49I1YtmKr4x74ODN4DnF97iuLr875
```

**地址配置:**

1. 客户端触发登录地址: [http://127.0.0.1:8080/oauth2/authorization/keycloak](http://127.0.0.1:8082/oauth2/authorization/keycloak)
2. 提供给keycloak的回调地址: https://127.0.0.1:8080/login/oidc/code/keycloak

**扩展应用:**

1. 如果你想获取登录成功的用户信息,注入依赖 tech.finovy.framework.security.oidc.extend.UserDetailService。
2. 如果想在用户登录成功扩展逻辑，实现 tech.finovy.framework.security.oidc.extend.AuthorizationCallbackHandler。

#### 19. framework-starter-seata

##### 作用:

集成Seata客户端依赖，用于分布式事务相关。因涉及多种模式，代码使用方面参考官方文档 https://seata.io/zh-cn/docs/overview/what-is-seata.html

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-seata</artifactId>
</dependency>
```

#### 20. framework-starter-sentinel-cloud

##### 作用:

集成sentinel MVC客户端依赖，用于Web层限流(Http)

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-sentinel-cloud</artifactId>
</dependency>
```

#### 21. framework-starter-sentinel-dubbo

##### 作用:

集成sentinel dubbo客户端依赖，用于RPC层限流

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-sentinel-dubbo</artifactId>
</dependency>
```

#### 22. framework-starter-interceptor-app

##### 作用:

用于token判断拦截。(兼容旧版本)

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-interceptor-app</artifactId>
</dependency>
```

###### 代码使用:

将 tech.finovy.framework.global.interceptor.SessionInterceptor 添加至 WebMvcConfigurer 拦截器链

#### 23. framework-starter-interceptor-mybatis

##### 作用:

用于mybatis层面租户拦截注入。(兼容旧版本)

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-interceptor-mybatis</artifactId>
</dependency>
```

#### 24. framework-starter-interceptor-tenant

##### 作用:

用于mybatis层面租户拦截注入,默认租户字段为appid。(兼容旧版本)

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-interceptor-tenant</artifactId>
</dependency>
```

#### 25. framework-starter-local-cache

##### 作用:

集成本地缓存依赖

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-local-cache</artifactId>
</dependency>
```

###### 代码使用:

参照 tech.finovy.framework.cache.local.api.LocalCacheService

#### 26. framework-starter-datasource-dynamic

##### 作用:

集成多数据源依赖，维护了多个数据源连接池。

##### 依赖配置:

framework-dynamic-datasource

```json
{
    "config": [
        {
            "asyncInit": false,
            "encrypt": true,
            "key": "user_db",
            "password": "password",
            "poolPreparedStatements": true,
            "url": "url",
            "username": "username"
        }
    ]
}
```

##### 使用方式

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-datasource-dynamic</artifactId>
</dependency>
```

###### 代码使用:

注入 DynamicDataSourceMap，使用此类API获取相关连接池

#### 27. framework-starter-disruptor

##### 作用:

集成disruptor

##### 使用方式

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-disruptor</artifactId>
</dependency>
```

###### 代码使用:

创建DefaultDisruptorEngineProvider:

```
// DisruptorEventConfiguration - 自定义disruptor配置
final DefaultDisruptorEngineProvider engineProvider = new DefaultDisruptorEngineProvider(configuration);
```

使用 tech.finovy.framework.disruptor.spi.DisruptorEngine  API 处理事件相关

#### 28. framework-starter-event

##### 作用:

集成事件依赖

##### 使用方式

###### 引入依赖:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-event</artifactId>
</dependency>
```

###### 代码使用

参照

- tech.finovy.framework.distributed.event.api.AsyncEventService
- tech.finovy.framework.distributed.event.api.EventService
