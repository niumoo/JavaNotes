---
title: Springboot 系列（六）web 开发之拦截器和三大组件
toc_number: false
date: 2019-02-21 08:32:01
url: springboot/springboot-06-web-filter-apo-webbase
tags:
 - Springboot
categories:
 - Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

## 1. 拦截器
![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/e678416c36eaf0969de279423d0e1d16.png)Springboot 中的 Interceptor 拦截器也就是 mvc 中的拦截器，只是省去了 xml 配置部分。并没有本质的不同，都是通过实现 HandlerInterceptor 中几个方法实现。几个方法的作用一一如下。
1. **preHandle**
   进入 Habdler 方法之前执行，一般用于身份认证授权等。
2. **postHandle**
   进入 Handler 方法之后返回 modelAndView 之前执行，一般用于塞入公共模型数据等。
3. **afterCompletion**
   最后处理，一般用于日志收集，统一后续处理等。
<!-- more -->

### 1.1 引入依赖

```xml
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

```

### 1.2 编写拦截器

```java
package net.codingme.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 拦截器
 *
 * @Author niujinpeng
 * @Date 2019/1/6 16:54
 */
@Slf4j
public class LogHandlerInterceptor implements HandlerInterceptor {

    /**
     * 请求方法执行之前
     * 返回true则通过
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        StringBuffer requestURL = request.getRequestURL();
        log.info("preHandle请求URL：" + requestURL.toString());
        return true;
    }

    /**
     * 返回modelAndView之前执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("postHandle返回modelAndView之前");
    }

    /**
     * 执行Handler完成执行此方法
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("afterCompletion执行完请求方法完全返回之后");
    }
}
```

### 1.3 配置拦截器

省去了 XML 中的拦截器配置部分后，使用 springboot 推荐的方式配置自定义拦截器。

```java
package net.codingme.boot.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 1.使用FastJSON
 * 2.配置时间格式化
 * 3.解决中文乱码
 * 4.添加自定义拦截器
 *
 * @Author niujinpeng
 * @Date 2018/12/13 15:35
 */
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

    /**
     * 添加自定义拦截器
     * .addPathPatterns("/**")  拦截的请求路径
     * .excludePathPatterns("/user"); 排除的请求路径
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user");
    }
}
```

## 2 切面编程

1. AOP：面向切面（方面）编程，扩展功能不修改源代码实现
2. AOP采取横向抽取机制，取代了传统纵向继承体系重复性代码
3. AOP底层使用动态代理实现
   - 有接口情况使用动态代理创建接口实现类代理对象
   - 没有接口情况使用动态代理创建类的子类代理对象

```java
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * <p>
 * 使用AOP记录访问日志
 * 使用@Before在切入点开始处切入内容
 * 使用@After在切入点结尾处切入内容
 * 使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
 * 使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
 * 使用@AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑
 * <p>
 * 注解：
 * Aspect:AOP
 * Component：Bean
 * Slf4j：可以直接使用log输出日志
 * Order：多个AOP切同一个方法时的优先级，越小优先级越高越大。
 * 在切入点前的操作，按order的值由小到大执行
 * 在切入点后的操作，按order的值由大到小执行
 *
 * @Author niujinpeng
 * @Date 2019/1/4 23:29
 */

@Aspect
@Component
@Slf4j
@Order(1)
public class LogAspect {
    /**
     * 线程存放信息
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 定义切入点
     * 第一个*：标识所有返回类型
     * 字母路径：包路径
     * 两个点..：当前包以及子包
     * 第二个*：所有的类
     * 第三个*：所有的方法
     * 最后的两个点：所有类型的参数
     */
    @Pointcut("execution(public * net.codingme.boot.controller..*.*(..))")
    public void webLog() {
    }

    /**
     * 在切入点开始处切入内容
     *
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 记录请求时间
        startTime.set(System.currentTimeMillis());
        // 获取请求域
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        // 记录请求内容
        log.info("Aspect-URL: " + request.getRequestURI().toLowerCase());
        log.info("Aspect-HTTP_METHOD: " + request.getMethod());
        log.info("Aspect-IP: " + request.getRemoteAddr());
        log.info("Aspect-REQUEST_METHOD: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("Aspect-Args: " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 在切入点之后处理内容
     */
    @After("webLog()")
    public void doAfter() {

    }

    /**
     * 在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        log.info("Aspect-Response: " + ret);
        Long endTime = System.currentTimeMillis();
        log.info("Aspect-SpeedTime: " + (endTime - startTime.get()) + "ms");
    }

}

```

访问查看拦截器和 AOP 的日志输出。

