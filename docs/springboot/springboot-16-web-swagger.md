---
title: Springboot 系列（十六）你真的了解 Swagger 文档吗？
toc_number: false
date: 2019-11-26 08:08:08
url: springboot/springboot-16-web-swagger
tags:
- Springboot
- Swagger
categories:
- Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

## 前言

目前来说，在 Java 领域使用 `Springboot` 构建微服务是比较流行的，在构建微服务时，我们大多数会选择暴漏一个 `REST API` 以供调用。又或者公司采用前后端分离的开发模式，让前端和后端的工作由完全不同的工程师进行开发完成。不管是微服务还是这种前后端分离开发，维持一份完整的及时更新的 `REST API` 文档，会极大的提高我们的工作效率。而传统的文档更新方式（如手动编写），很难保证文档的及时性，经常会年久失修，失去应有的意义。因此选择一种新的 API 文档维护方式很有必要，这也是这篇文章要介绍的内容。
<!-- more -->

## 1. OpenAPI  规范介绍

![Open API](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/oas_original-01.png)

`OpenAPI Specification` 简称 OAS，中文也称  `OpenAPI` 描述规范，使用 `OpenAPI` 文件可以描述整个 API，它制定了一套的适合通用的与语言无关的 `REST API` 描述规范，如 API 路径规范、请求方法规范、请求参数规范、返回格式规范等各种相关信息，使人类和计算机都可以不需要访问源代码就可以理解和使用服务的功能。

下面是 `OpenAPI` 规范中建议的 API 设计规范，基本路径设计规范。
```shell
https://api.example.com/v1/users?role=admin&status=active
\________________________/\____/ \______________________/
         server URL       endpoint    query parameters
                            path
```
对于传参的设计也有规范，可以像下面这样：

