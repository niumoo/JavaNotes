---
title: Springboot 系列（十五）如何编写自己的 Springboot starter
toc_number: false
date: 2019-11-01 08:08:08
url: springboot/springboot-15-my-starter
tags:
- Springboot
- Springboot starter
categories:
- Springboot
typora-root-url: ..\
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![图片来自网络]](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572539675483.png)

# 1. 前言

`Springboot` 中的自动配置确实方便，减少了我们开发上的复杂性，那么自动配置原理是什么呢？之前我也写过了一篇文章进行了分析。  
[Springboot 系列（三）Spring Boot 自动配置](https://www.wdbyte.com/2019/01/springboot/springboot03-auto-config/)。
<!--more -->
由于自动配置用到了配置文件的绑定，如果你还不知道常见的配置文件的用法，可以参考这篇文章。  
[Springboot 系列（二）Spring Boot 配置文件](https://www.wdbyte.com/2019/01/springboot/springboot03-auto-config/)。

在这一次，通过学习 `Springboot` 自动配置模式，编写一个自己的 `starter`，用来加深对自动配置的理解。

熟悉模式，有助于提升编写的 `starter` 的规范性，编写自己的 `starter` 之前先来学习 `Springboot` 官方 `starter` 以及常见框架的整合 `starter` 的编写方式 ，可以领略到其中的奥秘。

# 2. Springboot 官方模式

选择一个官方的自动配置进行分析，这里就选择常见的配置端口号配置。

## 2.1. 引入依赖

使用端口号之前我们需要先引入 web 依赖。

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

如果你观察 `starter` 多的话，也许你发已经发现了一个**模式**，`Springboot` 官方的 `starter `的名字都是  `spring-boot-starter-xxxx`命名的。

查看 `spring-boot-starter-web` 会发现，其实这个依赖只是一个空盒子，除了依赖其他 `pom` 之外，没有一行代码。

![spring-boot-starter-web](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572480685107.png)

这时，发现了另外一个**模式**：`starter` 只依赖其他 `pom`，不做代码实现。

那么 `spring-boot-starter-web` 到底依赖了哪些内容？

![spring-boot-starter-web 的依赖](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572481136481.png)

观察这个依赖信息，然后再参照其他的官方 `starter` ，可以找到几个固定的引入，可以被称之为**模式**的依赖引入。

1. 依赖 `spring-boot-starter`。
2. 依赖 `spring-boot-autoconfigure`。

## 2.2. 自动配置

引入依赖只有配置端口号，像这样。

```properties
server.port=8090
```

IDEA 中可以通过点击 `server.port` 找到这个配置绑定的类文件。可以看到配置最终会注入到类`ServerProperties` 类的 `port` 属性上。

![Server 属性配置](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572478509712.png)

那么这个 `ServerProperties` 到底是哪里使用的呢？继续查找，找到一个和 `Servlet` 的有关的调用。

![getPort 的调用](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572478794011.png)

发现是被 `ServletWebServerFactoryCustomizer`类进行了调用，这个类里面定义了 

```java
private final ServerProperties serverProperties;
```

用来使用配置的属性。  
继续查看这个类的调用，发现只有一个类使用这个类，这个类是`ServletWebServerFactoryAutoConfiguration`。  

![ServletWebServerFactoryAutoConfiguration 类](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572479738431.png)

根据我们对注解的理解，这个类就是自动配置主要类了。同时自动配置类都是以 `AutoConfiguration` 结尾。

看这个类的几个注解的意思。  

1. 优先级别较高。

```java
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
```

2. 只有在 `ServletRequest` 类存在和是 Web 应用时生效。

```java
@ConditionalOnClass(ServletRequest.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
```

3. 开启了 `ServerProperties` 的配置绑定。

```java
@EnableConfigurationProperties(ServerProperties.class)
```

4. 导入了几个类。

```java
@Import({ ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar.class,
		ServletWebServerFactoryConfiguration.EmbeddedTomcat.class,
		ServletWebServerFactoryConfiguration.EmbeddedJetty.class,
		ServletWebServerFactoryConfiguration.EmbeddedUndertow.class })
```

同时注入配置到 Bean 工厂以供其他地方调用。

```java
@Bean
public ServletWebServerFactoryCustomizer servletWebServerFactoryCustomizer(ServerProperties serverProperties) {
	return new ServletWebServerFactoryCustomizer(serverProperties);
}
```

自动配置仅仅是这些东西吗？根据之前文章里的分析，我们知道不止代码，至少还有一个指定自动配置类的配置文件需要读取。也就是 `spring.factories` 文件。

![spring.factories](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572480162756.png)

如果你不知道，可以先看这篇文章。[Springboot 系列（三）Spring Boot 自动配置](https://www.wdbyte.com/2019/01/springboot/springboot03-auto-config/) 。  
事实确实如此，可以在 `spring.factories` 中找到上面跟踪到的类。
也就是 `ServletWebServerFactoryAutoConfiguration`.

根据上面的分析，可以发现 `Springboot` 官方 `starter` 的几个**模式**。  
1. 使用 `XXXProperties` 自动绑定 `XXX` 开头的配置信息，如：`ServerProperties`。
2. 把 `XXXProperties` 定义到要使用的类中，如：`ServletWebServerFactoryCustomizer`。
3. 编写一个 `XXXAutoConfiguration` ，开启  `XXXProperties` 的自动配置，限定生效场景，创建需要的类到 `Bean` 工厂。如：`ServletWebServerFactoryAutoConfiguration`。

# 3. 第三方集成模式

`Springboot` 官方如果把所有的框架都编写成 `starter`，是不现实的。因此很多第三方框架需要主动集成到 `springboot`，所以我们选择一个常用的框架分析它的 `starter` 实现。因为已经看过了 `springboot` 官方 `starter` 是如何配置的， 第三方框架也是类似，所以在下面观察的过程中会直接指出相同点，而不再做对比详细对比。

这里选择 `mybatis-spring-boot-starter` 进行学习分析。

## 3.1 引入依赖

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
```

这里 `mybatis` 框架的 `starter` 依赖符合一定的**规则**，即 **xxx-spring-boot-starter**.

观察这个 `starter`，发现它也没有做任何的代码实现，这一点和 `springboot` 官方一致。

![mybatis-spring-boot-starter](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572532932799.png)

查看 `mybatis-spring-boot-starter` 的依赖项，有很多，其中和自动配置有关的主要是。

```xml
<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-autoconfigure</artifactId>
</dependency>
```

## 3.2 自动配置

查看 `mybatis-spring-boot-autoconfigure` 的内容发现和 `springboot` 官方的 `autoconfigure`结构上是差不多的。

![mybatis-spring-boot-autoconfigure](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572533517315.png)

`mybatis` 的自动配置也是通过 `spring.factories` 来指明自动配置，然后通过 `XxxAutoConfiguration` 绑定 `XxxProperties` 来进行自动配置.

![MybatisAutoConfiguration](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572533809395.png)

在原理上，和上面 `springboot` 官方的 `starter`是相同的，所以不做过多的介绍了。

# 4. 编写自己的 starter

说了那么多，终于到了实操环节，通过上面的介绍，我们可以大致得出编写自己的 `starter `步骤。

1. 创建名字为 `xxx-spring-boot-starter` 的启动器项目。
2. 创建名字为 `xxx-spring-boot-autoconfigure`的项目。
   - 编写属性绑定类 `xxxProperties`.
   - 编写服务类，引入 `xxxProperties`.
   - 编写自动配置类`XXXAutoConfiguration`注入配置。
   - 创建 `spring.factories` 文件，用于指定要自动配置的类。
3. 启动器项目为空项目，用来引入 `xxx-spring-boot-autoconfigure`等其他依赖。
4. 项目引入 `starter`，配置需要配置的信息。

## 4.1 创建启动器项目

由于启动器不需要代码实现，只需要依赖其他项目，所以直接创建一个空的 maven 项目。但是名字要规范。    
这里创建的 `starter` 是 `myapp-spring-boot-starter`。

![myapp-spring-boot-starter](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572534904150.png)

pom 文件非常简单，只需要引入接下来要创建的 `myapp-spring-boot-autoconfigure`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>net.codingme.starter</groupId>
    <artifactId>myapp-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!-- 启动器 -->
    <dependencies>
        <!--  引入自动配置项目 -->
        <dependency>
            <groupId>net.codingme.starter</groupId>
            <artifactId>myapp-spring-boot-autoconfigure</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

## 4.2 创建自动配置项目

结合上面对 `starter` 的分析，直接创建一个名字为 `myapp-spring-boot-autoconfigure` 的项目。项目中只引入 `springboot` 父项目以及 `spring-boot-starter`。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>net.codingme.starter</groupId>
    <artifactId>myapp-spring-boot-autoconfigure</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>myapp-spring-boot-autoconfigure</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
</project>

```

项目的总体结构看图。

![myapp-spring-boot-starter-autoconfigure](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572536336072.png)

在 `HelloProperties`中通过注解 `@ConfigurationProperties(prefix = "myapp.hello")`让类中的属性与 `myapp.hello`开头的配置进行绑定。

```java
/**
 * <p>
 *
 * @Author niujinpeng
 * @Date 2019/10/29 23:51
 */
@ConfigurationProperties(prefix = "myapp.hello")
public class HelloProperties {

    private String suffix;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
```

然后在 `HelloService`中的 `sayHello`方法使用 `HelloProperties` 中自动绑定的值。

```java
public class HelloService {
    HelloProperties helloProperties;
    
    public String sayHello(String name) {
        return "Hello " + name + "，" + helloProperties.getSuffix();
    }
    
    public HelloProperties getHelloProperties() {
        return helloProperties;
    }

    public void setHelloProperties(HelloProperties helloProperties) {
        this.helloProperties = helloProperties;
    }
}
```

为了让 `HelloService` 可以自动注入且能正常使用 `HelloProperties`，所以我们在   
`HelloServiceAutoConfiguration` 类中把 `HelloProperties.class` 引入，然后把 `HelloService` 注入到 `Bean`。

```java
/**
 * web应用才生效
 */
@ConditionalOnWebApplication
/**
 * 让属性文件生效
 */
@EnableConfigurationProperties(HelloProperties.class)
/***
 * 声明是一个配置类
 */
@Configuration
public class HelloServiceAutoConfiguration {

    @Autowired
    private HelloProperties helloProperties;

    @Bean
    public HelloService helloService() {
        HelloService helloService = new HelloService();
        helloService.setHelloProperties(helloProperties);
        return helloService;
    }
}
```

最后在 `spring.factories`中只需要指定要自动配置的类即可。

```
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
net.codingme.starter.HelloServiceAutoConfiguration
```

到这里，自动配置项目就完成了。可以在 `myapp-spring-boot-autoconfigure`项目执行 `mvn install` 把自动配置项目打包到本地仓库，然后使用相同的命令把 `myapp-spring-boot-starter` 安装到仓库。因为后者依赖于前者项目，所以这里前者需要先进 `mvn install`。

## 4.3 使用自定义的启动器

创建一个 `springboot`项目`myapp-spring-boot-starter-test`。

![myapp-spring-boot-starter-test](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572537470601.png)

引入 `web` 依赖，引入自己编写的 `myapp-spring-boot-starter`.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- 引入自己的 starter -->
<dependency>
    <groupId>net.codingme.starter</groupId>
    <artifactId>myapp-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

编写一个 `HelloController` 注入自动配置里的 `HelloService`用于测试。

```java
@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String sayHello(String name) {
        return helloService.sayHello(name);
    }
}
```

由于 `autoConfigure`  项目中定义了 `sayHello`  方法会输出“Hello”+传入的 name + 配置的 `hello.suffix`，所以我们在 `springboot` 配置文件中配置这个属性。

```properties
myapp.hello.suffix=早上好
```

运行测试项目，访问 /hello 路径传入一个 name 看看自动配置有没有生效。

![访问测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572537886411.png)

从测试结果可以看到自动配置的早上好已经生效了。到这里自己编写的 `starter`也已经完工。

项目已经传到 [Github](https://github.com/niumoo/springboot/tree/master/springboot-starter).  
 https://github.com/niumoo/springboot/tree/master/springboot-starter 
 
**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)