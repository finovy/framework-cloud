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
- Nacos 2.2.2

### 项目结构

```tex
framework-cloud                            
├─framework-core                                 ：核心代码层
│  ├─framework-core-commons                      ：公共依赖
│  ├─framework-core-disruptor                    ：Disruptor扩展
│  ├─framework-core-http                         ：http扩展
│  ├─framework-local-cache                       ：本地缓存
│  ├─framework-core-redis                        ：redis扩展
│  ├─framework-core-rocketmq                     ：rocketMQ扩展
├─framework-starters                             ：starter层
│  ├─framework-starter-nacos-entity              ：nacos POJO
│  ├─framework-starter-nacos-config              ：nacos配置中
│  ├─framework-starter-nacos-discovery           ：nacos注册中心
│  ├─framework-starter-healthcheck               ：健康检查
│  ├─framework-starter-http-logappender          ：rocketMQ扩展
├─framework-dependencies                         ：依赖管理
```

### 快速开始

1. 引入framework-cloud的版本管理依赖

   ```yaml
   <dependency>
       <groupId>tech.finovy</groupId>
       <artifactId>framework-dependencies</artifactId>
       <version>0.1.0-SNAPSHOT</version>
   </dependency>
   ```

2. 按需求引入其他功能模块，无需指定版本号（因为它们已framework-dependencies中定义）

   ```yaml
   <dependency>
       <groupId>tech.finovy</groupId>
       <artifactId>framework-core-commons</artifactId>
   </dependency>
   ```
