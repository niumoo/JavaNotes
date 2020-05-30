---
title: Springboot 系列（十七）迅速使用 Spring Boot Admin 监控你的 Spring Boot 程序
toc_number: false
date: 2019-12-23 08:08:08
url: springboot/springboot-17-admin
tags:
- Springboot
- 生产工具
- Springboot Admin
categories:
- Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

## 1. Spring Boot Admin 是什么

Spring Boot Admin 是由 [codecentric](github.com/codecentric) 组织开发的开源项目，使用 Spring Boot Admin 可以管理和监控你的 Spring Boot 项目。它分为客户端和服务端两部分，客户端添加到你的 Spring Boot 应用增加暴漏相关信息的 HTTP 接口，然后注册到 Spring Boot Admin 服务端，这一步骤可以直接向服务端注册，也可以通过 Eureka 或者 Consul 进行注册。而 Spring Boot Admin Server 通过 Vue.js 程序监控信息进行可视化呈现。并且支持多种事件通知操作。
<!-- more -->
## 2. Spring Boot Admin 服务端

Spring Boot Admin 服务端是基于 Spring Boot 项目的，如何创建一个 Spring Boot 项目这里不提，你可以参考之前文章或者从 [https://start.spring.io/](https://start.spring.io/)  直接获得一个 Spring Boot 项目。

### 2.1. 添加依赖

只需要添加 web 依赖和 Spring-boot-admin-starter-server 依赖。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
</dependency>
```



### 2.2. 启动配置

为了和下面的客户端端口不冲突，先修改端口号为 9090。

```yml
server:
  port: 9090
```

添加 `@EnableAdminServer` 注解启用 Spring Boot Admin Server 功能。

```java
@EnableAdminServer
@SpringBootApplication
public class SpringbootAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAdminServerApplication.class, args);
    }
}
```

服务端已经配置完成，启动项目进行访问就可以看到 Spring Boot Admin Server 的页面了。

![Spring Boot Admin Server UI](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191221204941292.png)

## 3. Spring Boot Admin 客户端

创建 Spring Boot 项目依旧不提，这里只需要添加  Spring Boot Admin 客户端需要的依赖，在项目启动时就会增加相关的获取信息的 API 接口。然后在 Spring Boot 配置文件中配置 Spring Boot Admin 服务端，就可以进行监控了。

### 3.1 客户端依赖

pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-client</artifactId>
</dependency>
<!-- Lombok 工具,与 admin client 无关 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

### 3.2 客户端配置

客户端配置主要为了让客户端可以成功向服务端注册，所以需要配置客户端所在应用相关信息以及 Spring Boot Admin Server 服务端的 url。

```yml
server:
  port: 8080

spring:
  application:
    # 应用名称
    name: sjfx-api-search
  jmx:
    enabled: true
  boot:
    admin:
      client:
        # 服务端 url
        url: http://127.0.0.1:9090
        instance:
          # 客户端实例 url
          service-url: http://127.0.0.1:8080
          prefer-ip: true
          # 客户端实例名称
          name: sjfx-api-search

management:
  endpoints:
    web:
      exposure:
      	# 暴漏的接口 - 所有接口
        include: "*"
