# Cloud Framework

------

[中文版](README_CN.md) | 英文版

### Directory List

- [Project Introduction](#Project Introduction)
- [Functional Features](#Functional Features)
- [Operating Environment](#Operating Environment)
- [Project Structure](#Project Structure)
- [Quick Start](#Quick Start)

### Project Introduction

This project does not involve the development of business code, but rather provides a solid and efficient development foundation. The framework predefines a series of dependency versions to ensure the overall consistency and stability of the project's technology stack. In addition to dependency management, we have also extended the functionality of some popular open-source projects to meet more specific and unique business requirements.

### Functional Features

1. **Unified Dependency Management**: All business projects can inherit from this parent project, ensuring that the technology component versions used in all sub-projects are consistent, greatly reducing version conflicts and technical risks.
2. **Extension of Open-Source Project Features**: We have extended the functionalities of some popular open-source projects in the market, such as Nacos, to better adapt to the specific requirements of business projects.
3. **Rapid Development**: By using this framework, business project teams can focus more on the development of business logic, without having to pay excessive attention to the underlying technical implementation and version compatibility issues.

### Operating Environment

- JDK 17+
- Spring Cloud 4.0.4
- Spring Cloud Alibaba 2022
- Disruptor 3.4.4
- Nacos 2.3.2

### Project Structure

```tex
framework-cloud
├─framework-common                               : Common Dependencies
│  ├─framework-common-compress                   : Compression Extension
│  ├─framework-common-core                       : Common Dependencies
│  ├─framework-common-security                   : Security Extension
│  ├─framework-common-utils                      : Utils Extension
├─framework-component  
│  ├─framework-component-scheduler-client        : Scheduler Extension
│  ├─framework-component-datasource-dynamic      : Dynamic Data Source Extension
│  ├─framework-component-disruptor               : Disruptor Extension
│  ├─framework-component-elasticsearch           : Elasticsearch Extension
│  ├─framework-component-event                   : Event Extension
│  ├─framework-component-http                    : HTTP Extension
│  ├─framework-component-listener                : Listener Extension
│  ├─framework-component-local-cache             : Local Cache
│  ├─framework-component-ratelimter              : Rate Limiter Extension
│  ├─framework-component-redis                   : Redis Extension
│  ├─framework-component-rocketmq                : RocketMQ Extension
├─framework-component-api  
│  ├─framework-datasource-api                    : Data Source API
│  ├─framework-datasource-manager-api            : Data Source Manager API
│  ├─framework-datasource-cache-api              : Data Source Cache API
│  ├─framework-datasource-event-api              : Data Source Event API
│  ├─framework-datasource-id-api                 : Data Source ID API
│  ├─framework-datasource-lock-api               : Data Source Lock API
│  ├─framework-datasource-map-api                : Data Source Map API
│  ├─framework-datasource-queue-api              : Data Source Queue API
│  ├─framework-elasticsearch-api                 : Elasticsearch API
│  ├─framework-elasticsearch-higth-level-api     : Elasticsearch High Level API
│  ├─framework-http-api                          : HTTP API
│  ├─framework-local-cache-api                   : Local Cache API
│  ├─framework-security-api                      : Security API
│  ├─framework-token-api                         : Token API
├─framework-component-entity  
│  ├─framework-component-nacos-entity            : Nacos POJO
│  ├─framework-component-redis-entity            : Redis POJO
│  ├─framework-component-scheduler-entity        : Scheduler POJO
├─framework-coverage                             : Coverage  Extension
├─framework-dependencies                         : Dependencies
├─framework-global  
│  ├─framework-global-constant                   : Global Constants
│  ├─framework-global-dubbo-tenant-filter        : Tenant Filter
│  ├─framework-global-exception                  : Exception Extension
│  ├─framework-global-interceptor-app            : Application Interceptor
│  ├─framework-global-interceptor-tenant         : Tenant Interceptor
│  ├─framework-global-tenant-context             : Tenant Context
├─framework-starters  
│  ├─framework-starters-auth-client              : Auth Client Extension
│  ├─framework-starters-cloud                    : Cloud Extension
│  ├─framework-starters-datasource-dynamic       : Dynamic Data Source Extension
│  ├─framework-starters-disruptor                : Disruptor Extension
│  ├─framework-starters-dubbo                    : Dubbo Extension
│  ├─framework-starters-elasticesearch-client    : Elasticsearch Extension
│  ├─framework-starters-environment              : Environment Extension
│  ├─framework-starters-event                    : Event Extension
│  ├─framework-starters-healthcheck              : Health Check
│  ├─framework-starters-http                     : HTTP Extension
│  ├─framework-starters-http-logappender         : HTTP Log Appender
│  ├─framework-starters-interceptor              : Application Interceptor
│  ├─framework-starters-local-cache              : Local Cache
│  ├─framework-starters-mongodb-client           : MongoDB Extension
│  ├─framework-starters-mybatis                  : MyBatis Extension
│  ├─framework-starters-nacos-config             : Nacos Configuration
│  ├─framework-starters-nacos-discovery          : Nacos Discovery
│  ├─framework-starters-oidc-client              : OIDC Client Extension
│  ├─framework-starters-oss-client               : OSS Extension
│  ├─framework-starters-rabbitmq-client          : RocketMQ Extension
│  ├─framework-starters-ratelimiter              : Rate Limiter Extension
│  ├─framework-starters-redis-redssionclient     : Redis Extension
│  ├─framework-starters-rocketmq-consumer        : RocketMQ Extension
│  ├─framework-starters-rocketmq-producer        : RocketMQ Extension
│  ├─framework-starters-scheduler-client         : Scheduler Extension
│  ├─framework-starters-seata                    : Seata Extension
│  ├─framework-starters-sentinel                 : Sentinel Extension
│  ├─framework-starters-skywalking               : Skywalking Extension
│  ├─framework-starters-springfox-adapter        : Swagger Extension
│  ├─framework-starters-transaction-client       : Transaction Extension
```

### Quick Start


[TOC]

### framework-cloud-parent

##### Purpose:

Serves as the parent dependency for projects, managing common component dependency versions to ensure uniformity and reduce maintenance overhead.

##### Usage:

```xml
<parent>
   <groupId>tech.finovy</groupId>
   <artifactId>framework-cloud-parent</artifactId>
   <version>0.2.0</version>
</parent>
```

### framework-global

#### framework-global-constant

##### Purpose:

Defined commonly used constants for projects, such as traceid. If it is used across projects, it needs to be organized here to avoid multiple definitions.

##### Usage:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-constant</artifactId>
</dependency>
```

#### framework-global-exception

##### Purpose:

Defined common exceptions for projects.

##### Usage:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-exception</artifactId>
</dependency>
```

#### framework-global-interceptor-app

##### Purpose:

Defined commonly used global interceptors and parameter parsers at the interface layer.

##### Usage:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-interceptor-app</artifactId>
</dependency>
```

#### framework-global-interceptor-mybatis

##### Purpose:

Defined Mybatis Plus tenant global interceptor, etc., for rewriting multi tenant SQL.

##### Usage

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-interceptor-mybatis</artifactId>
</dependency>
```

#### framework-global-interceptor-tenant

##### Purpose:

Defined commonly used tenant interceptors for implicitly passing tenant IDs

##### Usage

```java
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-interceptor-tenant</artifactId>
</dependency>
```

#### framework-global-tenant-context

##### Purpose:

Define Global Tenant Context

##### Usage


```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-global-tenant-context</artifactId>
</dependency>
```

Note: Generally introduced by other dependencies, business projects do not need to be introduced separately.

### framework-starters

#### 1. framework-starter-elasticsearch-client

##### Purpose:

Used to manage ES client connection information

##### Dependent files:

framework-core-elasticsearch.yaml

##### Usage:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-elasticsearch-client</artifactId>
</dependency>
```

###### Get client context:

```java
private ElasticSearchContext context = ElasticSearchContextHolder.get();

void test(){
        RestcClient client = context.getClient();
}
```

#### 2. framework-starter-healthcheck

##### Usage:

Used for startup and runtime health checks

##### principle:

Built in Rest interface to check if the service is alive

##### Usage:


```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-healthcheck</artifactId>
</dependency>
```

###### K8s configuration health check:

```xml
liveness: /healthcheck/liveness
readiness: /healthcheck/readiness
```

#### 3. framework-starter-http

##### Purpose:

用于配置http客户端, 集成基于服务发现的负载均衡功能

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-http</artifactId>
</dependency>
```

###### Injecting dependencies:

```java
@Autowired
private HttpTemplateService<RestTemplate> httpTemplateService;
```

###### Code call:

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

##### Purpose:

Integrated service log unified collection function, sent to the collection end via HTTP

##### Principle:

Based on logback, customize ` tech. finovy. framework. logappender LogHttpAppender `, implementing personalized log collection

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-http-logappender</artifactId>
</dependency>
```

###### Modify logback-spring.xml

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
		<!-- appender -->
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

##### Purpose:

Used for unified management of MongoDB clients

##### Dependent files:

framework-core-mongdb.json

###### File Example:

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

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-mongodb-client</artifactId>
</dependency>
```

Code usage:

```java
@Autowired
private MongodbClientSourceMap clientSourceMap;

        void mongClient() {
        MongoDatabase database = clientSourceMap.getMongodbDatabase("export-system");
        // with API call
        }
```

#### 6. framework-starter-nacos-config

##### Purpose:

Used for automatic assembly in the configuration center, providing more elegant extension points

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-nacos-config</artifactId>
</dependency>
```

###### Extension point:

- How to dynamically obtain configuration changes?

1. Inherit AbstractNacosConfigDefinitionListener
2. Inject the CustomizationDefinitionListener into the Spring container

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

##### Purpose:

Inject the CustomizationDefinitionListener into the Spring container

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-nacos-discovery</artifactId>
</dependency>
```



#### 8. framework-starter-oss-client

##### Purpose:

Manages OSS (Object Storage Service) client configurations.

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-oss-client</artifactId>
</dependency>
```

Code Example:

```java
@Autowired
private OssClientService ossClient;

public void ossClientTest() {
        // Reference API for Get/Put/Delete
        ossClient.getObject("bucketName","ossFileName");
        }
```



#### 9. framework-starter-rabbitmq-client

##### Purpose:

Handles RabbitMQ client connections.

##### Usage:

###### Include Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-rabbitmq-client</artifactId>
</dependency>
```



#### 10. framework-starter-ratelimiter

##### Purpose:

Integrates Guava RateLimiter for controlling access rate limits.

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-ratelimiter</artifactId>
</dependency>
```

###### Code Usage:

```java
@Autowired
private DistributedRateLimiterFactoryManager manager;

public void ratelimiterTest(){
// resource ,qps
final boolean test = manager.tryAcquire("resource", 1);
        }
```



#### 11. framework-starter-redis-redissonclient

##### Purpose:

Used to manage Redis client configuration and provide multiple extension methods

##### Dependency configuration:

###### Configuration name (default):

framework-core-redis

Note: Please refer to ` tech. finovy. framework. publication. autoconfiguration. ' RedissionProperties ` for custom configuration

###### Configuration Example:

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

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-redis-redissionclient</artifactId>
</dependency>
```
Note: The RedissionsClientInterface is outdated and will be removed on 0.2.0. If you need to use it, switch to tech.finovy.framework.redisson.holder.RedisContextHolder is obtained, and the original enhanced functions are transferred to tech.finovy.framework.redisson.holder.RedisContext

###### Code Usage:

Refer to `package tech.finovy.framework.redission.api`,, the following is only an example for explanation.

- RedissonClientInterface  : Expanded RedissonClient
- CacheApi                 : Redis based caching API
- DistributedIdApi         : Implementation of distributed ID based on Redis (compatible with old projects, it is recommended to migrate new projects to dedicated services), maintenance has been discontinued
- DistributedLockApi       : Implementation of Distributed Locks Based on Redirection
- LocalCacheMapApi         : Local cache implementation
- MapApi                   : Implementation of Map Operation Based on Redis

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

If you want to obtain Redis client, please use RedisContextHolder.get().getClient()

#### 12. framework-starter-rocketmq-consumer

##### Purpose:

Manage RocketMQ Consumers

##### Dependency configuration:

```yaml
rocketmq:
  nameserver: 10.16.2.60:9876;10.16.2.61:9876
```

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-rocketmq-consumer</artifactId>
</dependency>
```

###### Code Usage:

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

##### Purpose:

Manage RocketMQ Producer

##### Dependency configuration:

```yaml
rocketmq:
  nameserver: 10.16.2.60:9876;10.16.2.61:9876
```

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-rocketmq-producer</artifactId>
</dependency>
```

###### Code Usage

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

##### Purpose:

Quickly connect to Spring Cloud, integrating Nacos configuration, service discovery, service health checks, log collection, service containers, and other dependencies

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-cloud</artifactId>
</dependency>
```

#### 15. framework-starter-dubbo

##### Purpose:

Quick access to Dubbo, integration of Dubbo, and dependencies on Dubbo and Nacos integration

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-dubbo</artifactId>
</dependency>
```

#### 16. framework-starter-environment

##### Purpose:

Used for decrypting database fields, set up in the spring environment.

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-environment</artifactId>
</dependency>
```

#### 17. framework-starter-mybatis

##### Purpose:

Quickly integrate mybatis dependencies

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-mybatis</artifactId>
</dependency>
```

#### 18. framework-starter-oidc-client

##### Purpose:
Only used for projects where the front-end and back-end are not separated, using backend redirection for return.
Quickly integrate Spring Security and implement a third-party login server that interfaces with the oidc protocol, such as keycloak, feishu, teams, etc. The client only needs to implement the corresponding extension connection

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-oidc-client</artifactId>
</dependency>
```

###### Code Usage:
**allocation**:

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

1. Client triggered login address: [http://127.0.0.1:8080/oauth2/authorization/keycloak](http://127.0.0.1:8082/oauth2/authorization/keycloak)。
2. Provide callback address to keycloak: Fill in the front-end address, and the front-end will then request to the back-end https://127.0.0.1:8080/login/oidc/code/keycloak。

**extend**:

1. Logic after successful and failed login for extended users: implementation tech.finovy.framework.security.oidc.AuthorizationCallbackHandler
2. Obtaining User Information for Successful Login: Injecting Dependencies tech.finovy.framework.security.oidc.UserDetailService



#### 19. framework-starter-auth-client

##### Purpose:

Three party login docking for front-end and back-end separation projects. Quickly integrate Spring Security to achieve third-party login server integration with oidc protocol, such as keycloak, feishu, teams, etc., and also achieve account and password login. The client only needs to implement the corresponding extension connection. Belongs to an upgraded version of the framework starter idc client.

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-auth-client</artifactId>
</dependency>
```

**allocation**:

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

**Complete docking process with front-end and back-end separation**:

![oidc_example](/docs/image/oidc_way.png)

**flow**:

1. Obtain the third-party login configuration ClientProviderHolder. get(), which will be packaged by the backend to the frontend
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
2. The front-end redirects to three parties based on the URL obtained in the first step

3. Tri party callback to the front-end, front-end forwarding requests to the back-end interface

```text
Callback front-end case: http://127.0.0.1:8083/loading.html?registration_id=keycloak&session_state=b9cac00d-7389-48a5-8056-bbfd45642244&code=e5db626d-04b3-4d27-8342-9460867e0cad.b9cac00d-7389-48a5-8056-bbfd45642244.42918562-5c06-4604-bb40-3c4452862faf

Request backend case: http://127.0.0.1:8083/login/auth/code?registration_id=keycloak&session_state=b9cac00d-7389-48a5-8056-bbfd45642244&code=e5db626d-04b3-4d27-8342-9460867e0cad.b9cac00d-7389-48a5-8056-bbfd45642244.42918562-5c06-4604-bb40-3c4452862faf
```

API: GET login/auth/code?Request backend case
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

Note: If the API has a prefix, it needs to be added

**extend:**

1. Custom account and password verification logic:

   achieve tech.finovy.framework.security.auth.extend.CustomPasswordEncoder

   achieve tech.finovy.framework.security.auth.extend.UsernameAndPasswordService

2. Custom token storage logic: implementation tech.finovy.framework.security.auth.core.token.normal.TokenStorage

3. Obtaining User Information for Successful Login: Injecting Dependencies tech.finovy.framework.security.auth.UserDetailService

4. Logic of successful login failure for extended users: implementation tech.finovy.framework.security.auth.AuthorizationCallbackHandler


#### 20. framework-starter-seata

##### Purpose:

ntegrate Seata client dependencies for distributed transaction related purposes. Due to the involvement of multiple modes, please refer to the official documentation for Code Usage https://seata.io/zh-cn/docs/overview/what-is-seata.html

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-seata</artifactId>
</dependency>
```

#### 21. framework-starter-sentinel-cloud

##### Purpose:

Integrate Sentinel MVC client dependencies for web layer throttling (HTTP)

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-sentinel-cloud</artifactId>
</dependency>
```

#### 22. framework-starter-sentinel-dubbo

##### Purpose:

Integrate Sentinel Dubbo client dependencies for RPC layer flow limiting

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-sentinel-dubbo</artifactId>
</dependency>
```

#### 23. framework-starter-interceptor-app

##### Purpose:

Used for token judgment interception. (Compatible with older versions)

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-interceptor-app</artifactId>
</dependency>
```

###### Code Usage:

将 techno.finovy.framework.global.interceptor。会话接收器添加至 WebMvcConfigurer拦截器链

#### 24. framework-starter-interceptor-mybatis

##### Purpose:

Used for tenant interception injection at the mybatis level. (Compatible with older versions)

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-interceptor-mybatis</artifactId>
</dependency>
```

#### 25. framework-starter-interceptor-tenant

##### Purpose:

Used for tenant interception injection at the mybatis level, with the default tenant field being appid. (Compatible with older versions)

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-interceptor-tenant</artifactId>
</dependency>
```

#### 26. framework-starter-local-cache

##### Purpose:

Integrate local cache dependencies

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-local-cache</artifactId>
</dependency>
```

###### Code Usage:

consult tech.finovy.framework.cache.local.api.LocalCacheService

#### 27. framework-starter-datasource-dynamic

##### Purpose:

Integrate multiple data source dependencies and maintain multiple data source connection pools.

##### Dependency configuration:

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

##### Usage

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-datasource-dynamic</artifactId>
</dependency>
```

###### Code Usage:

Inject DynamicDataSourceMap and use this type of API to obtain relevant connection pools

#### 28. framework-starter-disruptor

##### Purpose:

Integrated disruptor

##### Usage

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-disruptor</artifactId>
</dependency>
```

###### Code Usage:

Create DefaultDisruptorEngineProvider:

```
// DisruptorEventConfiguration - Custom Interruptor Configuration
final DefaultDisruptorEngineProvider engineProvider = new DefaultDisruptorEngineProvider(configuration);
```

Use techno.finovy.framework.disruptor.spi.DisruptorEngine API handles event related issues

#### 29. framework-starter-event

##### Purpose:

Integration Event Dependency

##### Usage

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-event</artifactId>
</dependency>
```

###### Code Usage

consult

- tech.finovy.framework.distributed.event.api.AsyncEventService
- tech.finovy.framework.distributed.event.api.EventService

#### 30. framework-starter-scheduler-client

##### Purpose:

Integrated scheduled task client

##### Usage:

###### Add Dependency:

```xml
<dependency>
    <groupId>tech.finovy</groupId>
    <artifactId>framework-starter-event</artifactId>
</dependency>
```

###### Code Usage

**Regular scheduled tasks:**

The custom class inherits AbstractSchedulerExecutorListener, with the type consistent with the jobKey in the server-side configuration file.

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

Integrating this dependency, the call address configured on the server is: service address+/scheduler/trigger

**Streaming scheduling tasks:**


62/10000
实时翻译
划译
The custom class inherits the AbstractSchedulerFlowListener, with the type consistent with the jobKey in the server-side configuration file.

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

Integrating this dependency, the call address configured on the server is: service address+/scheduler/job_fetch, service address+/scheduler/job_process
