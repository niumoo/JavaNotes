---
title: Web笔记（五）Servlet 的生命周期
date: 2018-09-06 20:35:44
url: develop/web/web/base-05
updated: 2018-09-06 20:35:44
categories:
- Java EE
tags:
 - Servlet
 - Java EE
---
Servlet的声明周期是由servlet的容器（web服务器）来控制的，通过简单的概括可以分为4步：  

Servlet类加载 → 实例化Servlet → Servlet提供服务 → 销毁Servlet。  

1. Serlvet 类加载，该阶段仅仅执行一次。  
1. 实例化Servlet ，该阶段仅执行一次、  
1. Servlet 调用 service() 方法来处理客户端的请求，客户端请求一次执行一次，具体的执行次数取决于客户端的请求次数。    
1. Servlet 通过调用 destroy() 方法销毁Servlet，只在web服务器停止服务时执行一次。  
<!-- more -->

### 开发
通过一个简单的例子来演示Servlet 的生命周期，编写 LiftServlet.java。
```java
package net.codingme.servlet;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Desc：Servlet生命周期测试
 * 
 * @author niumoo on 2017年7月24日 上午11:04:40
 */
//@WebServlet("/LiftServlet")
public class LiftServlet extends HttpServlet {

	// 不能被覆盖，这里是web服务器的servlet初始化
	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("Web服务器初始化Servlet : init(ServletConfi confit)");
		super.init(config);
	}

	// 可以添加特定的初始化代码
	@Override
	public void init() throws ServletException {
		System.out.println("初始化init()");
		super.init();
	}

	// 不能被覆盖
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		System.out.println("调用服务：service(ServletRequest arg0, ServletResponse arg1)");
		super.service(arg0, arg1);
	}

	// service会检查请求类型，用来判断调用什么方法，不要覆盖
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		System.out.println("调用服务：service(HttpServletRequest arg0, HttpServletResponse arg1)");
		super.service(arg0, arg1);
	}

	/**
	 * get方法
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GET方法:doGet()");
		resp.getWriter().println("servlet get");
	}

	// 当web服务器正常停止时调用次销毁方法
	@Override
	public void destroy() {
		System.out.println("销毁：destroy()");
		super.destroy();
	}
}
```


### 配置
配置web.xml
```xml
<!-- Servlet生命周期 -->
<servlet>
	<servlet-name>liftServlet</servlet-name>
	<servlet-class>net.codingme.servlet.LiftServlet</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>liftServlet</servlet-name>
	<url-pattern>/liftServlet</url-pattern>
</servlet-mapping>
```

代码中配置了LiftServlet 的访问路径为/liftServlet。

### 访问

访问路径：http://localhost:8080/servlet-LifeCycle/liftServlet

访问两次链接 ，观察控制台输出：

```
信息: Starting ProtocolHandler ["http-nio-8080"]
八月 06, 2017 11:32:09 上午 org.apache.coyote.AbstractProtocol start
信息: Starting ProtocolHandler ["ajp-nio-8009"]
八月 06, 2017 11:32:09 上午 org.apache.catalina.startup.Catalina start
信息: Server startup in 2705 ms
Web服务器初始化Servlet : init(ServletConfi confit)
初始化init()
调用服务：service(ServletRequest arg0, ServletResponse arg1)
调用服务：service(HttpServletRequest arg0, HttpServletResponse arg1)
GET方法:doGet()
调用服务：service(ServletRequest arg0, ServletResponse arg1)
调用服务：service(HttpServletRequest arg0, HttpServletResponse arg1)
GET方法:doGet()
八月 06, 2017 11:32:42 上午 org.apache.catalina.core.StandardServer await
信息: A valid shutdown command was received via the shutdown port. Stopping the Server instance.
八月 06, 2017 11:32:42 上午 org.apache.coyote.AbstractProtocol pause
信息: Pausing ProtocolHandler ["http-nio-8080"]
八月 06, 2017 11:32:42 上午 org.apache.coyote.AbstractProtocol pause
信息: Pausing ProtocolHandler ["ajp-nio-8009"]
八月 06, 2017 11:32:43 上午 org.apache.catalina.core.StandardService stopInternal
信息: Stopping service [Catalina]
八月 06, 2017 11:32:43 上午 org.apache.catalina.core.ApplicationContext log
信息: SessionListener: contextDestroyed()
八月 06, 2017 11:32:43 上午 org.apache.catalina.core.ApplicationContext log
信息: ContextListener: contextDestroyed()
销毁：destroy()
八月 06, 2017 11:32:43 上午 org.apache.coyote.AbstractProtocol stop
信息: Stopping ProtocolHandler ["http-nio-8080"]
八月 06, 2017 11:32:43 上午 org.apache.coyote.AbstractProtocol stop
信息: Stopping ProtocolHandler ["ajp-nio-8009"]
八月 06, 2017 11:32:43 上午 org.apache.coyote.AbstractProtocol destroy
信息: Destroying ProtocolHandler ["http-nio-8080"]
八月 06, 2017 11:32:43 上午 org.apache.coyote.AbstractProtocol destroy
信息: Destroying ProtocolHandler ["ajp-nio-8009"]
```

### 总结

1. **servlet初始化**：运行一次，调用`init(ServletConfig config)`创建ServletConfig对象。调用`init()` 创建servlet对象并将两者关联。可以将代码放入init()方法完成特定的初始化工作。

1. **servlet服务**：每次请求运行， 接收到请求通过`service(ServletRequest arg0, ServletResponse arg1)`方法进行处理，然后把请求对象封装成HttpServletRequest，响应对象封装成HttpServletResponse调用`service(HttpServletRequest arg0, HttpServletResponse arg1)`处理。在这个方法里会根据请求方式调用doGet或者doPost方法进行处理。

1. **servlet销毁**：运行一次，销毁Servlet  对象，销毁与之关联的ServletConfig对象，

可以看到，在第一次访问时调用初始化代码，再次访问只调用服务代码，正常停止Tomcat，调用销毁代码。

**GitHub**：[Servlet 生命周期](https://github.com/niumoo/webcore/tree/master/servlet-LifeCycle)