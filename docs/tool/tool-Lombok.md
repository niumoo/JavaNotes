---
title: 如何使用 Lombok 进行优雅的编码
date: 2018-12-30 00:08:10
url: develop/tool-Lombok
tags:
 - Lombok
 - 开发工具
categories:
 - 生产工具
---

Project Lombok 是一个 java 库，它可以通过注解自动为你要编写的类添加相应功能，如 get/set 方法，提高了开发效率。

引入 POM 依赖。

```xml
<!-- Lombok核心依赖 -->
<dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <version>1.18.4</version>
       <scope>provided</scope>
</dependency>
<!-- 添加logback日志框架支持，（可有可无） -->
<dependency>
       <groupId>ch.qos.logback</groupId>
       <artifactId>logback-classic</artifactId>
       <version>1.0.6</version>
</dependency>
```
<!-- more -->

查看依赖关系可以发现 `logback-classic` 依赖了 `SLF4J-API` 日志门面以及 l`ogback-core` 日志实现框架。

![添加的pom依赖](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1544414938730.png)
关于日志注解的选择，可以参考[官方文档](https://www.projectlombok.org/features/log)。默认的日志输出注释类为被注释的类路径，也可以使用 topic 参数自定义，如`@Slf4j(topic="reporting")`. 一般我们都会选择 `@Slf4j` 这个日志抽象类。在使用这个注解的时候需要导入 SLF4-API 抽象层以及具体的日志实现框架，上方的依赖中我们已经添加了日志依赖。

```java
@CommonsLog
Creates private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(LogExample.class);
@Flogger
Creates private static final com.google.common.flogger.FluentLogger log = com.google.common.flogger.FluentLogger.forEnclosingClass();
@JBossLog
Creates private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(LogExample.class);
@Log
Creates private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LogExample.class.getName());
@Log4j
Creates private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogExample.class);
@Log4j2
Creates private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LogExample.class);
@Slf4j
Creates private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
@XSlf4j
Creates private static final org.slf4j.ext.XLogger log = org.slf4j.ext.XLoggerFactory.getXLogger(LogExample.class);
```
`Lombok` 的使用主要是几个注解，下面介绍常用的几个注解。

`@Getter/@Setter`   为属性生成 get 和 set 方法。

`@ToString` 生成 toString 方法，输出各个属性值。

`@EqualsAndHashCode` 生成 equals 和 hashCode 方法。

`@NoArgsConstructor` 生成无惨构造器。

`@AllArgsConstructor` 生成全参数构造器。

`@Data` 是一个方便注解，它捆绑了 `@ToString` `@Getter/@Setter`  `@EqualsAndHashCode` 以及 `@RequiredArgsConstructor`.

根据上面的解释，用下面的一个例子演示用法。

```java
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Lombok使用
 *
 * @Author niujinpeng
 * @Date 2018/12/10 11:05
 */
@Slf4j(topic = "Lombok")
public class LombokTest {

    public static void main(String[] args) {
        Person person1 = new Person();
        person1.setName("Darcy");
        person1.setAge(22);
        person1.setTeacher(true);
        log.info(person1.toString());

        Person person2 = new Person("Darcy", 22, true);
        log.info(person2.toString());

        boolean equals = person1.equals(person2);
        log.info("Equals:" + equals);
        log.info(person1.hashCode() + " and " + person2.hashCode());
    }
}

//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
class Person {
    private String name;
    private Integer age;
    private boolean isTeacher;
}
```

可以看到 Person 类除了几个属性定义之外没有其他任何方法代码，运行 LombokTest 可以在控制台看到输出如下。

```log
12:12:57.687 [main] INFO  Lombok - Person(name=Darcy, age=22, isTeacher=true)
12:12:57.690 [main] INFO  Lombok - Person(name=Darcy, age=22, isTeacher=true)
12:12:57.690 [main] INFO  Lombok - Equals:true
12:12:57.690 [main] INFO  Lombok - 1423350487 and 1423350487

Process finished with exit code 0
```

