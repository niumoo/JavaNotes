---
title: Springboot 系列（十）使用 Spring data jpa 访问数据库
toc_number: false
date: 2019-03-01 01:40:01
url: springboot/springboot-10-data-jpa
tags:
 - Springboot
 - SpringData Jpa
categories:
 - Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![桌面生活（来自网络）](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/7bea43f15edb0db3ac708c346acd35c4.jpg)

## 前言
Springboot data jpa 和 Spring jdbc 同属于 Spring开源组织，在 Spring jdbc 之后又开发了持久层框架，很明显 Spring data jpa 相对于 Spring jdbc 更加的便捷强大，不然也就没有开发的必要了。根据下面的文章开始体验下 Spring data jpa 魅力。
<!-- more -->
## 1. Spring data jpa 介绍
Spring data jpa 是 Spring data 系列的一部分，使用它可以轻松的实现对数据访问层的增强支持，在相当长的一段时间内，实现应用程序的数据访问层一直很麻烦，需要编写大量的样板式的代码来执行简单查询或者分页操作。Spring data jpa 的目标是尽量的减少实际编码来改善数据访问层的操作。
## 2. Spring data jpa 依赖
这次的实验基于系列文章第九篇实验代码，代码中的数据源相关的配置也可以参考系列文章第九篇，这里只演示 Spring data jpa 部分。

创建Spring boot 项目，引入需要的依赖。
```xml
    <dependencies>
        <!-- Spring Boot web 开发整合 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>spring-boot-starter-json</artifactId>
                    <groupId>org.springframework.boot</groupId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- 阿里 fastjson -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.47</version>
        </dependency>

        <!-- Lombok 工具 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- 导入配置文件处理器，在配置springboot相关文件时候会有提示 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- 单元测试 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>

        <!-- 数据库访问 JPA-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!--添加数据库链接 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!-- 阿里 druid 数据源，Spring boot 中使用Druid要用这个  -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
    </dependencies>
```
## 3. Spring data jpa 配置
关于 Druid 数据源的配置不再说明，可以参考系列文章第九篇。
```properties
############################################################
# 服务启动端口号
server.port=8080
spring.profiles.active=dev
############################################################
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/springboot?characterEncoding=utf-8&serverTimezone=GMT%2B8
spring.datasource.driver-class-name= com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=123
# 使用 druid 数据源
spring.datasource.type: com.alibaba.druid.pool.DruidDataSource
spring.datasource.initialSize: 5
spring.datasource.minIdle: 5
spring.datasource.maxActive: 20
spring.datasource.maxWait: 60000
spring.datasource.timeBetweenEvictionRunsMillis: 60000
spring.datasource.minEvictableIdleTimeMillis: 300000
spring.datasource.validationQuery: SELECT 1 FROM DUAL
spring.datasource.testWhileIdle: true
spring.datasource.testOnBorrow: false
spring.datasource.testOnReturn: false
spring.datasource.poolPreparedStatements: true
spring.datasource.filters: stat
spring.datasource.maxPoolPreparedStatementPerConnectionSize: 20
spring.datasource.useGlobalDataSourceStat: true
spring.datasource.connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
# SpringBoot JPA
spring.jpa.show-sql=true
# create 每次都重新创建表，update，表若存在则不重建
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL55Dialect
```
`spring.jpa.show-sql=true` 打印 SQL 语句。  
`spring.jpa.hibernate.ddl-auto=update` 根据 Enity 自动创建数据表，Update 表示如果表存在则不重新创建。

## 4. Spring data jpa 编码
Springboot Data JPA 是 ORM 的完整实现，实体类和数据表关系一一对应，因此实体类也就是数据表结构。`spring.jpa.hibernate.ddl-auto=update` 会在 JPA 运行时自动在数据表中创建被 `@Entity` 注解的实体数据表。如果表已经存在，则不会创建。
### 4.1. 数据实体类
```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 *
 * @Entity JPA实体
 * @Data GET SET TOSTRING
 * @NoArgsConstructor 无参构造
 * @AllArgsConstructor 全参构造
 * @Author niujinpeng
 * @Date 2018/12/19 17:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    /**
     * 用户ID
     *
     * @Id 主键
     * @GeneratedValue 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "username", length = 32, nullable = false)
    @NotNull(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @Column(name = "password", length = 32, nullable = false)
    @NotNull(message = "密码不能为空")
    private String password;
    /**
     * 年龄
     */
    @Column(name = "age", length = 3)
    private Integer age;
    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date birthday;
    /**
     * 技能
     */
    private String skills;
}
```

### 4.2. JPA 操作接口
JPA 操作接口只需要继承 JpaRepository 就可以了，JpaRepository 里封装了常用的增删改查分页等方法，可以直接使用，如果需要自定义查询方式，可以通过构造方法名的方式增加。下面增加了一个根据 username 和 password 查询 User 信息的方法。

