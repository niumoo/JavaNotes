---
title: Springboot 系列（四）Spring Boot 日志框架
toc_number: false
date: 2019-01-15 22:02:02
url: springboot/springboot04-log
tags:
 - Springboot
 - Springboot2
 - Logback
categories:
 - Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。、

> 注意：本 Spring Boot 系列文章基于 Spring Boot 版本 **v2.1.1.RELEASE** 进行学习分析，版本不同可能会有细微差别。

## 前言
![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/6ceaab90e32e1c0de8dd4ed097900509.png)Spring 框架选择使用了 JCL 作为默认日志输出。而 Spring Boot 默认选择了 SLF4J 结合 LogBack。那我们在项目中该使用哪种日志框架呢？在对于不同的第三方 jar 使用了不同的日志框架的时候，我们该怎么处理呢？
<!-- more -->

## 1. 日志框架介绍

日志对于应用程序的重要性不言而喻，不管是记录运行情况还是追踪线上问题，都离不开对日志的分析，在 Java 领域里存在着多种日志框架，如 JUL, Log4j, Log4j2, Commons Loggin, Slf4j, Logback 等。关于 Log4j, Log4j2 和 Slf4j 直接的故事这里不做介绍，有兴趣可以自行百度。

## 2. SLF4 的使用

在开发的时候不应该直接使用日志实现类，应该使用日志的抽象层。具体参考 [SLF4J  官方](https://www.slf4j.org/manual.html)。
下图是 SLF4J 结合各种日志框架的官方示例，从图中可以清晰的看出 SLF4J API 永远作为日志的门面，直接应用与应用程序中。
![SLF4](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/180c77a1bfd179623888aa83faf4519d.png)

同时 SLF4 官方给出了简单示例。

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```
需要注意的是，要为系统导入 SLF4J 的 jar 和 日志框架的实现 jar. 由于每一个日志的实现框架都有自己的配置文件，所以在使用 SLF4 之后，配置文件还是要使用实现日志框架的配置文件。


## 3. 统一日志框架的使用

一般情况下，在项目中存在着各种不同的第三方 jar ，且它们的日志选择也可能不尽相同，显然这样是不利于我们使用的，那么如果我们想为项目设置统一的日志框架该怎么办呢？

在 [SLF4J 官方](https://www.slf4j.org/legacy.html)，也给了我们参考的例子。

![Bridging legacy APIs](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/4d3f93aedfdfff372bf2908f5ef876bd.png)从图中我们得到一种统一日志框架使用的方式，可以使用一种和要替换的日志框架类完全一样的 jar 进行替换，这样不至于原来的第三方 jar 报错，而这个替换的 jar 其实使用了 SLF4J API. 这样项目中的日志就都可以通过 SLF4J API 结合自己选择的框架进行日志输出。
**统一日志框架使用步骤归纳如下**：

1. 排除系统中的其他日志框架。
2. 使用中间包替换要替换的日志框架。
3. 导入我们选择的 SLF4J 实现。


## 4. Spring Boot 的日志关系

### 4.1. 排除其他日志框架

根据上面总结的要统一日志框架的使用，第一步要排除其他的日志框架，在 Spring Boot 的 Maven 依赖里可以清楚的看到 Spring Boot 排除了其他日志框架。
![Spring Boot 排除其他日志框架](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/f24ff63ebb75a90c0d85b3dd74840cb5.png)我们自行排除依赖时也只需要按照图中的方式就好了。


### 4.2. 统一框架引入替换包

其实 Spring Boot 也是使用了 SLF4J+logback 的日志框架组合，查看 Spring Boot 项目的 Maven 依赖关系可以看到 Spring Boot 的核心启动器 spring-boot-starter 引入了 spring-boot-starter-logging.
```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-logging</artifactId>
      <version>2.1.1.RELEASE</version>
      <scope>compile</scope>
    </dependency>
```
而 spring-boot-starter-logging 的 Maven 依赖主要引入了 logback-classic (包含了日志框架 Logback 的实现)，log4j-to-slf4j (在 log4j 日志框架作者开发此框架的时候还没有想到使用日志抽象层进行开发，因此出现了 log4j 向 slf4j 转换的工具)，jul-to-slf4j ( Java 自带的日志框架转换为 slf4j).
```xml
  <dependencies>
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.3</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-to-slf4j</artifactId>
      <version>2.11.1</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>jul-to-slf4j</artifactId>
      <version>1.7.25</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
```
从上面的分析，Spring Boot 对日志框架的使用已经是清晰明了了，我们使用 IDEA 工具查看 Maven 依赖关系，可以清晰的看到日志框架的引用。如果没有 IDEA 工具，也可以使用 Maven 命令查看依赖关系。
```shell
mvn dependency:tree
```
![Spring Boot Maven 依赖](https://user-images.githubusercontent.com/26371673/50733360-33660980-11c7-11e9-8742-1f24e7449db2.png)由此可见，Spring Boot 可以自动的适配日志框架，而且底层使用 **SLF4 + LogBack** 记录日志，如果我们自行引入其他框架，需要排除其日志框架。

## 5. Spring Boot 的日志使用

### 5.1. 日志级别和格式
从上面的分析，发现 Spring Boot 默认已经使用了 **SLF4J + LogBack** . 所以我们在不进行任何额外操作的情况下就可以使用 **SLF4J + Logback** 进行日志输出。
编写 Java 测试类进行测试。
```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * 测试日志输出，
 * SLF4J 日志级别从小到大trace,debug,info,warn,error
 *
 * @Author niujinpeng
 * @Date 2018/12/11 21:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogbackTest {
    
    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testLog() {
        logger.trace("Trace 日志...");
        logger.debug("Debug 日志...");
        logger.info("Info 日志...");
        logger.warn("Warn 日志...");
        logger.error("Error 日志...");
    }
}

```
已知日志级别从小到大为 trace < debug < info < warn < error . 运行得到输出如下。由此可见  ***Spring Boot 默认日志级别为 INFO***.
```log
2018-12-11 23:02:58.028 [main] INFO  n.c.boot.LogbackTest - Info 日志...
2018-12-11 23:02:58.029 [main] WARN  n.c.boot.LogbackTest - Warn 日志...
2018-12-11 23:02:58.029 [main] ERROR n.c.boot.LogbackTest - Error 日志...
```
从上面的日志结合 Logback 日志格式可以知道 Spring Boot 默认日志格式是。
```shell
%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
# %d{yyyy-MM-dd HH:mm:ss.SSS} 时间
# %thread 线程名称
# %-5level 日志级别从左显示5个字符宽度
# %logger{50} 类名
# %msg%n 日志信息加换行
```
至于为什么 Spring Boot 的默认日志输出格式是这样？
![Spring Boot 默认日志输出](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/09754e106f158d8e2dfcf540ca00cb2c.png)我们可以在 Spring Boot 的源码里找到答案。

### 5.2 自定义日志输出
可以直接在配置文件编写日志相关配置。
```yaml
# 日志配置
# 指定具体包的日志级别
logging.level.net.codingme=debug
# 控制台和日志文件输出格式
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
# 日志文件大小
logging.file.max-size=10MB
# 保留的日志时间
logging.file.max-history=10
# 日志输出路径，默认文件spring.log
logging.path=systemlog
#logging.file=log.log
```

关于日志的输出路径，可以使用 logging.file 或者 logging.path 进行定义，两者存在关系如下表。

| `logging.file` | `logging.path` | 例子       | 描述                                                         |
| -------------- | -------------- | ---------- | ------------------------------------------------------------ |
| *（没有）*     | *（没有）*     |            | 仅控制台记录。                                               |
| 具体文件       | *（没有）*     | `my.log`   | 写入指定的日志文件，名称可以是精确位置或相对于当前目录。     |
| *（没有）*     | 具体目录       | `/var/log` | 写入`spring.log`指定的目录，名称可以是精确位置或相对于当前目录。 |


## 6. 替换日志框架
因为 Log4j 日志框架已经年久失修，原作者都觉得写的不好，所以下面演示替换日志框架为 Log4j2 的方式。根据[官网](https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/using-boot-build-systems.html#using-boot-starter)我们 Log4j2 与 logging 需要二选一，因此修改 pom如下。

```xml
		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>spring-boot-starter-logging</artifactId>
                    <groupId>org.springframework.boot</groupId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>
```

文章代码已经上传到 GitHub [Spring Boot 日志系统](https://github.com/niumoo/springboot/tree/master/springboot-logback)。


### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变的优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)