- [路径参数](https://swagger.io/docs/specification/describing-parameters/#path-parameters), 例如 `/users/{id}`
- [查询参数](https://swagger.io/docs/specification/describing-parameters/#query-parameters), 例如 `/users?role=未读代码`
- [header 参数](https://swagger.io/docs/specification/describing-parameters/#header-parameters), 例如 `X-MyHeader: Value`
- [cookie 参数](https://swagger.io/docs/specification/describing-parameters/#cookie-parameters),  例如 `Cookie: debug=0; csrftoken=BUSe35dohU3O1MZvDCU`

`OpenAPI` 规范的东西远远不止这些，目前 `OpenAPI` 规范最新版本是 3.0.2，如果你想了解更多的 `OpenAPI` 规范，可以访问下面的链接。
[OpenAPI Specification (https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md)](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md)

## 2. Swagger 介绍
![swagger](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191125225207292.png)

很多人都以为 `Swagger` 只是一个接口文档生成框架，其实并不是。 Swagger  是一个围绕着 `OpenAPI Specification`（OAS，中文也称 OpenAPI规范）构建的一组开源工具。可以帮助你从 API 的设计到 API 文档的输出再到  API 的测试，直至最后的 API 部署等整个 API 的开发周期提供相应的解决方案，是一个庞大的项目。 Swagger  不仅免费，而且开源，不管你是企业用户还是个人玩家，都可以使用 Swagger 提供的方案构建令人惊艳的 `REST API`。

Swagger 有几个主要的产品。

- [Swagger Editor](http://editor.swagger.io/?_ga=2.112541447.2078165713.1574600445-3923049.1574128700) – 一个基于浏览器的 Open API 规范编辑器。
- [Swagger UI](https://swagger.io/swagger-ui/) – 一个将 OpenAPI 规范呈现为可交互在线文档的工具。
- [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) – 一个根据 OpenAPI 生成调用代码的工具。

如果你想了解更多信息，可以访问 Swagger 官方网站 [https://swagger.io](https://swagger.io)。


## 3. Springfox 介绍

源于 Java 中 Spring 框架的流行，让一个叫做  Marrty Pitt 的老外有了为 SpringMVC 添加接口描述的想法，因此他创建了一个遵守 OpenAPI 规范（OAS）的项目，取名为 *swagger-springmvc*，这个项目可以让 Spring 项目自动生成 JSON 格式的 OpenAPI 文档。这个框架也仿照了 Spring 项目的开发习惯，使用注解来进行信息配置。

后来这个项目发展成为 `Springfox`，再后来扩展出 `springfox-swagger2` ，为了让 JSON 格式的 API 文档更好的呈现，又出现了 `springfox-swagger-ui` 用来展示和测试生成的 OpenAPI 。这里的 springfox-swagger-ui 其实就是上面介绍的 Swagger-ui，只是它被通过 webjar 的方式打包到 jar 包内，并通过 maven 的方式引入进来。 

上面提到了 Springfox-swagger2 也是通过注解进行信息配置的，那么是怎么使用的呢？下面列举常用的一些注解，这些注解在下面的 Springboot 整合 Swagger 中会用到。

| 注解              | 示例                                                         | 描述                                       |
| ----------------- | ------------------------------------------------------------ | ------------------------------------------ |
| @ApiModel         | @ApiModel(value = "用户对象")                                | 描述一个实体对象                           |
| @ApiModelProperty | @ApiModelProperty(value = "用户ID", required = true, example = "1000") | 描述属性信息，执行描述，是否必须，给出示例 |
| @Api              | @Api(value = "用户操作 API(v1)", tags = "用户操作接口")      | 用在接口类上，为接口类添加描述             |
| @ApiOperation     | @ApiOperation(value = "新增用户")                            | 描述类的一个方法或者说一个接口             |
| @ApiParam         | @ApiParam(value = "用户名", required = true)                 | 描述单个参数                               |                       |                                            |

更多的 Springfox 介绍，可以访问 Springfox 官方网站。

[Springfox Reference Documentation (http://springfox.github.io)](http://springfox.github.io/springfox/docs/current/)

## 4. Springboot 整合 Swagger

就目前来说 ，Springboot 框架是非常流行的微服务框架，在微服务框架下，很多时候我们都是直接提供 `REST API` 的。REST API 如果没有文档的话，使用者就很头疼了。不过不用担心，上面说了有一位叫 Marrty Pitt 的老外已经创建了一个发展成为 Springfox 的项目，可以方便的提供 JSON 格式的 OpenAPI 规范和文档支持。且扩展出了 springfox-swagger-ui 用于页面的展示。

需要注意的是，这里使用的所谓的 Swagger 其实和真正的 Swagger 并不是一个东西，这里使用的是 Springfox 提供的 Swagger 实现。它们都是基于 OpenAPI 规范进行 API 构建。所以也都可以 Swagger-ui 进行 API 的页面呈现。

### 4.1. 创建项目

如何创建一个 Springboot 项目这里不提，你可以直接从 [Springboot 官方](https://start.spring.io/) 下载一个标准项目，也可以使用 idea 快速创建一个 Springboot 项目，也可以顺便拷贝一个 Springboot 项目过来测试，总之，方式多种多样，任你选择。

下面演示如何在 Springboot 项目中使用 swagger2。

### 4.2. 引入依赖

这里主要是引入了 springfox-swagger2，可以通过注解生成 JSON 格式的 OpenAPI 接口文档，然后由于 Springfox 需要依赖 jackson，所以引入之。springfox-swagger-ui 可以把生成的 OpenAPI 接口文档显示为页面。Lombok 的引入可以通过注解为实体类生成 get/set 方法。

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

    <!-- 引入swagger2的依赖-->
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.9.2</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>2.9.2</version>
    </dependency>
    
    <!-- jackson相关依赖 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.5.4</version>
    </dependency>

    <!-- Lombok 工具 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### 4.3. 配置 Springfox-swagger

Springfox-swagger 的配置通过一个 Docket 来包装，Docket 里的 apiInfo 方法可以传入关于接口总体的描述信息。而 apis 方法可以指定要扫描的包的具体路径。在类上添加 @Configuration 声明这是一个配置类，最后使用 @EnableSwagger2 开启 Springfox-swagger2。

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * Springfox-swagger2 配置
 *
 * @Author niujinpeng
 * @Date 2019/11/19 23:17
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.codingme.boot.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("未读代码 API")
                .description("公众号：未读代码(weidudaima) springboot-swagger2 在线借口文档")
                .termsOfServiceUrl("https://www.wdbyte.com")
                .contact("达西呀")
                .version("1.0")
                .build();
    }
}
```

### 4.4. 代码编写

文章不会把所有代码一一列出来，这没有太大意义，所以只贴出主要代码，完整代码会上传到 Github，并在文章底部附上 Github 链接。

参数实体类 `User.java`，使用 `@ApiModel` 和 ` @ApiModelProperty` 描述参数对象，使用 ` @NotNull` 进行数据校验，使用 `@Data` 为参数实体类自动生成 get/set 方法。

```java
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 用户实体类
 *
 * @Author niujinpeng
 * @Date 2018/12/19 17:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户对象")
public class User {

    /**
     * 用户ID
     *
     * @Id 主键
     * @GeneratedValue 自增主键
     */
    @NotNull(message = "用户 ID 不能为空")
    @ApiModelProperty(value = "用户ID", required = true, example = "1000")
    private Integer id;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "用户密码", required = true)
    private String password;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "用户年龄", example = "18")
    private Integer age;
    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "用户生日")
    private Date birthday;
    /**
     * 技能
     */
    @ApiModelProperty(value = "用户技能")
    private String skills;
}
```

编写 Controller 层，使用 `@Api` 描述接口类，使用 `@ApiOperation` 描述接口，使用 `@ApiParam` 描述接口参数。代码中在查询用户信息的两个接口上都添加了 ` tags = "用户查询"` 标记，这样这两个方法在生成 Swagger 接口文档时候会分到一个共同的标签组里。

```java
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.codingme.boot.domain.Response;
import net.codingme.boot.domain.User;
import net.codingme.boot.enums.ResponseEnum;
import net.codingme.boot.utils.ResponseUtill;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户操作
 *
 * @Author niujinpeng
 * @Date 2019/11/19 23:17
 */

@Slf4j
@RestController(value = "/v1")
@Api(value = "用户操作 API(v1)", tags = "用户操作接口")
public class UserController {

    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/user")
    public Response create(@Valid User user, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            log.info(message);
            return ResponseUtill.error(ResponseEnum.ERROR.getCode(), message);
        } else {
            // 新增用户信息 do something
            return ResponseUtill.success("用户[" + user.getUsername() + "]信息已新增");
        }
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/user/{username}")
    public Response delete(@PathVariable("username")
                           @ApiParam(value = "用户名", required = true) String name) throws Exception {
        // 删除用户信息 do something
        return ResponseUtill.success("用户[" + name + "]信息已删除");
    }

    @ApiOperation(value = "修改用户")
    @PutMapping(value = "/user")
    public Response update(@Valid User user, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            log.info(message);
            return ResponseUtill.error(ResponseEnum.ERROR.getCode(), message);
        } else {
            String username = user.getUsername();
            return ResponseUtill.success("用户[" + username + "]信息已修改");
        }
    }

    @ApiOperation(value = "获取单个用户信息", tags = "用户查询")
    @GetMapping(value = "/user/{username}")
    public Response get(@PathVariable("username")
                        @NotNull(message = "用户名称不能为空")
                        @ApiParam(value = "用户名", required = true) String username) throws Exception {
        // 查询用户信息 do something
        User user = new User();
        user.setId(10000);
        user.setUsername(username);
        user.setAge(99);
        user.setSkills("cnp");
        return ResponseUtill.success(user);
    }

    @ApiOperation(value = "获取用户列表", tags = "用户查询")
    @GetMapping(value = "/user")
    public Response selectAll() throws Exception {
        // 查询用户信息列表 do something
        User user = new User();
        user.setId(10000);
        user.setUsername("未读代码");
        user.setAge(99);
        user.setSkills("cnp");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        return ResponseUtill.success(userList);
    }
}
```

最后，为了让代码变得更加符合规范和好用，使用一个统一的类进行接口响应。

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "响应信息")
public class Response {
    /**
     * 响应码
     */
    @ApiModelProperty(value = "响应码")
    private String code;
    /**
     * 响应信息
     */
    @ApiModelProperty(value = "响应信息")
    private String message;

    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private Collection content;
}
```

### 4.5. 运行访问

直接启动 Springboog 项目，可以看到控制台输出扫描到的各个接口的访问路径，其中就有 `/2/api-docs`。

![Springboot 启动](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191126000341030.png)

这个也就是生成的 OpenAPI 规范的描述 JSON 访问路径，访问可以看到。

![OpenAPI - JSON](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191126000613238.png)

因为上面我们在引入依赖时，也引入了 springfox-swagger-ui 包，所以还可以访问 API 的页面文档。访问路径是 /swagger-ui.html，访问看到的效果可以看下图。

![swagger 访问](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191126000808167.png)

也可以看到用户查询的两个方法会归到了一起，原因就是这两个方法的注解上使用相同的 tag 属性。

### 4.7. 调用测试

springfox-swagger-ui 不仅是生成了 API 文档，还提供了调用测试功能。下面是在页面上测试获取单个用户信息的过程。

1. 点击接口 [/user/{username}] 获取单个用户信息。
2. 点击 `**Try it out** ` 进入测试传参页面。
3. 输入参数，点击 **Execute** 蓝色按钮执行调用。
4. 查看返回信息。

下面是测试时的响应截图。

![swagger 测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20191126001337387.png)

## 5. 常见报错

如果你在程序运行中经常发现像下面这样的报错。

```log
java.lang.NumberFormatException: For input string: ""
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65) ~[na:1.8.0_111]
	at java.lang.Long.parseLong(Long.java:601) ~[na:1.8.0_111]
	at java.lang.Long.valueOf(Long.java:803) ~[na:1.8.0_111]
	at io.swagger.models.parameters.AbstractSerializableParameter.getExample(AbstractSerializableParameter.java:412) ~[swagger-models-1.5.20.jar:1.5.20]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_111]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_111]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_111]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_111]
	at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:536) [jackson-databind-2.5.4.jar:2.5.4]
	at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:666) [jackson-databind-2.5.4.jar:2.5.4]
	at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:156) [jackson-databind-2.5.4.jar:2.5.4]
	at com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serializeContents(IndexedListSerializer.java:113) [jackson-databind-2.5.4.jar:2.5.4]
```

那么你需要检查使用了 `@ApiModelProperty` 注解且字段类型为数字类型的属性上，`@ApiModelProperty` 注解是否设置了 example 值，如果没有，那就需要设置一下，像下面这样。

```java
@NotNull(message = "用户 ID 不能为空")
@ApiModelProperty(value = "用户ID", required = true, example = "1000")
private Integer id;
```

文中代码都已经上传到 [ https://github.com/niumoo/springboot ](https://github.com/niumoo/springboot)

## 参考文档

- [OpenAPI Specification](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md)
- [Swagger Documentation](https://swagger.io/docs/specification/about/)
- [Springfox Reference Documentation](http://springfox.github.io/springfox/docs/current/)


**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)