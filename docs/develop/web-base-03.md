---
title: Web笔记（三）Servlet 的类与接口API
date: 2018-09-01 09:58:35
url: develop/web/web/base-03
updated: 2018-09-01 09:58:35
categories:
- Java EE
tags:
 - Servlet
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

### Servlet 介绍
Java Servlet 是运行在 Web 服务器或应用服务器上的程序，它是作为来自 Web 浏览器或其他 HTTP 客户端的请求和 HTTP 服务器上的数据库或应用程序之间的中间层。
使用 Servlet，您可以收集来自网页表单的用户输入，呈现来自数据库或者其他源的记录，还可以动态创建网页。
Java Servlet 通常情况下与使用 CGI（Common Gateway Interface，公共网关接口）实现的程序可以达到异曲同工的效果。但是相比于 CGI，Servlet 有以下几点优势：
 - 性能明显更好。
 - Servlet 在 Web 服务器的地址空间内执行。这样它就没有必要再创建一个单独的进程来处理每个客户端请求。
 - Servlet 是独立于平台的，因为它们是用 Java 编写的。
 - 服务器上的 Java 安全管理器执行了一系列限制，以保护服务器计算机上的资源。因此，Servlet 是可信的。
 - Java 类库的全部功能对 Servlet 来说都是可用的。它可以通过 sockets 和 RMI 机制与 applets、数据库或其他软件进行交互。
 <!-- more -->

### Servlet API
Servlet API 包含两个包，分别是:javax.servlet 与javax.servlet.http包。具体的类与接口结构如下图：

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/68c057bb4390b74f4ebb980719345e2a.jpg)

Servlet 的运行需要Web 服务器的支持，Web服务器可以通过调用Servlet对象提提供的标准API ，对客户端请求做出处理。
[官方API](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/)

#### Servlet 接口
  定义所有 servlet 都必须实现的方法。
servlet 是运行在 Web 服务器中的小型 Java 程序。servlet 通常通过 HTTP（超文本传输协议）接收和响应来自 Web 客户端的请求。
要实现此接口，可以编写一个扩展 javax.servlet.GenericServlet 的一般 servlet，或者编写一个扩展 javax.servlet.http.HttpServlet 的 HTTP servlet。
此接口定义了初始化 servlet 的方法、为请求提供服务的方法和从服务器移除 servlet 的方法。这些方法称为生命周期方法，它们是按以下顺序调用的：

1. 构造 servlet，然后使用 init 方法将其初始化。
2. 处理来自客户端的对 service 方法的所有调用。
3. 从服务中取出 servlet，然后使用 destroy 方法销毁它，最后进行垃圾回收并终止它。

除了生命周期方法之外，此接口还提供了 getServletConfig 方法和 getServletInfo 方法，servlet 可使用前一种方法获得任何启动信息，而后一种方法允许 servlet 返回有关其自身的基本信息，比如作者、版本和版权。

Servlet 接口中定义的方法如下：

```java
//当Servlet将要卸载时由Servlet引擎调用，用于是释放资源 
void  destroy()  

//由 servlet 容器调用，指示将该 servlet 放入服务。servlet 容器
//仅在实例化 servlet 之后调用 init 方法一次。在 servlet 可以接
//收任何请求之前，init 方法必须成功完成。
void  init(ServletConfig config) throws ServletException

//由 servlet 容器调用，以允许 servlet 响应某个请求。此方法仅在 
//servlet 的 init() 方法成功完成之后调用。  
void  service(ServletRequest req, ServletResponse res) throws ServletException, java.io.IOException 

//返回 ServletConfig 对象，该对象包含此 servlet 的初始化和启动
//参数。返回的 ServletConfig 对象是传递给 init 方法的对象。此接
//口的实现负责存储 ServletConfig 对象，以便此方法可以返回该对象。
//实现此接口的 GenericServlet 类已经这样做了。
ServletConfig  getServletConfig()

// 返回有关 servlet 的信息，比如作者、版本和版权。。
String	getServletInfo()

```

####  ServletConfig 接口
由Servlet接口中的Init方法我们可以发现，init方法在做初始化工作的时候会传入ServletConfig类型的参数进行初始化工作， Servlet 容器使用的 ServletConfig配置对象，该对象在初始化期间将信息传递给 Servlet。

ServletConfig 接口中定义的方法如下：

```java
//返回包含指定初始化参数的值的 String，如果参数不存在，则返回 null。
String getInitParameter(String name) 

//返回一个存储所有初始化变量的枚举函数，如果Servlet没有初始化变量，
//返回一个空的枚举函数 
java.util.Enumeration<E> getInitParameterNames() 

//返回一个ServletConfig 对象的引用 
ServletContext getServletContext() 

//返回此 servlet 实例的名称。该名称可能是通过服务器管理提供的
//在 Web 应用程序部署描述符中分配，或者对于未注册（和未命名）
//的 servlet 实例，该名称将是该 servlet 的类名称。
String getServletName() 

```