```java
package net.codingme.boot.domain.repository;

import net.codingme.boot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *
 * @Author niujinpeng
 * @Date 2019/1/1114:26
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
     /**
     * 一个自定义方法，根据 username 和 password 查询 User 信息
     */
    User findByUsernameAndPassword(String username, String password);
}
```
到这里，Jpa 的功能已经可以测试使用了，关于 Service 层和 Controller 就不在这里贴了，直接编写 Springboot 单元测试进行 Jpa 测试。

## 5. Spring data jpa 测试
使用 Springboot 的单元测试方法可以方便的测试 Springboot 项目，对 Springboot 单元测试不了解的可以直接参照[官方文档](https://docs.spring.io/spring-boot/docs/2.1.x/reference/htmlsingle/#boot-features-testing-spring-applications)的说明，当然，也可以直接看下面的示例代码。  
下面编写四个测试方法分别测试根据 Id 查询、分页查询、更新数据、根据 username 和 password 查询四个功能。
```java
package net.codingme.boot.domain.repository;

import net.codingme.boot.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.Optional;

/**
 * 单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * id查询
     */
    @Test
    public void findByIdUserTest() {
        Optional<User> userOptional = userRepository.findById(1);
        User user = userOptional.orElseGet(null);
        System.out.println(user);
        Assert.assertNotNull(user);
    }

    /**
     * 分页查询
     */
    @Test
    public void findByPageTest() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<User> userPage = userRepository.findAll(pageRequest);
        List<User> userList = userPage.getContent();
        userList.forEach((user) -> System.out.println(user));
        Assert.assertNotNull(userList);
    }

    /**
     * 更新
     */
    @Test
    public void updateUserTest() {
        Optional<User> userOptional = userRepository.findById(1);
        User user = userOptional.orElseThrow(() -> new RuntimeException("用户信息没有取到"));
        System.out.println(user.getAge());
        ;
        user.setAge(user.getAge() + 1);
        User updateResult = userRepository.save(user);
        Assert.assertNotNull(updateResult);
    }

    /**
     * 根据 Username 和 Password 查询
     */
    @Test
    public void findByUsernameAndPasswordTest() {
        User user = userRepository.findByUsernameAndPassword("Darcy", "123");
        System.out.println(user);
        Assert.assertNotNull(user);
    }
}
```
首先看到四个方法全部运行通过。
![单元测试结果](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/77d538a467dd500356c519b67f41b7a6.png)
分页查询查出数据库中的两条数据。
```log
Hibernate: select user0_.id as id1_0_, user0_.age as age2_0_, user0_.birthday as birthday3_0_, user0_.password as password4_0_, user0_.skills as skills5_0_, user0_.username as username6_0_ from user user0_ limit ?
Hibernate: select count(user0_.id) as col_0_0_ from user user0_
User(id=1, username=Darcy, password=123, age=18, birthday=2019-01-12 21:02:30.0, skills=Go)
User(id=3, username=Chris, password=456, age=23, birthday=2019-01-01 00:11:22.0, skills=Java)
```
根据 Id 查询也没有问题。
```
Hibernate: select user0_.id as id1_0_0_, user0_.age as age2_0_0_, user0_.birthday as birthday3_0_0_, user0_.password as password4_0_0_, user0_.skills as skills5_0_0_, user0_.username as username6_0_0_ from user user0_ where user0_.id=?
User(id=1, username=Darcy, password=123, age=18, birthday=2019-01-12 21:02:30.0, skills=Go)
```
更新操作也是正常输出 SQL ，没有任何异常。
```
Hibernate: select user0_.id as id1_0_0_, user0_.age as age2_0_0_, user0_.birthday as birthday3_0_0_, user0_.password as password4_0_0_, user0_.skills as skills5_0_0_, user0_.username as username6_0_0_ from user user0_ where user0_.id=?
18
Hibernate: select user0_.id as id1_0_0_, user0_.age as age2_0_0_, user0_.birthday as birthday3_0_0_, user0_.password as password4_0_0_, user0_.skills as skills5_0_0_, user0_.username as username6_0_0_ from user user0_ where user0_.id=?
Hibernate: update user set age=?, birthday=?, password=?, skills=?, username=? where id=?
```
最后一个是自定义查询操作，上面三个方法的输出中，Darcy 用户对应的年龄是 18，在经过更新加1 之后应该变为19，下面是自定义查询的结果。
```
Hibernate: select user0_.id as id1_0_, user0_.age as age2_0_, user0_.birthday as birthday3_0_, user0_.password as password4_0_, user0_.skills as skills5_0_, user0_.username as username6_0_ from user user0_ where user0_.username=? and user0_.password=?
User(id=1, username=Darcy, password=123, age=19, birthday=2019-01-12 21:02:30.0, skills=Go)
```
可见是没有任何问题的。  
文章代码已经上传到 [GitHub](https://github.com/niumoo/springboot/tree/master/springboot-data-jpa)。  
测试代码中使用了一些 JDK8 的特性，如 `Optional` 类的使用，以后会单独写一部分关于 JDK 新特性的文章，欢迎扫码关注公众号。


**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)