```

配置中的  `include: "*"` 公开了所有的端口，对于生产环境，应该自信的选择要公开的接口。

Spring Boot Admin 可以获取应用中的定时任务，所以在代码中增加一个定时任务计划，每 20 秒输出一次当前时间，日志级别为 `INFO`，用于下面的定时任务和日志监控测试。

```java
@Slf4j
@SpringBootApplication
@EnableScheduling
public class SpringbootAdminClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAdminClientApplication.class, args);
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void run20s() {
        log.info("定时任务:{}", LocalDateTime.now());
    }
}
```

### 3.3. 客户端运行

启动客户端会暴漏相关的运行状态接口，并且自动向配置的服务端发送注册信息。

下面是客户端的启动日志：

```log
2019-12-21 22:45:32.878  INFO 13204 --- [           main] n.c.b.SpringbootAdminClientApplication   : Starting SpringbootAdminClientApplication on DESKTOP-8SCFV4M with PID 13204 (D:\IdeaProjectMy\springboot-git\springboot-admin\springboot-admin-client\target\classes started by 83981 in D:\IdeaProjectMy\springboot-git\springboot-admin)
2019-12-21 22:45:32.881  INFO 13204 --- [           main] n.c.b.SpringbootAdminClientApplication   : No active profile set, falling back to default profiles: default
2019-12-21 22:45:33.627  INFO 13204 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2019-12-21 22:45:33.634  INFO 13204 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2019-12-21 22:45:33.634  INFO 13204 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.29]
2019-12-21 22:45:33.706  INFO 13204 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2019-12-21 22:45:33.706  INFO 13204 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 797 ms
2019-12-21 22:45:33.850  INFO 13204 --- [           main] o.s.b.a.e.web.ServletEndpointRegistrar   : Registered '/actuator/jolokia' to jolokia-actuator-endpoint
2019-12-21 22:45:33.954  INFO 13204 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2019-12-21 22:45:34.089  INFO 13204 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService
2019-12-21 22:45:34.117  INFO 13204 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService 'taskScheduler'
2019-12-21 22:45:34.120  INFO 13204 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 15 endpoint(s) beneath base path '/actuator'
2019-12-21 22:45:34.162  INFO 13204 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-12-21 22:45:34.163  INFO 13204 --- [           main] n.c.b.SpringbootAdminClientApplication   : Started SpringbootAdminClientApplication in 1.563 seconds (JVM running for 2.131)
2019-12-21 22:45:34.271  INFO 13204 --- [gistrationTask1] d.c.b.a.c.r.ApplicationRegistrator       : Application registered itself as 6bcf19a6bf8c
2019-12-21 22:45:34.293  INFO 13204 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2019-12-21 22:45:34.294  INFO 13204 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2019-12-21 22:45:34.300  INFO 13204 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 6 ms
```

从启动日志里的 `Exposing 15 endpoint(s) beneath base path '/actuator'` 这段，可以看到暴漏了 15 个 `/actuator` 的 API 接口，可以直接访问查看响应结果。

![Spring Boot Admin Client 监测接口](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191221224351221.png)

从日志 `Application registered itself as 6bcf19a6bf8c` 可以看到客户端已经注册成功了。再看服务端可以看到注册上来的一个应用实例。

![Spring Boot Admin Server](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191221225022655.png)

## 4. Spring Boot Admin 功能

点击监控页面上的在线的应用实例，可以跳转到应用实例详细的监控管理页面，也就是 Vue.js 实现的 web 展示。

![Spring Boot Admin Server 监控页面](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191222180411114.png)

Spring Boot Admin Server 可以监控的功能很多，使用起来没有难度，下面描述下可以监测的部分内容：

- 应用运行状态，如时间、垃圾回收次数，线程数量，内存使用走势。
- 应用性能监测，通过选择 JVM 或者 Tomcat 参数，查看当前数值。
- 应用环境监测，查看系统环境变量，应用配置参数，自动配置参数。
- 应用 bean 管理，查看 Spring Bean ，并且可以查看是否单例。
- 应用计划任务，查看应用的计划任务列表。
- 应用日志管理，动态更改日志级别，查看日志。
- 应用 JVM 管理，查看当前线程运行情况，dump 内存堆栈信息。
- 应用映射管理，查看应用接口调用方法、返回类型、处理类等信息。

上面提到的日志管理，可以动态的更改日志级别，以及查看日志。默认配置下是只可以动态更改日志级别的，如果要在线查看日志，就需要手动配置日志路径了。

客户端上可以像下面这样配置日志路径以及日志高亮。

```yml
# 配置文件：application.yml
logging:
  file:
    name: boot.log
  pattern:
#     日志高亮
    file: '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID}){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx'
```

下面是在 Spring Boot Admin 监测页面上查看的客户端应用日志。

![Spring Boot Admin Server 查看日志](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191222193212726.png)

## 5. Spring Boot Admin 进阶

### 5.1. 邮件通知

Spring Boot Admin Server  支持常见的通知方式，比如邮件通知、电报通知、PagerDuty 通知等，下面将会演示常见的通知方式 - 邮件通知，最后也会演示如何通过监听时间进下设置自定义通知方式。

Spring Boot Admin Server 的邮件通知通过 Thymeleaf 模板发送 HTML 格式的电子邮件。因此，想要使用邮件通知首先要引入 Thymeleaf 依赖以及 `spring-boot-starter-mail` 依赖，并配置邮件发送者信息和接受者信息。

 **1. 添加依赖**

``` 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<!-- Thymeleaf 模版，用于发送模版邮件 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

**2. 配置邮件**

主要设置发送者信息和接收者信息。

```
spring:
  boot:
    admin:
      notify:
        mail:
          # 逗号分隔的邮件收件人列表
          to: xxxx@126.com
          # 开启邮箱通知
          enabled: true
          # 不需要发送通知的状态：从状态A:到状态B
          ignore-changes: {"UNKNOWN:UP"}
          # 逗号分隔的抄送收件人列表
          cc: xxxx@126.com
          # 发件人
          from: Spring Boot Admin<xxxx@126.com>
          
# 邮件发送者信息
  mail:
    host: smtp.126.com
    port: 25
    username: xxxx@126.com
    default-encoding: utf-8
    password: xxxx
```

