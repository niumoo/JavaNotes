---
title: Springboot 系列（三）Spring Boot 自动配置
toc_number: false
date: 2019-01-10 23:01:01
url: springboot/springboot03-auto-config
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

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/90be321394ddff7aa6dfdc9910888fda.png)

关于配置文件可以配置的内容，在 [Spring Boot 官方网站](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#common-application-properties)已经提供了完整了配置示例和解释。

可以这么说，Spring Boot 的一大精髓就是自动配置，为开发省去了大量的配置时间，可以更快的融入业务逻辑的开发，那么自动配置是怎么实现的呢？
<!-- more -->
## 1.  `@SpringBootApplication`

跟着 Spring Boot 的启动类的注解 `@SpringBootApplication` 进行源码跟踪，寻找自动配置的原理。

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
```

`@EnableAutoConfiguration` 开启自动配置。

`@ComponentScan` 开启注解扫描

从 `SpringBootApplication` 我们可以发现，这是一个简便的注解配置，它包含了自动配置，配置类，包扫描等一系列功能。

## 2. `@EnableAutoConfiguration`

继续跟踪，查看`@EnableAutoConfiguration` 源码，里面比较重要的是 `@Import` ，导入了一个翻译名为自动配置的选择器的类。这个类其实就是自动配置的加载选择器。

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({AutoConfigurationImportSelector.class})
public @interface EnableAutoConfiguration {
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}

```

继续跟踪 `AutoConfigurationImportSelector.class` .在这个类有一个重要的方法 `getCandidateConfigurations`.用于加载 Spring Boot 配置的自动配置类。

`getAutoConfigurationEntry` 会筛选出有效的自动配置类。

```java
protected AutoConfigurationEntry getAutoConfigurationEntry(
			AutoConfigurationMetadata autoConfigurationMetadata,
			AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return EMPTY_ENTRY;
		}
		AnnotationAttributes attributes = getAttributes(annotationMetadata);
		List<String> configurations = getCandidateConfigurations(annotationMetadata,
				attributes);
		configurations = removeDuplicates(configurations);
		Set<String> exclusions = getExclusions(annotationMetadata, attributes);
		checkExcludedClasses(configurations, exclusions);
		configurations.removeAll(exclusions);
		configurations = filter(configurations, autoConfigurationMetadata);
		fireAutoConfigurationImportEvents(configurations, exclusions);
		return new AutoConfigurationEntry(configurations, exclusions);
	}	

protected List<String> getCandidateConfigurations(AnnotationMetadata metadata,
			AnnotationAttributes attributes) {
		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(
				getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());
		Assert.notEmpty(configurations,
				"No auto configuration classes found in META-INF/spring.factories. If you "
						+ "are using a custom packaging, make sure that file is correct.");
		return configurations;
	}
```
下图是 DEBUG 模式下筛选之后的结果，因为我只添加了 web 模块，所以只有 web 相关的自动配置。

![筛选过后的自动配置](https://user-images.githubusercontent.com/26371673/50733348-ec781400-11c6-11e9-8f0d-01797d797d69.png)

## 3. xxxAutoConfiguration 与 xxxProperties

在上面的 debug 里，我们看到了成功加载的自动配置，目前只看到了配置类，却还没有发现自动配置值，随便选择一个 `AutoConfiguration` 查看源码。

这里选择了 `ServletWebServerFactoryAutoConfiguration`.

```java
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
//判断当前项目有没有这个类
//CharacterEncodingFilter；SpringMVC中进行乱码解决的过滤器；
@ConditionalOnClass(ServletRequest.class)
//Spring底层@Conditional注解（Spring注解版），根据不同的条件，如果
//满足指定的条件，整个配置类里面的配置就会生效； 判断当前应用是否是web应用，如果是，当前配置类生效
@ConditionalOnWebApplication(type = Type.SERVLET)
@EnableConfigurationProperties(ServerProperties.class)
@Import({ ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar.class,
		ServletWebServerFactoryConfiguration.EmbeddedTomcat.class,
		ServletWebServerFactoryConfiguration.EmbeddedJetty.class,
		ServletWebServerFactoryConfiguration.EmbeddedUndertow.class })
public class ServletWebServerFactoryAutoConfiguration {
```

需要注意的是 `@EnableConfigurationProperties(ServerProperties.class)`.他的意思是启动指定类的
`ConfigurationProperties`功能；将配置文件中对应的值和 `ServerProperties` 绑定起来；并把
`ServerProperties` 加入到 IOC 容器中。

再来看一下 `ServerProperties` .

```java
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
public class ServerProperties {

	/**
	 * Server HTTP port.
	 */
	private Integer port;
```

显而易见了，这里使用 ConfigurationProperties 绑定属性映射文件中的 server 开头的属性。结合默认配置
```
# 路径spring-boot-autoconfigure-2.1.1.RELEASE.jar
# /META-INF/spring-configuration-metadata.json

    {
      "name": "server.port",
      "type": "java.lang.Integer",
      "description": "Server HTTP port.",
      "sourceType": "org.springframework.boot.autoconfigure.web.ServerProperties",
      "defaultValue": 8080
    }
```
达到了自动配置的目的。

## 4. 自动配置总结

1. SpringBoot 启动的时候加载主配置类，开启了自动配置功能 @EnableAutoConfiguration 。
2. @EnableAutoConfiguration  给容器导入META-INF/spring.factories  里定义的自动配置类。
3. 筛选有效的自动配置类。
4. 每一个自动配置类结合对应的 xxxProperties.java 读取配置文件进行自动配置功能 。

## 5. 配置类

通过自动配置，我们发现已经帮我们省去了大量的配置文件的编写，那么在自定义配置的时候，我们是不是需要编写XML呢？Spring boot 尽管可以使用 `SpringApplication`XML 文件进行配置，但是我们通常会使用 `@Configuration` 类进行代替，这也是官方推荐的方式。

### 5.1 XML配置

定义 helloService Bean.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="helloService" class="net.codingme.boot.service.HelloService"></bean>

</beans>
```

引入配置。

```java
@ImportResource(value = "classpath:spring-service.xml")
@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
```

### 5.2 注解配置

此种方式和上面的XML配置是等效的，也是官方推荐的方式。`@Configuration` 注解的类（要在扫描的包路径中）会被扫描到。

```java
/**
 * <p>
 * 配置类，相当于传统Spring 开发中的 xml-> bean的配置
 *
 * @Author niujinpeng
 * @Date 2018/12/7 0:04
 */
@Configuration
public class ServiceConfig {

    /**
     * 默认添加到容器中的 ID 为方法名（helloService）
     *
     * @return
     */
    @Bean
    public HelloService helloService() {
        return new HelloService();
    }
}
```



## 6. 附录

| @Conditional扩展注解            | 作用（判断是否满足当前指定条件）                 |
| ------------------------------- | ------------------------------------------------ |
| @ConditionalOnJava              | 系统的java版本是否符合要求                       |
| @ConditionalOnBean              | 容器中存在指定Bean；                             |
| @ConditionalOnMissingBean       | 容器中不存在指定Bean；                           |
| @ConditionalOnExpression        | 满足SpEL表达式指定                               |
| @ConditionalOnClass             | 系统中有指定的类                                 |
| @ConditionalOnMissingClass      | 系统中没有指定的类                               |
| @ConditionalOnSingleCandidate   | 容器中只有一个指定的Bean，或者这个Bean是首选Bean |
| @ConditionalOnProperty          | 系统中指定的属性是否有指定的值                   |
| @ConditionalOnResource          | 类路径下是否存在指定资源文件                     |
| @ConditionalOnWebApplication    | 当前是web环境                                    |
| @ConditionalOnNotWebApplication | 当前不是web环境                                  |
| @ConditionalOnJndi              | JNDI存在指定项                                   |



文章代码已经上传到 GitHub [Spring Boot 自动配置](https://github.com/niumoo/springboot/tree/master/springboot-config)。


### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)