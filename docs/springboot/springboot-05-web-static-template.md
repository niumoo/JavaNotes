---
title: Springboot 系列（五）web 开发之静态资源和模版引擎
toc_number: false
date: 2019-02-15 22:32:01
url: springboot/springboot-05-web-static-template
tags:
 - Springboot
 - Thymelaf
 - FreeMarker
categories:
 - Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

## 前言
Spring Boot 天生的适合 web 应用开发，它可以快速的嵌入 Tomcat, Jetty  或 Netty 用于包含一个 HTTP 服务器。且开发十分简单，只需要引入 web 开发所需的包，然后编写业务代码即可。

## **自动配置原理？**

在进行 web 开发之前让我再来回顾一下自动配置，可以参考系列文章第三篇。Spring Boot 为 Spring MVC 提供了自动配置，添加了如下的功能：

- 视图解析的支持。
- 静态资源映射，WebJars 的支持。
- 转换器 Converter 的支持。
- 自定义 Favicon 的支持。
- 等等
<!-- more -->
在引入每个包时候我们需要思考是如何实现自动配置的，以及我们能自己来配置哪些东西，这样开发起来才会得心应手。

[关于 Spring Boot Web 开发的更详细介绍可以参考官方文档。](https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/boot-features-developing-web-applications.html)

## 1. JSON 格式转换

Spring Boot 默认使用 Jackson 进行 JSON 化处理，如果想要切换成 FastJson 可以首先从[官方文档](https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/howto-spring-mvc.html#howto-customize-the-responsebody-rendering)里查询信息。从这里知道对于 ResponseBody 的渲染主要是通过 HttpMessageConverters， 而首先引入FastJson Pom依赖并排除 Spring Boot 自带的 Jackson。

```xml
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
     <groupId>com.alibaba</groupId>
     <artifactId>fastjson</artifactId>
     <version>1.2.47</version>
</dependency>
```

编写转换器处理 json 的日期格式同时处理中文乱码问题。

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 自定义JSON转换器
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //日期格式化
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

        converter.setSupportedMediaTypes(fastMediaTypes);
        converter.setFastJsonConfig(fastJsonConfig);

        converters.add(converter);
    }
    
}
```

## 2. 静态资源映射

> By default, Spring Boot serves static content from a directory called `/static` (or `/public` or `/resources` or `/META-INF/resources`) in the classpath or from the root of the `ServletContext`.
### 2.1 默认映射
官方文档告诉我们 Spring Boot 对于静态资源的映射目录是 /static , /public , /resources 以及 /META-INF/resource。除此之外其实还映射了 `/webjars/**` 到 `classpath:/META-INF/resources/webjars`。

很明显此处是自动配置实现的，通过查看源码分析这段配置。

![Mvc静态资源映射](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/746f53d54281496bb8aafcc2e7f1ada6.png)

![Mvc静态资源映射](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/03afb17acb7e7a3ccd45a5b434f111d1.png)

而对于网站图标，Spring Boot 也已经配置了默认位置，可以在看到。

```java
// path: org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
@Bean
public SimpleUrlHandlerMapping faviconHandlerMapping() {
	SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
	mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
	mapping.setUrlMap(Collections.singletonMap("**/favicon.ico", // 图表
			faviconRequestHandler()));
	return mapping;
}

@Bean
public ResourceHttpRequestHandler faviconRequestHandler() {
	ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
	requestHandler.setLocations(resolveFaviconLocations());
	return requestHandler;
}

private List<Resource> resolveFaviconLocations() {
	String[] staticLocations = getResourceLocations(
			this.resourceProperties.getStaticLocations());
	List<Resource> locations = new ArrayList<>(staticLocations.length + 1);
	Arrays.stream(staticLocations).map(this.resourceLoader::getResource)
			.forEach(locations::add);
	locations.add(new ClassPathResource("/"));
	return Collections.unmodifiableList(locations);
}
```

根据 Spring Boot 默认的静态资源映射规则，可以直接把需要的静态资源放在响应的文件夹下然后直接引用即可。

![静态资源映射](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/4d1f6c2368dce288a2187741fc4dc0c7.png)

而放在 Public 文件夹下的 HTML 页面也可以直接访问。
![静态资源映射](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/79377b2dadbeee45f00da8921190938b.png)

### 2.2 webjars

[webjars](https://www.webjars.org/) 的思想是把静态资源打包到 Jar 包中，然后使用 JVM 构建工具进行管理，如 maven , Gradle 等。

使用 webjars 第一步需要进入依赖，如要使用 bootstrap。

```xml
 <!-- Web Jars 静态资源文件 -->
 <dependency>
     <groupId>org.webjars</groupId>
     <artifactId>bootstrap</artifactId>
     <version>4.1.3</version>