如果想了解更多关于 Spring Boot 邮件发送信息，可以参考 [Spring Boot 系列文章第十三篇](https://mp.weixin.qq.com/s?__biz=MzI1MDIxNjQ1OQ==&mid=2247483764&idx=1&sn=8cee8b1781b8659b3fdc23c0d650db49&chksm=e984e810def3610640e741eea1f94bbf95d4a2a5b1c68946829a036b4a6bc58b0d23e156474a&token=971841717&lang=zh_CN#rd)。

配置好邮件通知之后，重启服务端和客户端，等客户端注册到服务端之后直接终止客户端的运行，稍等片刻就可以在配置的通知接收邮箱里收到客户端实例下线通知了。

![Sping Boot Admin Server 邮件通知](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191222215118514.png)

邮件通知使用的模板存放在 server 依赖的 classpath:/META-INF/spring-boot-admin-server/mail/status-changed.html 路径，如果想要自定义模板内容。可以拷贝这个文件放到自己的 templates 目录下，修改成自己想要的效果，然后在配置中指定自定义模板路径。

```yml
spring:
  boot:
    admin:
      notify:
        mail:
          # 自定义邮件模版
          template: classpath:/templates/notify.html
```

### 5.2 自定义通知

自定义通知只需要自己实现 Spring Boot Admin Server 提供的监听通知类即可，下面会演示如何在实例状态改变时输出实例相关信息。

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import de.codecentric.boot.admin.server.notify.LoggingNotifier;
import reactor.core.publisher.Mono;

@Component
public class CustomNotifier extends AbstractEventNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingNotifier.class);

    public CustomNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                LOGGER.info("Instance {} ({}) is {}", instance.getRegistration().getName(), event.getInstance(),
                    ((InstanceStatusChangedEvent)event).getStatusInfo().getStatus());
            } else {
                LOGGER.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(),
                    event.getType());
            }
        });
    }
}
```

### 5.2. 访问限制

上面提到过，因为客户端增加了暴漏运行信息的相关接口，所以在生产环境中使用存在风险，而服务端没有访问限制，谁的可以访问也是不合理的。

下面将会为客户端和服务端分别增加访问限制，客户端主要是限制敏感接口的访问权限，服务端则是全局的访问限制。这些访问限制都通过 spring 安全框架 security 来实现，所以首先要为客户端和服务端都增加 maven 依赖。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**1. 服务端**

在引入安全框架依赖之后，需要配置访问控制，比如静态资源不需要限制，登录登出页面指定等。

```java
import java.util.UUID;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import io.netty.handler.codec.http.HttpMethod;

@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    private final AdminServerProperties adminServer;

    public SecuritySecureConfig(AdminServerProperties adminServer) {
        this.adminServer = adminServer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

        http.authorizeRequests()
                .antMatchers(this.adminServer.path("https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/**")).permitAll()
                .antMatchers(this.adminServer.path("/login")).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(this.adminServer.path("/login")).successHandler(successHandler).and()
                .logout().logoutUrl(this.adminServer.path("/logout")).and()
                .httpBasic().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                        new AntPathRequestMatcher(this.adminServer.path("/instances"), HttpMethod.POST.toString()),
                        new AntPathRequestMatcher(this.adminServer.path("/instances/*"), HttpMethod.DELETE.toString()),
                        new AntPathRequestMatcher(this.adminServer.path("/actuator/**"))
                )
                .and()
                .rememberMe().key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600);
        // @formatter:on
    }

    // 代码配置用户名和密码的方式
    // Required to provide UserDetailsService for "remember functionality"
    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // auth.inMemoryAuthentication().withUser("user").password("{noop}password").roles("USER");
    // }
}
```

在 application.yml 配置文件中配置用户名和密码。

```yml
spring:
  security:
    user:
      name: user
      password: 123
```

重启服务端，再次访问就需要用户名和密码进行登录了。 

![Spring Boot Admin Server 登录](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191222222408380.png)

**2. 客户端**

客户端在引入安全框架之后，也需要配置访问权限，主要是配置哪些路径可以访问，哪些路径访问需要登录限制，默认所有接口都需要登录限制。

同样的，客户端应用也需要在配置中配置客户端应用对于敏感接口的登录用户和密码，同时需要配置 Spring Boot Admin Server 的访问用户和密码，还要把自身的用户和密码注册时告诉服务端，不然服务端不能获取到监测数据。

```yml
spring:
  security:
    user:
      # 客户端敏感接口用户和密码
      name: client
      password: 123
  application:
    # 应用名称
    name: sjfx-api-search
  jmx:
    enabled: true
  boot:
    admin:
      client:
        # 服务端 url
        url: http://127.0.0.1:9090
        instance:
          # 客户端实例 url
          service-url: http://127.0.0.1:8080
          prefer-ip: true
          # 客户端实例名称
          name: sjfx-api-search
          metadata:
            # 客户端自身的用户和密码告诉服务端
            user.name: client
            user.password: 123
        # 服务端用户名密码
        username: user
        password: 123
```

客户端敏感接口访问测试。

![客户端应用访问](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191222232720674.png)

到这里，客户端的敏感接口访问需要登录，服务端的管理页面也需要登录，客户端和服务端的访问控制已经完成了。

文中代码已经上传到：[github.com/niumoo/springboot/tree/master/springboot-admin](github.com/niumoo/springboot/tree/master/springboot-admin)

**参考资料：**

[https://github.com/codecentric/spring-boot-admin](https://github.com/codecentric/spring-boot-admin)

[https://codecentric.github.io/spring-boot-admin/current/](https://codecentric.github.io/spring-boot-admin/current/)


**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)