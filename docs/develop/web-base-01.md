---
title: Web笔记（一） Web 简介与开发环境搭建
date: 2018-08-15 05:03:59
url: develop/web/web/base-01
updated: 2018-08-15 05:03:59
categories:
 - Java 开发
tags:
 - Tomcat
 - Java EE
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

### Web应用程序的工作原理
大多数的Web应用程序结构都是采用最为流行的B/S软件开发体系结构，将Web应用程序部署在Web服务器上，只要Web服务器启动，用户就可以通过客户端浏览器发送[HTTP](http://www.codingme.net/post/java-web-01)请求到Web服务器，此时运行在Web服务器上对应的Web应用程序将处理客户端请求，处理完成后做出响应。

Web应用程序工作原理图如下：

![Web应用程序工作原理](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/2a80764098b039c156a354df120d12f9.jpg)
<!-- more -->

### Web应用开发技术
Web应用开发技术又分为客户端开发技术，服务器开发技术。
客户端开发技术：

1. HTML
1. CSS
1. JavaScript
...

服务器开发技术：
1. PHP
1. ASP.NET
1. Servlet
1. JSP
...

两个概念：
`静态页面`：类似于HTML这种单纯的客户端页面，在每次访问的时候得到的信息都是相同的吗吗，和后台没有任何交互，它是实际存在的，无需经过服务器的编译，直接加载到客户浏览器上显示出来。我们称之为静态页面。

`动态页面`：相对静态页面，显示的内容可以随着时间、环境或者数据库操作的结果而发生改变的。我们称之为动态页面。

### Web 服务器
进行Java Web开发环境的搭建，首先我们需要了解下Web服务器。
WEB服务器也称为WWW(WORLD WIDE WEB)服务器，主要功能是提供网上信息浏览服务，Web服务器可以解析HTTP协议。当Web服务器接收到一个HTTP请求,会返回一个HTTP响应,例如送回一个HTML页面。为了处理一个请求Web服务器可以响应一个静态页面或图片，进行页面跳转或者把动态响应的产生委托给一些其它的程序例如CGI脚本，JSP脚本，servlets，ASP脚本，服务器端JavaScript，或者一些其它的服务器端技术。
关于HTTP协议详细信息可以查看[网络协议之HTTP](https://www.wdbyte.com/2018/07/computer/protocol-http/)

几种常见的Web服务器。
1. Resin
Resin是CAUCHO公司的产品，是一个非常流行的application server，对servlet和JSP提供了良好的支持，性能也比较优良，resin自身采用JAVA语言开发。
1. JBoss
是一个基于J2EE的开放源代码的应用服务器。 JBoss代码遵循LGPL许可，可以在任何商业应用中免费使用。JBoss是一个管理EJB的容器和服务器，支持EJB 1.1、EJB 2.0和EJB3的规范。但JBoss核心服务不包括支持servlet/JSP的WEB容器，一般与Tomcat或Jetty绑定使用。
1. WebSphere
WebSphere 是 IBM 的软件平台。它包含了编写、运行和监视全天候的工业强度的随需应变 Web 应用程序和跨平台、跨产品解决方案所需要的整个中间件基础设施，如服务器、服务和工具。WebSphere 提供了可靠、灵活和健壮的软件。
1. WebLogic
WebLogic是美国Oracle公司出品的一个application server，确切的说是一个基于JAVAEE架构的中间件，WebLogic是用于开发、集成、部署和管理大型分布式Web应用、网络应用和数据库应用的Java应用服务器。将Java的动态功能和Java Enterprise标准的安全性引入大型网络应用的开发、集成、部署和管理之中。
1. Tomcat
Tomcat是Apache 软件基金会（Apache Software Foundation）的Jakarta 项目中的一个核心项目，由Apache、Sun 和其他一些公司及个人共同开发而成。由于有了Sun 的参与和支持，最新的Servlet 和JSP 规范总是能在Tomcat 中得到体现，Tomcat 5支持最新的Servlet 2.4 和JSP 2.0 规范。因为Tomcat 技术先进、性能稳定，而且免费，因而深受Java 爱好者的喜爱并得到了部分软件开发商的认可，成为目前比较流行的Web 应用服务器。



### 安装与配置JDK

Java web开发首先我们需要配置web服务器，这样我们才能通过web服务器部署发布web项目，才可以进行访问，这里选择Tomcat作为web服务器，Tomcat基于运行基于Jre环境，因此我们在配置Tomcat 之前需要配置Java环境。

jdk的下载与安装这里不说，只顺便说一下win下环境变量的配置。

1. 配置环境变量 ：右键我的电脑  →  属性→  高级系统设置  → 环境变量
1. 在系统变量里新建JAVA_HOME 变量，值为JDK安装路径
1. 新建`classpath`变量，值为：
		.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar

1. 修改PATH变量值，在值后添加：
		%JAVA_HOME%\bin；%JAVA_HOME%\jre\bin

配置完毕后运行命令java -version有版本信息打印说明配置成功
```dos
Microsoft Windows [版本 10.0.15063]
(c) 2017 Microsoft Corporation。保留所有权利。

C:\Users\Niu>java -version
java version "1.8.0_111"
Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)

C:\Users\Niu>
```
### 安装与启动Tomcat
Tomcat是免费的开源软件，可在直接在官方网站下载。
[http://tomcat.apache.org/](http://tomcat.apache.org/)
1.可以直接在左侧选择版本：

![enter image description here](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/ae2daaf8a3849968ec40589cdcc54f4b.jpg)

2.选择版本后可以在下方进行下载，可以选择下载安装版或者解压版，这里选择了解压版
tar.gz文件是Linux操作系统下的安装版本
exe文件是Windows系统下的安装版本
zip文件是Windows系统下的压缩版本

![enter image description here](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/5479a7ba9d098838d3e0fee1d2f05877.jpg)

3.下载完成后解压缩，得到Tomcat目录，

![Tomca目录](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/869cfe4d5dc8a32e1e1b7f7723587a53.jpg)

4.可以在%Tomcat%\conf\server.xml中修改默认端口号(默认为8080)

```xml
<Connector URIEncoding="UTF-8" connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>
```
5.配置完毕后可以运行%Tomcat%\bin\startup.sh进行启动

![Tomcat启动](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/6eee55ca7cc387416c7f0bcc4fa245f9.jpg)

6.浏览器访问http://localhost:8080 进行测试

![Tomcat访问测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/8d9b6b2f556a6305d8d20a194aba1869.jpg)

#### Eclipse中使用Tomcat
点击菜单：Window → Prefences → Server → Runtime Environments
点击左边Add按钮，选择Apache，选择Tomcat版本，点击NEXT

![Eclipse添加Tomcat](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/00aba424b3e589bc32e556f2c2409566.jpg)


至此，Java Web集成开发环境配置完成，可以在Eclipse中Server面板中新建Tomcat Server进行启动。

新建Server:

![新建Server](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/0e5978714a480e71652775e9819e46db.jpg)

完成创建：

![完成server创建](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/c360b946bd1e0805f38fb3057683eb9e.jpg)

完成创建之后可以在新建的server上右键选择start进行启动，启动效果如同双击startup.bat，console控制台会同步显示启动信息。

### web项目开发

在环境搭建完成之后，我们应该使用这个环境进行Web项目的开发。这里使用一个简单的例子，来演示如何使用Eclipse开发并且部署一个Web项目。

#### 创建web项目
首先我们打开Eclipse，点击菜单File -> new -> project 新建项目

![新建项目here](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/aec15cfa216538721861005372b29b45.jpg)

选择Web Project项目进行创建

![创建项目](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1f3a7bd37a8abc15e8e85c1963d7f951.jpg)

填写项目名称等信息

![enter image description here](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/597f7b796de57cfae5b774f93a66709b.jpg)

点击两次Next之后，创建web.xm信息，完成项目创建

![创建web.xml](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/4741b851105cb98eeeba23234936bd11.jpg)

查看项目目录结构
![web项目目录结构](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/78f64a263de3943d92aa14e019673308.jpg)


为了实验效果，我们在WebContent下创建一个index.html文件，编写Hello web内容进行部署演示。
WebContent 上右键 NEW - >File - >输入文件名index.html完成文件创建。
编写内容：

```html
<!DOCTYPE html>
<html>
<head>
	<title>Web test</title>
</head>
<body>
	<h3>Hello web</h3>
</body>
</html>
```
#### 部署web项目
在eclipse中使用Tomcat进行启动项目
在已经创建完毕的Tomcat server上右键添加项目，然后进行启动即可

![在Eclipse使用Tomcat启动Web项目 here](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/a679a296bfc9d79297f11199958cf153.gif)

#### 访问web项目
可以看到项目启动在8080端口，启动完毕，此时可以通过浏览器进行项目访问。

http://localhost:8080/web-Test/index.html

![访问测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/57549829bfef115af813a93c64751e4c.jpg)

此时，在Eclipse中创建部署启动一个Web项目已经完成。

**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)