</dependency>
```

引入之后查看 bootstrap 资源。

![WebJars 引入 bootstrap](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/3626437cd5104cea9ed5a82ea25aac0c.png)

由于 Springboot 映射了 `/webjars/**` 到 `classpath:/META-INF/resources/webjars`. 因此可以直接在文件中引用 webjars 的静态资源。

```html
<!-- Bootstrap core CSS -->
<link href="/webjars/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
<script src="/webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
```



## 3. 模版引擎

Spring MVC 支持各种模版技术，如 Thymeleaf , FreeMarker , JSP 等。而Thyemeleaf 原型即页面的特性或许更符合 Spring Boot 快速开发的思想而被官方推荐。

![模版引擎原理](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/02c76ef91580600fb7265155822d5619.png)

[Thymeleaf](https://www.thymeleaf.org/) 是适用于 Web 开发的服务端 Java 模版引擎，Thymeleaf 为开发工作流程带来优雅自然的模版，由于其非侵入的特性，可以让页面不管是在静态原型下还是用作模版引擎时都有良好的页面展现。

```html
<table>
  <thead>
    <tr>
      <th th:text="#{msgs.headers.name}">Name</th>
      <th th:text="#{msgs.headers.price}">Price</th>
    </tr>
  </thead>
  <tbody>
    <tr th:each="prod: ${allProducts}">
      <td th:text="${prod.name}">Oranges</td>
      <td th:text="${#numbers.formatDecimal(prod.price, 1, 2)}">0.99</td>
    </tr>
  </tbody>
</table>
```

### 3.1 引入 Thymeleaf

```xml
 		<!-- thymeleaf 模版-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
```

### 3.2 使用 Thymeleaf 

根据 Spring Boot 自动配置原理，先看一下 Thymeleaf 的配置类，从中可以看出 Thymeleaf 的相关配置。我们可以知道 默认存放目录是 templates 文件夹，文件后缀为 `.html` 且开启了缓存。

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";
	/**
	 * Whether to enable template caching.
	 */
	private boolean cache = true;
```

为了在开发中编写模版文件时不用重启，可以在配置中关闭缓存。

```properties
# 关闭模版缓存
spring.thymeleaf.cache=false
# 如果需要进行其他的配置，可以参考配置类：ThymeleafProperties
# org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
```

编写 Controller 响应信息。

```java
  /**
     * 获取ID为1的用户信息
     *
     * @return
     */
    @GetMapping(value = "/user/1")
    public String getUserById(Model model) {
        User user1 = new User("Darcy", "password", 24, new Date(), Arrays.asList("Java", "GoLang"));
        User user2 = new User("Chris", "password", 22, new Date(), Arrays.asList("Java", "Web"));
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        model.addAttribute("userList", userList);
        model.addAttribute("user", user1);
        return "user";
    }
```

因为 Thymelaf 默认模版位置在 templates 文件夹下，因此在这个文件夹下编写页面信息。

```html
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Thymeleaf 的基本使用</title>
    <!-- 引入JS文件 -->
    <!--<script th:src="@{/static/js/alert.js}"></script>-->
</head>
<body>

<div>
    <p><b>Hello Thymeleaf Index</b></p>
    用户名称：<input th:id="${user.username}" th:name="${user.username}" th:value="${user.username}">
    <br/>
    用户技能：<input th:value="${user.skills}">
    <br/>
    用户年龄：<input th:value="${user.age}">
    <br/>
    用户生日：<input th:value="${#dates.format(user.birthday,'yyyy-MM-dd hh:mm:ss ')}">
</div>


<div th:object="${user}">
    <p><b>Hello Thymeleaf Index</b></p>

    用户名称：<input th:id="*{username}" th:name="*{username}" th:value="*{username}">
    <br/>
    用户技能：<input th:value="*{skills}">
    <br/>
    用户年龄：<input th:value="*{age}">
    <br/>
    用户生日：<input th:value="*{#dates.format(birthday,'yyyy-MM-dd hh:mm:ss')}">
</div>

<div>
    <p><b>Text 与 utext</b></p>
    <!-- th:text 显示HTML源码，作为字符串 -->
    <span th:text="${user.username}">abc</span>
    <br>
    <span th:utext="${user.username}">abc</span>
</div>

<div>
    <p><b>URL 的引用</b></p>
    <a th:href="@{https://www.baidu.com}">网站网址</a>
</div>

<div>
    <p><b>表单的使用</b></p>
    <form th:action="@{/th/postform}" th:object="${user}" method="post">
        用户名称：<input type="text" th:field="*{username}">
        <br/>
        用户技能：<input type="text" th:field="*{skills}">
        <br/>
        用户年龄：<input type="text" th:field="*{age}">
        <input type="submit">
    </form>
</div>

<div>
    <p><b>判断的使用</b></p>
    <div th:if="${user.age} == 18">18岁了</div>
    <div th:if="${user.age} gt 18">大于18岁</div>
    <div th:if="${user.age} lt 18">小于18岁</div>
    <div th:if="${user.age} ge 18">大于等于</div>
    <div th:if="${user.age} le 18">小于等于</div>
</div>

<div>
    <p><b>选择框</b></p>
    <select>
        <option>请选择一本书</option>
        <option th:selected="${user.username eq 'admin'}">管理员</option>
        <option th:selected="${user.username eq 'Darcy'}">Darcy</option>
        <option th:selected="${user.username eq 'Chris'}">Chris</option>
    </select>
</div>

<div>
    <p><b>遍历功能</b></p>
    <table>
        <tr>
            <th>用户名称</th>
            <th>年龄</th>
            <th>技能</th>
        </tr>
        <tr th:each="u:${userList}">
            <td th:text="${u.username}"></td>
            <td th:text="${u.age}"></td>
            <td th:text="${u.skills}"></td>
        </tr>
    </table>
</div>

<div>
    <p><b>Switch功能</b></p>
    <div th:switch="${user.username}">
        <p th:case="'admin'">欢迎管理员</p>
    </div>
</div>
</body>
</html>
```
访问页面可以看到数据正常显示。

![访问页面](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/5e7b0da4afded072ed59c42a34b3f7fc.png)

文章代码已经上传到 GitHub [Spring Boot Web开发 - 静态资源](https://github.com/niumoo/springboot/tree/master/springboot-web-staticfile)。
文章代码已经上传到 GitHub [Spring Boot Web开发 - 模版引擎](https://github.com/niumoo/springboot/tree/master/springboot-web-template)。


### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变的优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)