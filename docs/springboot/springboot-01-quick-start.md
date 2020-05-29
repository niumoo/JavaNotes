---
title: Springboot 系列（一）Spring Boot 入门篇
toc_number: false
date: 2019-01-01 15:14:17
url: springboot/springboot01-quick-start
tags:
- Springboot
categories:
- Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

> 注意：本 Spring Boot 系列文章基于 Spring Boot 版本 **v2.1.1.RELEASE** 进行学习分析，版本不同可能会有细微差别。

## 前言

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/a60be3362289ed4d901bae342a685f84.png)

由于 J2EE 的开发变得笨重，繁多的配置，错乱的依赖管理，低下的开发效率，复杂的部署流程，第三方技术的集成难度较大等。同时随着复杂项目的演进，微服务分布式架构思想逐渐进入开发者的视野。

## 1. Spring Boot 介绍
`Spring Boot` 提供了一组工具只需要极少的配置就可以快速的构建并启动基于 Spring 的应用程序。解决了传统 Spring 开发需要配置大量配置文件的痛点，同时 `Spring Boot` 对于第三方库设置了合理的默认值，可以快速的构建起应用程序。当然 `Spring Boot` 也可以轻松的自定义各种配置，无论是在开发的初始阶段还是投入生成的后期阶段。

<!-- more -->
## 2. Spring Boot 优点
- 快速的创建可以独立运行的 Spring 项目以及与主流框架的集成。
- 使用嵌入式的 Servlet 容器，用于不需要打成war包。
- 使用很多的启动器（Starters）自动依赖与版本控制。
- 大量的自动化配置，简化了开发，当然，我们也可以修改默认值。
- 不需要配置 XML 文件，无代码生成，开箱即用。
- 准生产环境的运行时应用监控。
- 与云计算的天然集成。


## 3. Spring Boot 前置
说了那么多的 Spring Boot 的好处，那么使用 Spring Boot 需要哪些前置知识呢？我简单列举了一下。
- Spring 框架的使用。
- Maven 构建工具的使用。
- IDEA 或其他开发工具的使用。

## 4. Spring Boot 体验
现在我们已经了解了 Spring Boot 是什么，下面我们将使用 Spring Boot 开发一个入门案例，来体验 Spring Boot 开发姿势是如何的优雅与迅速。
Spring Boot 官方已经为我们如何快速启动 Spring Boot 应用程序提供了多种方式。

你可以在 Spring 官方网站直接生成项目下载导入IDE进行开发。

> https://start.spring.io/

也可以直接克隆 GitHub 上的初始项目进行体验。

```shell
git clone https://github.com/spring-guides/gs-spring-boot.git
cd gs-spring-boot/initial
```

这里我们选择后者，直接克隆进入到 initial 文件夹使用 maven 进行编译启动。

```shell
 mvn package && java -jar target/gs-spring-boot-0.1.0.jar
```

第一次编译需要下载需要的依赖，耗时会比较长，编译完成之后紧接着可以看到 Spring 的启动标志。这时 Spring Boot 的 web程序已经运行在8080端口了。

```shell
$ curl -s localhost:8080
Greetings from Spring Boot!
```

## 5. Spring Boot 开发

下面手动编写一个 Spring Boot 入门案例，快速的开发一个 web mvc 应用。
项目结构如下：

![Spring boot 项目结构](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/8b8a006da0df292015c9895b337a3ba4.png)

### 5.1 依赖项

```xml

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

`spring-boot-starter-parent` 是Spring Boot 的核心依赖，它里面定义了各种在开发中会用到的第三方 jar  的版本信息，因此我们在引入其他的 Spring Boot 为我们封装的启动器的时候都不在需要指定版本信息。如果我们需要自定义版本信息，可以直接覆盖版本属性值即可。

`spring-boot-starter-web`  提供 web 以及 MVC 和 validator 等web开发框架的支持。

`spring-boot-starter-test`  提供测试模块的支持。如 Junit，Mockito。

需要说明的是，Spring Boot 为我们提供了很多的已经封装好的称为启动器（starter）的依赖项。让我们在使用的时候不需要再进行复杂的配置就可以迅速的进行应用集成。所有的官方启动器依赖可以在[这里](https://github.com/spring-projects/spring-boot/tree/v2.1.1.RELEASE/spring-boot-project/spring-boot-starters)查看。

> 所有**官方**发布的启动器都遵循类似的命名模式; `spring-boot-starter-*`，这里`*`是指特定类型的应用程序。此命名结构旨在帮助您寻找启动器。
>
> 注意：编写自己的启动器的时候不应该使用这种命名方式。

### 5.2 启动类

```java
@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            // 开始检查spring boot 提供的 beans
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }
}

```

`@SpringBootApplication`  注解是一个便利的注解，它包含了以下几个注解。

1. `@Configuration` 定义配置类。

2. `@EnableAutoConfiguration` 开启自动配置。

3. `@EnableWebMvc` 标记为 web应用程序。

4. `@ComponentScan` 组件扫描。

### 5.3 控制器

```java
@RestController
public class HelloController {
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
```

`@RestController` 是 `@Controller` 与 `@ResponseBody` 的结合体。

### 5.4 访问测试

直接启动 `HelloApplication.java` 类就可以在控制台看到启动输出，然后访问8080端口查看启动是否正常。

![Spring boot 项目结构](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/f8d782a2b291c6e082c116f5792088bb.png)

经过上面的例子，已经使用 Spring Boot 快速的创建了一个 web 应用并进行了简单的访问测试。

## 6. Spring Boot 单元测试

结合上面提到的 Spring Boot 启动器知识，Spring Boot 已经为我们提供了丰富的第三方框架，测试框架也不例外。

导入单元测试依赖。

```java
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>	
```

### 6.1 模拟请求测试

编写单元测试

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void contextLoads() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Greetings from Spring Boot!"));
    }

}

```

关于上面代码的一些说明。

- **MockMvc** 允许我们方便的发送 HTTP 请求。
- **SpringBootTest** 方便的创建一个 Spring Boot 项目的测试程序。

运行没有任何异常说明程序测试通过。

### 6.2 Spring Boot 集成测试

```java

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

/**
 * <p>
 * 嵌入式服务器由随机端口启动webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
 * 并且在运行时发现实际端口@LocalServerPort
 *
 * @Author niujinpeng
 * @Date 2018/12/4 15:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloApplicationTestBySpringBoot {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setup() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void getHello() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assert (response.getBody().equals("Greetings from Spring Boot!"));
    }

}

```

嵌入式服务器由随机端口启动 `webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT`

并且在运行时使用注解 `@LocalServerPort` 发现实际端口。

运行测试类通过输出。

```log
2018-12-06 22:28:01.914  INFO 14320 --- [o-auto-1-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2018-12-06 22:28:01.914  INFO 14320 --- [o-auto-1-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2018-12-06 22:28:01.937  INFO 14320 --- [o-auto-1-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 23 ms
```

文章代码已经上传到 GitHub [Spring Boot 入门案例](https://github.com/niumoo/springboot/tree/master/springboot-hello)。


### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变的优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)