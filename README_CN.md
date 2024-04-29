# Framework Cloud

------

**中文版 | [English](README.md)**

### 目录

- [项目介绍](#项目介绍)
- [功能特性](#功能特性)
- [运行环境](#运行环境)
- [项目结构](#项目结构)
- [快速开始](#快速开始)

### 项目介绍

本项目不涉及业务代码的开发，而是提供一个稳固、高效的开发基础。该框架预定义了一系列依赖版本，确保了整体项目的技术栈一致性和稳定性。除了依赖管理，我们还对一些流行的开源项目进行了功能扩展，以满足更加具体和特殊的业务需求。

### 功能特性

1. **统一的依赖管理**：所有业务项目可以继承此父项目，从而保证所有子项目使用的技术组件版本是一致的，大大降低了版本冲突和技术风险。
2. **开源项目功能扩展**：我们针对市场上流行的一些开源项目，如Nacos，进行了功能扩展，以便更好地适应业务项目的具体需求。
3. **快速开发**：通过使用该框架，业务项目团队可以更加专注于业务逻辑的开发，无需过多关注底层技术实现和版本兼容性问题。

### 运行环境

- JDK 17+
- Spring Cloud 4.0.4
- Spring Cloud Alibaba 2022
- Disruptor 3.4.4
- Nacos 2.3.2

### 项目结构


```tex
framework-cloud
├─framework-common                               : Common Dependencies
│  ├─framework-common-compress                   : 压缩工具扩展
│  ├─framework-common-core                       : 公共基础依赖
│  ├─framework-common-security                   : 安全扩展
│  ├─framework-common-utils                      : 实用工具扩展
├─framework-component  
│  ├─framework-component-scheduler-client        : 调度器客户端扩展
│  ├─framework-component-datasource-dynamic      : 动态数据源扩展
│  ├─framework-component-disruptor               : Disruptor高性能并发编程框架的封装
│  ├─framework-component-elasticsearch           : Elasticsearch搜索引擎的集成与扩展
│  ├─framework-component-event                   : 事件处理扩展，用于应用内事件发布订阅
│  ├─framework-component-http                    : HTTP通信扩展，简化HTTP请求处理
│  ├─framework-component-listener                : 监听器扩展，用于监听特定事件
│  ├─framework-component-local-cache             : 本地缓存实现
│  ├─framework-component-ratelimter              : 速率限制器扩展，控制API访问频率
│  ├─framework-component-redis                   : Redis缓存数据库的集成
│  ├─framework-component-rocketmq                : RocketMQ消息队列的集成与扩展
├─framework-component-api  
│  ├─framework-datasource-api                    : 数据源操作API
│  ├─framework-datasource-manager-api            : 管理API
│  ├─framework-datasource-cache-api              : 缓存API
│  ├─framework-datasource-event-api              : 事件API
│  ├─framework-datasource-id-api                 : ID API
│  ├─framework-datasource-lock-api               : 锁API
│  ├─framework-datasource-map-api                : Map API
│  ├─framework-datasource-queue-api              : 队列API
│  ├─framework-elasticsearch-api                 : Elasticsearch API
│  ├─framework-elasticsearch-higth-level-api     : Elasticsearch高级API
│  ├─framework-http-api                          : HTTP API
│  ├─framework-local-cache-api                   : 本地缓存API
│  ├─framework-security-api                      : 安全API
│  ├─framework-token-api                         : 令牌API
├─framework-component-entity  
│  ├─framework-component-nacos-entity            : Nacos实体类
│  ├─framework-component-redis-entity            : Redis实体类
│  ├─framework-component-scheduler-entity        : 调度器实体类
├─framework-coverage                             : 测试覆盖率扩展
├─framework-dependencies                         : 依赖管理
├─framework-global  
│  ├─framework-global-constant                   : 全局常量
│  ├─framework-global-dubbo-tenant-filter        : 租户过滤器
│  ├─framework-global-exception                  : 异常处理扩展
│  ├─framework-global-interceptor-app            : 应用拦截器
│  ├─framework-global-interceptor-tenant         : 租户拦截器
│  ├─framework-global-tenant-context             : 租户上下文
├─framework-starters  
│  ├─framework-starters-auth-client              : 认证客户端扩展
│  ├─framework-starters-cloud                    : 云服务扩展
│  ├─framework-starters-datasource-dynamic       : 动态数据源启动器
│  ├─framework-starters-disruptor                : Disruptor启动器
│  ├─framework-starters-dubbo                    : Dubbo启动器
│  ├─framework-starters-elasticesearch-client    : Elasticsearch客户端扩展
│  ├─framework-starters-environment              : 环境配置扩展
│  ├─framework-starters-event                    : 事件处理启动器
│  ├─framework-starters-healthcheck              : 健康检查
│  ├─framework-starters-http                     : HTTP扩展
│  ├─framework-starters-http-logappender         : HTTP日志追加器
│  ├─framework-starters-interceptor              : 应用拦截器启动器
│  ├─framework-starters-local-cache              : 本地缓存启动器
│  ├─framework-starters-mongodb-client           : MongoDB客户端扩展
│  ├─framework-starters-mybatis                  : MyBatis扩展
│  ├─framework-starters-nacos-config             : Nacos配置管理
│  ├─framework-starters-nacos-discovery          : Nacos服务发现
│  ├─framework-starters-oidc-client              : OIDC客户端扩展
│  ├─framework-starters-oss-client               : 对象存储服务扩展
│  ├─framework-starters-rabbitmq-client          : RocketMQ客户端扩展（此处可能是笔误，应为RocketMQ）
│  ├─framework-starters-ratelimiter              : 速率限制器启动器
│  ├─framework-starters-redis-redssionclient     : Redis客户端扩展
│  ├─framework-starters-rocketmq-consumer        : RocketMQ消费者启动器
│  ├─framework-starters-rocketmq-producer        : RocketMQ生产者启动器
│  ├─framework-starters-scheduler-client         : 调度器客户端启动器
│  ├─framework-starters-seata                    : Seata分布式事务扩展
│  ├─framework-starters-sentinel                 : Sentinel流量控制扩展
│  ├─framework-starters-skywalking               : Skywalking监控扩展
│  ├─framework-starters-springfox-adapter        : Swagger适配器
│  ├─framework-starters-transaction-client       : 事务处理客户端扩展
```

### 快速开始



[TOC]

### framework-cloud-parent

##### 作用:

作为项目父依赖，用于管理通用组件依赖版本

##### 使用方式:

```xml
<parent>
   <groupId>tech.finovy</groupId>
   <artifactId>framework-cloud-parent</artifactId>
   <version>0.2.0</version>
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

###### 获取客户端上下文:

```java
private ElasticSearchContext context = ElasticSearchContextHolder.get();

void test(){
   RestcClient client = context.getClient();
}
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

备注: 可参考 `tech.finovy.framework.redission.autoconfigure.RedissionProperties` 进行自定义配置.

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
   <artifactId>framework-starter-redis-redissionclient</artifactId>
</dependency>
```
备注: RedissonClientInterface 已经过时，将在0.2.0进行删除, 如需使用转用 tech.finovy.framework.redisson.holder.RedisContextHolder 获取,原有增强功能转到 tech.finovy.framework.redisson.holder.RedisContext

###### 代码使用:

参阅 `package tech.finovy.framework.redission.api`, 如下仅举例说明。

- RedissonClientInterface  : 扩展了 RedissonClient
- CacheApi                            : 基于Redis的缓存API
- DistributedIdApi               : 基于Redis的分布式ID实现(兼容旧项目，新项目建议迁移至专用服务)，已停止维护
- DistributedLockApi          : 基于Redission的分布式锁实现
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
   <artifactId>framework-starter-environment</artifactId>
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
仅用于前后端不分离的项目，利用后端重定向进行返回。
快速集成Spring Security，实现对接oidc协议的三方登录服务端，如keycloak，feishu，teams等。客户端只需实现对应的扩展接即可

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
   <groupId>tech.finovy</groupId>
   <artifactId>framework-starter-oidc-client</artifactId>
</dependency>
```

###### 代码使用:
**配置**:

```yaml
spring:
   security:
      oauth2:
         client:
            # 退出登录地址
            logout-url: /logout
            # 不需要鉴权的接口
            pass-paths:
               - /*.html
               - /config
            registration:
               keycloak:
                  # keycloak host，不填则使用默认值。
                  host: http://10.21.1.17:30055
                  # keycloak realm，不填则使用默认值 master。
                  realm: user
                  # (必需)id & secret (需要向keycloak管理员申请)
                  client-id: id
                  client-secret: secret
```
**流程**:

1. 客户端触发登录地址: [http://127.0.0.1:8080/oauth2/authorization/keycloak](http://127.0.0.1:8082/oauth2/authorization/keycloak)。
2. 提供给keycloak的回调地址: 填写前端地址 ,前端再请求到后端 https://127.0.0.1:8080/login/oidc/code/keycloak。

**扩展**:

1. 扩展用户登录成功失败后的逻辑: 实现 tech.finovy.framework.security.oidc.AuthorizationCallbackHandler
2. 获取登录成功的用户信息: 注入依赖 tech.finovy.framework.security.oidc.UserDetailService



#### 19. framework-starter-auth-client

##### 作用:

用于前后端分离项目的三方登录对接。快速集成Spring Security，实现对接oidc协议的三方登录服务端，如keycloak，feishu，teams等，同时也实现了账号密码登录。客户端只需实现对应的扩展接即可。属于framework-starter-oidc-client的升级版。

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
   <groupId>tech.finovy</groupId>
   <artifactId>framework-starter-auth-client</artifactId>
</dependency>
```

**配置**:

```yaml
spring:
   security:
      oauth2:
         client:
            # 开发模式
            code-mode-enable: true
            # 开发模式默认的用户(和数据库已有数据一致)
            code-mode-username: admin
            # 账号密码登录地址
            default-account-login-url: /login/account
            # 是否开启jwt格式token返回给前端
            jwt-enable: false
            # 后端登出地址
            logout-url: /logout
            # 不需要鉴权的接口
            pass-paths:
               - /public/**
            registration:
               keycloak:
                  # 领域配置
                  realm: master
                  # host配置
                  host: http://10.21.1.17:30055
                  # (必需)id & secret (需要向keycloak管理员申请)
                  client-id: id
                  client-secret: secret
                  # 前端回调地址，注意 1. 要和keycloak服务端填写的回调地址保持一致 2. registration_id=keycloak 必须添加
                  redirect-uri: https://cicd.internal-project.com?registration_id=keycloak
               gitee:
                  client-id: id
                  client-secret: secret
                  # 前端回调地址，注意 1. 要和gitee服务端填写的回调地址保持一致 2. registration_id=gitee 必须添加
                  redirect-uri: https://cicd.internal-project.com?registration_id=gitee
               feishu:
                  client-id: id
                  client-secret: secret
                  # 前端回调地址，注意 1. 要和feishu服务端填写的回调地址保持一致 2. registration_id=feishu 必须添加
                  redirect-uri: https://cicd.internal-project.com?registration_id=feishu
```

**前后端分离完整的对接流程**:

![oidc_example](/docs/image/oidc_way_en.png)

**流程**:

1. 获取三方登录配置 ClientProviderHolder.get()，由后端包装给前端
```json
 {
   "code": 0,
   "data": [
      {
         "type": "keycloak",
         "url": "http://10.21.1.17:30055/realms/master/protocol/openid-connect/auth?response_type=code&client_id=test-keycloak&redirect_uri=http://127.0.0.1:8083/loading.html?registration_id=keycloak&scope=openid"
      }
   ],
   "msg": "success",
   "success": true
}
```
2. 前端根据第一步获取的url重定向至三方

3. 三方回调至前端，前端转发请求至后端接口

```text
回调前端案例: http://127.0.0.1:8083/loading.html?registration_id=keycloak&session_state=b9cac00d-7389-48a5-8056-bbfd45642244&code=e5db626d-04b3-4d27-8342-9460867e0cad.b9cac00d-7389-48a5-8056-bbfd45642244.42918562-5c06-4604-bb40-3c4452862faf

请求后端案例: http://127.0.0.1:8083/login/auth/code?registration_id=keycloak&session_state=b9cac00d-7389-48a5-8056-bbfd45642244&code=e5db626d-04b3-4d27-8342-9460867e0cad.b9cac00d-7389-48a5-8056-bbfd45642244.42918562-5c06-4604-bb40-3c4452862faf
```

API: GET login/auth/code?三方回调的参数
Response

 ```json
 {
   "code":0,
   "data":{
      "token":"101bfcf6-9315-4b3f-b435-a60205906c88"
   },
   "msg":"SUCCESS"
}
   ```
备注: 注意API有前缀的话，需要添加上

**扩展:**

1. 自定义账号密码验证逻辑:

   实现 tech.finovy.framework.security.auth.extend.CustomPasswordEncoder

   实现 tech.finovy.framework.security.auth.extend.UsernameAndPasswordService

2. 自定义token存储逻辑: 实现 tech.finovy.framework.security.auth.core.token.normal.TokenStorage

3. 获取登录成功的用户信息: 注入依赖 tech.finovy.framework.security.auth.UserDetailService

4. 扩展用户登录成功失败逻辑: 实现 tech.finovy.framework.security.auth.AuthorizationCallbackHandler


#### 20. framework-starter-seata

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

#### 21. framework-starter-sentinel-cloud

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

#### 22. framework-starter-sentinel-dubbo

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

#### 23. framework-starter-interceptor-app

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

#### 24. framework-starter-interceptor-mybatis

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

#### 25. framework-starter-interceptor-tenant

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

#### 26. framework-starter-local-cache

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

#### 27. framework-starter-datasource-dynamic

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

#### 28. framework-starter-disruptor

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

#### 29. framework-starter-event

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

#### 30. framework-starter-scheduler-client

##### 作用:

集成定时任务客户端

##### 使用方式:

###### 引入依赖:

```xml
<dependency>
   <groupId>tech.finovy</groupId>
   <artifactId>framework-starter-event</artifactId>
</dependency>
```

###### 代码使用

**普通定时任务:**

自定义类继承AbstractSchedulerExecutorListener，type与服务端配置文件中的jobKey一致。

```java
@Slf4j
@Component
public class CustomService extends AbstractSchedulerExecutorListener {

   @Override
   public String getType() {
      return "type";
   }

   @Override
   public RemoteJobExecuteResult trigger(RemoteJobExecuteConfig config) {
      // 业务逻辑
      return new RemoteJobExecuteResult();
   }
}
```

集成此依赖，在服务端配置的调用地址为:  服务地址 + /scheduler/trigger.

**流式调度任务:**

自定义类继承 AbstractSchedulerFlowListener, type与服务端配置文件中的jobKey一致。

```java
public class CustomService<T> extends AbstractSchedulerFlowListener<T> {

   @Override
   public List<T> fetch(RemoteJobExecuteConfig config) {
      // 自定义逻辑
      return super.fetch(config);
   }

   @Override
   public RemoteJobExecuteResult process(RemoteJobProcessConfig<T> input) {
      // 自定义逻辑
      return super.process(input);
   }

   @Override
   public String getType() {
      return "type";
   }
}
```

集成此依赖，在服务端配置的调用地址为:  服务地址 + /scheduler/job_fetch，服务地址 + /scheduler/job_process