####  GenericServlet 接口
  定义一般的、与协议无关的 Servlet。要编写用于 Web 上的 HTTP Servlet，请改为扩展 javax.servlet.http.HttpServlet。GenericServlet 实现 Servlet 和 ServletConfig 接口。**实现了Servlet 接口中除service（）方法之外的所有抽象方法，但是是默认实现。**Servlet可以直接扩展 GenericServlet，GenericServlet 使编写 Servlet变得更容易。它提供生命周期方法 init 和 destroy 的简单版本，以及 ServletConfig 接口中的方法的简单版本。GenericServlet 还实现 log 方法，在 ServletContext 接口中对此进行了声明。

要编写一般的 servlet，只需重写抽象 service 方法即可。在开发中很少使用，不再说明。
#### HttpServlet 接口
HttpServlet 类继承GenericServlet 类，是一个抽象类。在Java Web 开发中自定义Servlet 通常都是直接集成HttpServlet 类，此类实现了Servlet 接口中的service(ServletRequest request,ServletResponse response) 方法，用来处理客户端请求，根据request中的getMethod方法来调用不同的处理方法，例如，get方法，则调用doGet方法进行处理，也因此HttpServlet定义了处理不同请求的不同方法。

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/7ae09229e2b45fc552e9b2ab3b30d31e.jpg)


#### ServletRequest 和 ServletResponse 接口
  定义将客户端请求信息提供给某个 servlet 的对象。servlet 容器创建 ServletRequest 对象，并将该对象作为参数传递给该 servlet 的 service 方法。
ServletRequest 对象提供包括参数名称、参数值、属性和输入流的数据。扩展 ServletRequest 的接口可提供其他特定于协议的数据，例如 javax.servlet.http.HttpServletRequest 提供的 HTTP 数据。

ServletRequest中有很多有用的方法:
```java
//返回指定servlet上下文（web应用）的URL的前缀。 
String getContextPath()  

//返回请求URI上下文后的子串 
String getServletPath() 

//返回指定的HTTP头标指。如果其由请求给出，则名字应为大小写不敏感。 
String getHeader(String name) 

//返回具有指定名字的请求属性，如果不存在则返回null。属性可由servlet
//引擎设置或使用setAttribute()显式加入。
Object getAttribute(String name) 

//以指定名称保存请求中指定对象的引用。 
void setAttribute(String name,Object obj) 

//返回指定输入参数，如果不存在，返回null。 
String getParameter(String name) 

//返回请求所用的字符编码。 
String getCharacteEncoding()

```
具体可以参考官方API
[ServletRequest API](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/ServletRequest.html)

ServletResponse 中有很多有用的方法：
```java
//返回响应使用字符解码的名字。除非显式设置，否则为ISO-8859-1 
String getCharacterEncoding() 

//返回用于将返回的二进制输出写入客户端的流，此方法和getWrite()
//方法二者只能调用其一。 
OutputStream getOutputStream()throws IOException 

//返回用于将返回的文本输出写入客户端的一个字符写入器，此方法和
//getOutputStream()二者只能调用其一。 
Writer getWriter()throws IOException

//设置内容类型。在HTTP servlet中即设置Content-Type头标。 
void setContentType(String type) 
```
具体可以参考官方API
[ServletResponse API](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/ServletResponse.html)

####  HttpServletRequest 和 HttpServletResponse 接口
HttpServletRequest 是专用于HTTP的ServletRequest 子接口，客户端浏览器的请求被封装成一个HttpServletRequest 对象，请求信息请求的地址，参数，数据，上传的文件，IP地址，甚至操作系统信息都包含在内。
主要处理：

1. 读取和写入HTTP头标 
2. 取得和设置cookies 
3. 取得路径信息 
4. 标识HTTP会话。 

HttpServletRequest 常用方法：
```java
//返回与请求相关cookie的一个数组。
Cookie[] getCookies()  

//返回HTTP请求方法（例如GET、POST等等） 
String getMethod() 

 //返回客户端的会话ID 
String getRequestedSessionId()

 //调用getSession(true)的简化版。 
HttpSession getSession()

//返回当前HTTP会话，如果不存在，则创建一个新的会话，create参数为true。
HttpSession getSession(boolean create) 

//返回URL中一部分，从“/”开始，包括上下文，但不包括任意查询字符串。 
String getRequestURI() 

//返回查询字符串，即URL中?后面的部份。 
String getQueryString() 

//返回指定servlet上下文（web应用）的URL的前缀。 
String getContextPath() 
```
HttpServletResponse 则继承了ServletResponse 接口。都提供了与HTTP有关的方法，主要是对HTTP状态码和Cookie的管理。
```java
//将一个Set-Cookie头标加入到响应。
void addCookie(Cookie cookie) 

//设置响应状态码为指定值（可选的状态信息）
void sendError(int status)

//重定向到某一个Web组件
void sendRedirect(String location) 
```