```log
09:57:15.408  INFO 2836 --- [nio-8080-exec-1] n.c.boot.config.LogHandlerInterceptor    : preHandle请求URL：http://localhost:8080/
09:57:15.413  INFO 2836 --- [nio-8080-exec-1] net.codingme.boot.config.LogAspect       : Aspect-URL: /
09:57:15.413  INFO 2836 --- [nio-8080-exec-1] net.codingme.boot.config.LogAspect       : Aspect-HTTP_METHOD: GET
09:57:15.413  INFO 2836 --- [nio-8080-exec-1] net.codingme.boot.config.LogAspect       : Aspect-IP: 0:0:0:0:0:0:0:1
09:57:15.414  INFO 2836 --- [nio-8080-exec-1] net.codingme.boot.config.LogAspect       : Aspect-REQUEST_METHOD: net.codingme.boot.controller.HelloController.index
09:57:15.415  INFO 2836 --- [nio-8080-exec-1] net.codingme.boot.config.LogAspect       : Aspect-Args: []
09:57:15.424  INFO 2836 --- [nio-8080-exec-1] net.codingme.boot.config.LogAspect       : Aspect-Response: Greetings from Spring Boot!SpringBoot是一个spring应用程序
09:57:15.425  INFO 2836 --- [nio-8080-exec-1] net.codingme.boot.config.LogAspect       : Aspect-SpeedTime: 12ms
09:57:15.436  INFO 2836 --- [nio-8080-exec-1] n.c.boot.config.LogHandlerInterceptor    : postHandle返回modelAndView之前
09:57:15.437  INFO 2836 --- [nio-8080-exec-1] n.c.boot.config.LogHandlerInterceptor    : afterCompletion执行完请求方法完全返回之后
```

## 3. Servlet,Filter,Listener
Servlet, Filter, Listener 是 Java web 的核心内容，那么在 Springboot 中如何使用呢？
### 3.1 编写 Servlet

```java
package net.codingme.boot.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * @WebServlet(urlPatterns = "/myservlet") // 定义访问路径
 * @Author niujinpeng
 * @Date 2019/1/24 16:25
 */
@Slf4j
@WebServlet(urlPatterns = "/myservlet")
public class MyServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        log.info("Servlet 开始初始化");
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Servlet 开始处理 GET 方法");
        PrintWriter writer = resp.getWriter();
        writer.println("Hello Servlet");
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void destroy() {
        log.info("Servlet 开始销毁");
        super.destroy();
    }
}
```

### 3.2 编写 Filter

```java
package net.codingme.boot.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * <p>
 *
 * @Author niujinpeng
 * @Date 2019/1/24 16:35
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class MyFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("拦截器开始拦截");
        filterChain.doFilter(request, response);
    }

}

```

### 3.3 编写 Listener

```java
package net.codingme.boot.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * <p>
 *
 * @Author niujinpeng
 * @Date 2019/1/24 16:45
 */
@Slf4j
@WebListener
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("监听器开始初始化");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("监听器开始销毁");
    }
}

```

### 3.4 添加到容器

添加到容器有两种方式，第一种使用注解扫描。

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ServletComponentScan 扫描Servlet,Filter,Listener 添加到容器
 */
@SpringBootApplication
@ServletComponentScan
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

}
```

或者使用配置类想容器中添加。

```java
/**
 * <p>
 * 在这里注册Servlet Filter Listener 或者使用 @ServletComponentScan
 *
 * @Author niujinpeng
 * @Date 2019/1/24 16:30
 */
@Configuration
public class WebCoreConfig {

    @Bean
    public ServletRegistrationBean myServlet() {
        return new ServletRegistrationBean<>(new MyServlet());
    }

    @Bean
    public FilterRegistrationBean myFitler() {
        return new FilterRegistrationBean<>(new MyFilter());
    }

    @Bean
    public ServletListenerRegistrationBean myListener() {
        return new ServletListenerRegistrationBean(new MyListener());
    }
    
}
```

启动可以在控制台看到监听器启动。

```log
 11:35:03.744  INFO 8616 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1364 ms
 11:35:03.798  INFO 8616 --- [           main] net.codingme.boot.listener.MyListener    : 监听器开始初始化
 11:35:03.892  INFO 8616 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
 11:35:04.055  INFO 8616 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
```

访问 Servlet 可以看到拦截器和 Servlet 生效。

```log
 11:36:55.552  INFO 3760 --- [nio-8080-exec-1] net.codingme.boot.servlet.MyServlet      : Servlet 开始初始化
 11:36:55.556  INFO 3760 --- [nio-8080-exec-1] net.codingme.boot.filter.MyFilter        : 拦截器开始拦截
 11:36:55.556  INFO 3760 --- [nio-8080-exec-1] net.codingme.boot.servlet.MyServlet      : Servlet 开始处理 GET 方法
```

文章代码已经上传到 GitHub [Spring Boot Web开发 - 拦截处理](https://github.com/niumoo/springboot/tree/master/springboot-web-interceptor)。
文章代码已经上传到 GitHub [Spring Boot Web开发 - Servlet,Filter,Listener](https://github.com/niumoo/springboot/tree/master/springboot-web-servlet-filter-listener)。

### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)