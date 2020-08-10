---
title: Web笔记（四）Servlet 程序开发
date: 2018-09-04 20:35:44
url: develop/web/web/base-04
updated: 2018-09-04 20:35:44
categories:
 - Java 开发
tags:
 - Servlet
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。


### Servlet 的编写
Servlet本质上是一个Java类，创建一个Servlet很简单，就是定义一个Java类，这个类继承自`javax.servlet.http.HttpServlet`类，覆盖其中的doGet和doPost方法，在doGet和doPost中编写处理请求的代码。
由于 Servlet 不是 Java 平台标准版的组成部分，所以必须添加jar包：`servlet-api.jar`
可以在Tomcat下lib目录下找到，也可自行下载添加。在创建Web项目的时候如果有选择Tomcat ，则会自动添加，如何创建一个web项目并部署到Tomcat，可以参考之前文章。
[Web笔记（一） Web 简介与开发环境搭建
](https://www.wdbyte.com/2018/08/develop/web/web-base-01/)
<!-- more -->

编写第一个Servlet ，我们可以直接在Eclipse 中的Web项目里新建Servelt。
![创建servlet](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/541e00d4849885d86f4f7795d3c4e1a8.png)    

也可以普通创建类手动继承HttpServlet。在创建完毕后需要手动重写doGet和doPost方法。
![创建servlet](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/ac0664ef704f0e97e5e657bf849605da.png)

使用第一种方法新建 FirstServlet.java 类（注意看代码注释）
```java
package net.codingme.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 第一个servlet测试
 * 
 */
//@WebServlet("/firstServlet")　
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FirstServlet() {
		super();
	}

	// get请求处理
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//设置响应的文本类型和字符编码
		response.setContentType("text/html;charset=UTF-8");
		//通过输出流向客户端做出响应
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<h3>firest servlet</h3>");
		out.println("</body></html>");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

```
HTTP的请求方式除了`get`和`post`请求之外还有put、delete等，get和post是最为常用两种请求方式，所以通常只覆盖doGet和doPost方法。  
`response.setContentType("text/html;charset=UTF-8");`用于设置响应的文本类型和编码方式，通过响应对象获得输出流对象out，用于向客户端浏览器输出响应内容，代码中输出的HTML标记，这实际上是一个动态的Web的页面。
关于HTTP协议的相关知识可以查看文章[网络协议之HTTP](https://www.wdbyte.com/2018/07/computer/protocol-http/)


### Servlet 的web.xml配置
Servlet编写完成之后，需要在工程`WEBROOT/WEB-INF/web.xml`中进行配置才可以生效，web.xml是Web应用的主配置文件，包含Web应用配置的主要信息。

在web.xml中根元素`<web-app>`中配置Servlet，代码如下：
```java
<servlet>
	<servlet-name>first</servlet-name>
	<servlet-class>net.codingme.servlet.FirstServlet</servlet-class>
</servlet>

<servlet-mapping>
	<servlet-name>first</servlet-name>
	<url-pattern>/firstServlet</url-pattern>
</servlet-mapping>
```
配置解释：
```java
<servlet>
	<servlet-name>servlet名</servlet-name>
	<servlet-class>servlet的class的全名</servlet-class>
</servlet>

<servlet-mapping>
	<servlet-name>servlet名</servlet-name>
	<url-pattern>servlet的访问路径</url-pattern>
</servlet-mapping>
```

### Servlet 3.0的注解配置

如果Servlet版本是`3.0`及以上的，可以使用`@WebServlet`注解进行配置，省去了web.xml中的繁琐配置。此方式和通过web.xml配置Servlet`二选一`即可。

**@WebServlet主要属性列表**

| 属性名|类型|描述|
| ------------ | ------------ | ------------ |
|  name |String   |  	指定 Servlet 的 name 属性，等价于 <servlet-name>。如果没有显式指定，则该 Servlet 的取值即为类的全限定名。 |
| value  | 	String[]  |  该属性等价于 urlPatterns 属性。两个属性不能同时使用。 |
| urlPatterns | 	String[]  | 	指定一组 Servlet 的 URL 匹配模式。等价于 <url-pattern> 标签。  |
| loadOnStartup  |  int | 	指定 Servlet 的加载顺序，等价于 <load-on-startup> 标签。  |
| initParams  |WebInitParam[]   | 指定一组 Servlet 初始化参数，等价于 <init-param> 标签。  |
|asyncSupported |boolean   | 声明 Servlet 是否支持异步操作模式，等价于 <async-supported> 标签。  |
|description |String   |  该 Servlet 的描述信息，等价于 <description> 标签。 |
| displayName |String  | 	该 Servlet 的显示名，通常配合工具使用，等价于 <display-name> 标签。  |

**@WebServlet完整的使用例子**
```java
@WebServlet(urlPatterns = {"/simple"}, asyncSupported = true, 
loadOnStartup = -1, name = "SimpleServlet", displayName = "ss", 
initParams = {@WebInitParam(name = "username", value = "tom")} 
) 

```

只配置访问路径的示例：
```java
package net.codingme.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 第一个servlet测试
 * 使用注解配置访问路径
 */
 @WebServlet(urlPatterns= {"/firstServlet"})
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FirstServlet() {
		super();
	}
	.
	.
	.
```

### Servlet 的运行与访问
Servlet配置完成之后，把Servlet所在工程项目部署到web服务器上并启动。
发布成功之后可以访问servlet配置的路径进行访问。  
格式如：`协议://服务器地址:端口号/WEB应用名/Servlet的访问路径`
那么web服务器是如何找到对应的servlet类的呢？
1. 查找web.xml中Servlet配置信息中的`<url-pattern>`值与请求路径相匹配的项。
1. 访问到对应的(`<servlet-mapping>`中`<servlet-name>`与`<servlet>`中`<servlet-name>`值相等，)`<servlet-class>`，可以访问到指定的Servlet类。
1. web服务器将在第一次访问servlet时实例化一个servlet对象用于处理请求。

访问Servlet有三种方式
 - 直接在浏览器地址中输入访问路径来访问
 - 通过超链接来访问
 - 通过提交表单来访问

访问测试：  
![访问测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1c8602c60269fa6d2531c00d96d2398c.jpg)


### Servlet 的初始化参数

自定义的Servlet继承了了HttpServlet，HttpServlet继承了GenericServlet，GenericServlet类实现了ServletConfig接口，所以自定义的Servlet中可以直接使用ServletConfig中的`getInitParameter(String name)`方法。也可直接通过getServletName()方法获得Servlet名字，也就是web.xml中相应的`<servlet>`元素的`<servlet-name>`的值。如果没有配置会返回Servlet类名。


#### 编写Servlet
```java
package net.codingme.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * 
 * Servlet获得初始化参数
 * 
 * @author Niu on 2017年7月25日 下午6:14:23
 */
public class GetInitParamServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//通过getInitParameter方法获得配置文件中设置的初始化值
		String name = this.getInitParameter("name");
		String password = this.getInitParameter("password");
		
		//通过getServletName方法获得配置文件中设置Servlet名字
		String servletName = this.getServletName();
		
		//设置响应文本类型和编码方式
		resp.setContentType("text/html;charset=UTF-8");
		
		//通过输出流向客户端做出相应
		PrintWriter out = resp.getWriter();
		out.println("name："+name);
		out.println("<br>");
		out.println("password："+password);
		out.println("<br>");
		out.println("servletName："+servletName);
		out.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
```


#### 配置Servlet
初始化了两个参数，分别是`name：xy`和`password：123`，访问路径/getInitParam

```xml
<!-- Servlet获取初始化参数值 -->
	<servlet>
		<servlet-name>getInitParam</servlet-name>
		<servlet-class>net.codingme.servlet.GetInitParamServlet</servlet-class>
		<!-- 初始化参数值 -->
		<init-param>
			<param-name>name</param-name>
			<param-value>xy</param-value>
		</init-param>
		<init-param>
			<param-name>password</param-name>
			<param-value>123</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>getInitParam</servlet-name>
		<url-pattern>/getInitParam</url-pattern>
	</servlet-mapping>
	
```


#### 测试Servlet
访问配置的路径：http://localhost:8080/servlet-GetInitParam/getInitParam
![访问测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/bfad3c21f6258b6bbdfd1d960ca72e74.png)
获取到配置的初始化参数。

**GitHub**：[获取初始化参数](https://github.com/niumoo/webcore/tree/master/servlet-GetInitParam)

### Servlet 的开发总结
Servlet开发步骤：
1. 编写Servlet，编写一个Java类，继承HttpServlet类并覆盖doGet和doPost方法
2. 在配置文件web.xml中配置Servlet（Servlet3.0以上版本可以使用注解）
3. 将Servlet所在Java Web项目部署到Web服务器上，例如Tomcat
4. 启动Web服务器
5. 请求访问Servlet

Servlet执行流程：
1. 客户端浏览器向Web服务器发送请求访问某一个Servlet
1. Web服务器根据配置信息定位到具体的Servlet
3. 如果这个Servlet是第一次被访问，此时Servlet对象在内存中不存在，则创建这个Servlet对象，如果这个Servlet已经被访问过，则Servlet的对象已经存在内存中，然后创建一个线程操作这个Servlet对象，完成具体功能。
4. 获得运行结果，通过响应对象（response）设置响应参数并将结果返回到客户端。
5. 客户端将相应结果显示在浏览器中。
	

GitHub：[第一个Servlet](https://github.com/niumoo/webcore/tree/master/servlet-First)

**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)