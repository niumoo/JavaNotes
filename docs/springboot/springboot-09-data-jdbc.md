---
title: Springboot 系列（九）使用 Spring JDBC 和 Druid 数据源监控
toc_number: false
date: 2019-02-27 23:40:01
url: springboot/springboot-09-data-jdbc
tags:
 - Springboot
 - Druid
 - Spring JDBC
categories:
 - Springboot
typora-root-url: ..\..
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

## 前言
![监控](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/129bdbc0a9f53e0fd3b748978ccd5fe6.png)

作为一名 Java 开发者，相信对 JDBC（Java Data Base Connectivity）是不会陌生的，JDBC作为 Java 基础内容，它提供了一种基准，据此可以构建更高级的工具和接口，使数据库开发人员能够编写数据库应用程序。下面演示下 Springboot 中如何使用 JDBC 操作，并配置使用 Druid 连接池，体验 Druid 对数据库操作强大的监控和扩展功能。Alibaba-Durid 官方手册[点这里](https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)。  
<!-- more -->

## 1. 数据库准备
使用mysql数据库创建数据库 springboot，并在库中新建数据表 user 并新增两条信息。
```sql
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `age` int(11) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `skills` varchar(255) DEFAULT NULL,
  `username` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

# 新增数据
INSERT INTO `springboot`.`user`(`id`, `age`, `birthday`, `password`, `skills`, `username`) VALUES (1, 17, '2019-01-12 21:02:30', '123', 'Go', 'Darcy');
INSERT INTO `springboot`.`user`(`id`, `age`, `birthday`, `password`, `skills`, `username`) VALUES (3, 23, '2019-01-01 00:11:22', '456', 'Java', 'Chris');
```
## 2. 添加依赖
新建一个 Springboot项目，这里不说。添加依赖如下。
```xml
    <dependencies>
        <!-- spring jdbc 操作模版 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <!-- springboot web开发 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- mysql 数据库连接 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- 引入druid数据源 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.12</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>
```
## 3. 配置数据源信息
常规的 JDBC 配置不需要配置这么多内容，这里因为使用了 Druid 连接池，所以配置了 Druid 部分。对自动配置不理解的可以查看系列文章[Springboot 系列（二）Spring Boot 配置文件](https://www.wdbyte.com/2019/01/springboot/springboot02-config/#4-%E9%85%8D%E7%BD%AE%E7%9A%84%E4%BD%BF%E7%94%A8)。
```yml
spring:
  datasource:
    username: root
    password: 123
    url: jdbc:mysql://127.0.0.1:3306/springboot?characterEncoding=utf-8&serverTimezone=GMT%2B8
    driver-class-name: com.mysql.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource

    initialSize: 5
    minIdle: 5
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUAL
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
    #   配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    filters: stat
    maxPoolPreparedStatementPerConnectionSize: 20
    useGlobalDataSourceStat: true
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
```
配置完毕之后，配置信息还不能绑定到 Druid数据源中，还需要新建一个配置类绑定数据源和配置信息。
```java
/**
 * <p>
 * Druid 数据源配置
 *
 * @Author niujinpeng
 * @Date 2019/1/14 22:20
 */
@Configuration
public class DruidConfig {
    /**
     * 配置绑定
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druid() {
        return new DruidDataSource();
    }
}
```
到这里，数据源已经配置完毕，编写测试方法测试 druid 连接池是否生效。
```java

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootDataJdbcApplicationTests {
    @Autowired
    DataSource dataSource;
    /**
     * 测试JDBC数据源
     * @throws SQLException
     */
    @Test
    public void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }
}
```
运行看到 contextLoads 输出信息。
```
class com.alibaba.druid.pool.DruidDataSource
Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
2019-02-27 14:14:56.144  INFO 12860 --- [           main] com.alibaba.druid.pool.DruidDataSource   : {dataSource-1} inited
com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@3e104d4b
```
输出日志中的 com.alibaba.druid 说明 Druid 已经生效。
## 4. 使用 Spring-JDBC
传统的 JDBC 使用中，需要编写大量代码，从构造 PreparedStatement 到查询不胜其烦。面对这样的开发痛点，Spring 封装了 Spring-jdbc. 让我们使用 JdbcTemplate 即可轻松的操作数据库。Spring-jdbc 的详细使用不是这篇文章重点，只简单演示下是否生效。  
编写控制器，查询一个 user 信息。
```java
@RestController
public class JdbcController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @ResponseBody
    @GetMapping("/query")
    public Map<String, Object> map() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * FROM user");
        return list.get(0);
    }
}
```
启动spring 项目，请求 /query 接口得到正常响应。
```json
{
"id": 1,
"age": 17,
"birthday": "2019-01-12T13:02:30.000+0000",
"password": "123",
"skills": "Go",
"username": "Darcy"
}
```
可见 Spring-JDBC 已经从数据库中取出了数据信息。
## 5. 使用 Druid 监控
如果使用 Druid 连接池却不使用监控功能，那么就有点暴殄天物了。下面开始配置 Druid 的 SQL 监控功能。在上面写的 DruidConfig 配置类中增加配置 Druid 的 Servlet 和 Filter.
```java
 	/**
     * Druid的servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet());
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "123");
        initParams.put("allow","127.0.0.1");
        bean.setInitParameters(initParams);
        bean.setUrlMappings(Arrays.asList("/druid/*"));
        return bean;
    }
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>(new WebStatFilter());
        HashMap<String, String> initParams = new HashMap<>();
        initParams.put("exclusions", "/css,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
```
上面配置了 Druid 监控访问路径为 `/druid`、登录用户是 `admin`、登录密码是`123`、允许访问的IP是`127.0.0.1` 本机、不需要监控的请求是 `/css` 和 `/druid` 开头的请求。

重新启动项目，访问测试 `/query`，然后访问 `/durid` 登录页。
![Druid 登录页](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/d6f42164708fcd4c8e5009386bb9837e.png)

登录后可以看到 SQL 监控信息和 URL 监控等信息。
![SQL 监控](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/64a71b68b6afb74e22a2eb274e8956c3.png)
URL 监控。
![URL 监控](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/7b96df5bbd8698295696a3805ad82e26.png)

文章代码已经上传到 GitHub [Spring Boot jdbc](https://github.com/niumoo/springboot/tree/master/springboot-data-jdbc)。

### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)