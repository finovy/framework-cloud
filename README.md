# Framework Cloud

------

**English | [中文版](README_CN.md)**

### Directory List

  - [Project Introduction](#project-introduction)
  - [Functional Features](#functional-features)
  - [Operating Environment](#operating-environment)
  - [Project Structure](#project-structure)
  - [Quick Start](#quick-start)

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
- Nacos 2.2.2

### Project Structure

```tex
framework-cloud
├─framework-core                                 : Core Code Layer
│  ├─framework-core-commons                      : Common Dependencies
│  ├─framework-core-disruptor                    : Disruptor Extension
│  ├─framework-core-http                         : HTTP Extension
│  ├─framework-local-cache                       : Local Cache
│  ├─framework-core-redis                        : Redis Extension
│  ├─framework-core-rocketmq                     : RocketMQ Extension
├─framework-starters                             : Starter Layer
│  ├─framework-starter-nacos-entity              : Nacos POJO
│  ├─framework-starter-nacos-config              : Nacos Configuration
│  ├─framework-starter-nacos-discovery           : Nacos Registry Center
│  ├─framework-starter-healthcheck               : Health Check
│  ├─framework-starter-http-logappender          : RocketMQ Extension
├─framework-dependencies                         : Dependency Management
```

### Quick Start

1. Introduce the version management dependency of framework-cloud

   ```yaml
   <dependency>
       <groupId>tech.finovy</groupId>
       <artifactId>framework-dependencies</artifactId>
       <version>0.1.0-SNAPSHOT</version>
   </dependency>
   ```

2. Introduce other functional modules as needed, without specifying version numbers (because they are already defined in framework-dependencies)

   ```yaml
   <dependency>
       <groupId>tech.finovy</groupId>
       <artifactId>framework-core-commons</artifactId>
   </dependency>
   ```