#### ServletContext 接口
一个ServletContext 接口对象表示一个Web应用程序的上下文，运行在Java虚拟机中每个Web应用程序都有一个与之香瓜你的Servlet上下文，Sevetl API提供了一个ServletContext 接口老表示上下文，它提供了一些方法可以让Servlet 通过这些方法与Servlet  容器进行通信。
常用方法:
```java
//将对象绑定到此 servlet 上下文中的给定属性名称。若存在，则替换之
void setAttribute(String name, Object object)

//返回Servlet 上下文中名字为name的对象，名字是name的对象是全局对象
//因此可以被同一个Servlet 在任意时候访问，或者上下文中任意其他的Servlet访问
Object getAttribute(String name) 

// 返回包含指定上下文范围初始化参数值的 String，如果参数不存在，则返回 null
String getInitParameter(String name) 
```
[ServletContext 接口官方API](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/ServletContext.html)

#### HttpSession 接口
  提供一种方式，跨多个页面请求或对 Web 站点的多次访问标识用户并存储有关该用户的信息。
HttpSession 接口用于记录HTTP客户端和HTTP会话之间的关联，且可以在多个连接和请求中持续一段时间，Session则可以让无状态的HTTP请求在多个请求时识别用户记录状态。

此接口允许 servlet
查看和操作有关某个会话的信息，比如会话标识符、创建时间和最后一次访问时间
将对象绑定到会话，允许跨多个用户连接保留用户信息。

常用方法：
```java

//使此会话无效，然后取消对任何绑定到它的对象的绑定。
void invalidate()  

//从此会话中移除与指定名称绑定在一起的对象。如果会话没有与
//指定名称绑定在一起的对象，则此方法不执行任何操作。
void removeAttribute(String name)

//使用指定名称将对象绑定到此会话。如果具有同样名称的对象已
//经绑定到该会话，则替换该对象。
 void setAttribute(String name, Object value)
 
//返回与此会话中的指定名称绑定在一起的对象，如果没有对象绑
//定在该名称下，则返回 null。
 Object getAttribute(String name)
 
//返回创建此会话的时间，该时间是用自格林威治标准时间 1970
//年 1 月 1 日午夜起经过的毫秒数来测量的。
long getCreationTime() 

//返回包含分配给此会话的唯一标识符的字符串。
String getId() 

//超时时间以秒为单位。负数则永不超时。
void setMaxInactiveInterval(int interval) 

//返回 servlet 容器在客户端访问之间将使此会话保持打开状态
//的最大时间间隔，以秒为单位。
int getMaxInactiveInterval()
```


### 附录：HttpServletRequest的请求信息获取

```java
/**
 * HttpServletRequest请求参数获取测试
 * @author niumoo 
 */
@WebServlet("/dispense")
public class DispenseServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException {
		// 获取请求方式：GET
		System.out.println("req.getMethod():" + req.getMethod());

		// 获取项目名称：/dispense
		System.out.println("req.getContextPath():" + req.getContextPath());

		// 获取完整请求路径：http://localhost:8888/dispense/dispense
		System.out.println("req.getRequestURL():" + req.getRequestURL());

		// 获取除了域名外的请求数据：/dispense/dispense
		System.out.println("req.getRequestURI():" + req.getRequestURI());

		// 获取请求参数：name=codingme.net
		System.out.println("req.getQueryString():" + req.getQueryString());
		// 获取请求头：
		System.out.println("req.getHeader(\"user-Agent\"):" + req.getHeader("user-Agent"));

		System.out.println("-------------------------------------------------");
		// 获取所有的消息头名称
		Enumeration<String> headerNames = req.getHeaderNames();
		// 获取获取的消息头名称，获取对应的值，并输出
		while (headerNames.hasMoreElements()) {
			String nextElement = headerNames.nextElement();
			System.out.println(nextElement + ":" + req.getHeader(nextElement));
		}

		System.out.println("---------------------------------------------------");
		// 根据名称获取此重名的所有数据
		System.out.println("req.getHeader(\"accept\"):" + req.getHeader("accept"));

		// 获取请求主机名
		System.out.println("req.getHeader(\"host\"):" + req.getHeader("host"));

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException {
		doGet(req, resp);
	}
}

```
请求：http://localhost:8888/dispense/dispense?name=codingme.net
输出：

```
req.getMethod():GET
req.getContextPath():/dispense
req.getRequestURL():http://localhost:8888/dispense/dispense
req.getRequestURI():/dispense/dispense
req.getQueryString():name=codingme.net
req.getHeader("user-Agent"):Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3080.5 Safari/537.36
--------------------------------------------------------
host:localhost:8888
connection:keep-alive
cache-control:max-age=0
upgrade-insecure-requests:1
user-agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3080.5 Safari/537.36
accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
accept-encoding:gzip, deflate, br
accept-language:zh-CN,zh;q=0.8
cookie:_ga=GA1.1.1003706294.1499565784; Hm_lvt_57ccbd5c600ed4e6bdb9458e666b6409=1499849256,1499853602,1499950574; Hm_lvt_1b51c3ea9a3e7b1a2bc55df97ab4efd3=1499952403
----------------------------------------------------------
req.getHeader("accept"):text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
req.getHeader("host"):localhost:8888
```

### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)