---
title: Web笔记（二）Tomcat 使用总结
date: 2018-08-23 09:12:31
url: develop/web/web/base-02
updated: 2018-08-23 09:12:31
categories:
- Java EE
tags:
- Tomcat
- Java EE
---

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/84e22fa4d78bc3eb8ed5109a0beb21f8.jpg)
###  Tomcat 介绍
Tomcat是由Apache软件基金会下属的Jakarta项目开发的一个Servlet容器，按照Sun Microsystems提供的技术规范，实现了对Servlet和JavaServer Page（JSP）的支持，并提供了作为Web服务器的一些特有功能，如Tomcat管理和控制平台、安全域管理和Tomcat阀等。由于Tomcat本身也内含了一个HTTP服务器，它也可以被视作一个单独的Web服务器。但是，不能将Tomcat和Apache HTTP服务器混淆，Apache HTTP服务器是一个用C语言实现的HTTPWeb服务器；这两个HTTP web server不是捆绑在一起的。Apache Tomcat包含了一个配置管理工具，也可以通过编辑XML格式的配置文件来进行配置。（摘录自[Wiki](https://zh.wikipedia.org/wiki/Apache_Tomcat)）([Apache Tomcat](http://tomcat.apache.org/))
<!-- more -->

### Tomcat 安装
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

### Tomcat 的默认端口

Tomcat 修改HTTP默认端口，可以直接修改Tomcat 目录下\conf \server.xml文件。默认端口号为8080，修改为想要的端口，重启Tomcat即可。
```xml
<Connector URIEncoding="UTF-8" connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>
```
若要修改成8081端口：
```xml
<Connector URIEncoding="UTF-8" connectionTimeout="20000" port="8081" protocol="HTTP/1.1" redirectPort="8443"/>
```
### Tomcat 的虚拟目录配置
什么是虚拟目录呢，简单的说，我们可以根据请求的路径不同，来发布不同的项目。如此形式：我们想要
在访问http://www.codingme.net/testA  时，进入A项目。
在访问http://www.codingme.net/testB  时，进入B项目。
这个时候我们就需要配置虚拟目录来完成这个操作。
此时的URL：http://www.codingme.net/testA  不是单纯的路径，而是协议域名端口号+WEB应用testA。
#### 自动映射虚拟目录
在Tomcat 默认情况下，我们可以看到 %Tomca%\conf \server.xml文件最底部有配置如下：

```xml
      <Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true">
        <!-- SingleSignOn valve, share authentication between web applications
             Documentation at: /docs/config/valve.html -->
        <!--
        <Valve className="org.apache.catalina.authenticator.SingleSignOn" />
        -->
        <!-- Access log processes all example.
             Documentation at: /docs/config/valve.html
             Note: The pattern used is equivalent to using pattern="common" -->
        <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
               prefix="localhost_access_log" suffix=".txt"
               pattern="%h %l %u %t "%r" %s %b" />
      </Host>
```
这里Tomcat 默认配置了虚拟主机localhost，基础应用目录webapps，也就是%Tomca%\webapps\ 目录，Tomcat 服务器会自动管理webapps目录下的所有web应用，并把它映射成虚似目录。
因此在需要配置虚拟目录时我们可以直接把项目复制到webapps 目录下进行发布
示例：
1. 复制项目到webapps下

![复制项目到webapps](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/73a4fd3da0339281189dd33b0b417eba.jpg)
2. 查看test/ index.html 文件内容
    ![Index内容](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/f9501721ef675ded41268e0080ef05bc.jpg)
3. 运行%Tomcat%/bin/startup.bat 启动Tomcat
4. 进行访问测试http://localhost:8080/test/index.html

![访问测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/b6b0c70169fe8297bec33d9f51980a72.jpg)

注意：http://localhost:8080 会默认访问 webapps/ROOT文件夹中的内容。

#### 修改server.xml 映射虚拟目录

我们也可以通过在server.xml 文件中的host 元素之间添加配置代码来配置虚拟目录：添加代码如下;

```xml
Context path="/app" docBase="D:/app" debug="0" reloadable="true" crossContext="true"/>
```

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/6a0ecaf86e878e286eb489d0b5b673d2.jpg)

配置中我们配置了访问路径为/app  ，项目路径D:/app 因此我们只要把要发布的内容放入D:/app 文件夹中即可。
 配置完毕后 运行%Tomcat%/bin/startup.bat 启动Tomcat，可以通过
> http://localhost:8080/app/index.html

访问到D:/app/index.html，需要多个虚拟目录可以直接配置多条配置。

注意：若想使用http://localhost:8080 访问项目则需要配置 `path=""`
注意：配置中的path值和docBase中的文件夹名称没有任何关系。

#### 在conf /Catalina /localhost 增加xml文件映射虚拟目录

博主比较推荐这一种。
在%Tomcat%/conf/Catalina/localhost目录中，增加配置文件来配置虚拟目录。
配置文件名称格式为：虚拟目录路径.xml
举个栗子：
在%Tomcat%/conf/Catalina/localhost目录中增加文件  `blog.xml`
写入内容：

```xml
<?xml version="1.0" encoding="utf-8"?>
 <Context
	 docBase="d:/blog"
	 reloadable="true">
</Context>
```
![Tomcat-localhost](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1a56a5580ae151d1b7201e11fd04bed8.jpg)

D盘下blog文件夹中内容：

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/ab3ac58378336d818a7386d18e887eab.jpg)

`docBase="d:/blog" ` 指定了Web应用存放位置为D盘下blog文件夹中。
`reloadable="true" `表示当blog文件夹中文件有变化中，自动加载。
配置完毕启动Tomcat后就可以通过访问到blog文件夹中的内容。

> http://localhost:8080/blog

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/5ca799c38a020dbd5ae476420537d3bc.jpg)

注意：需要多个虚拟目录可以多建立几个配置文件，文件名不能相同。
注意：若想使用http://localhost:8080 访问项目则需要配置文件名为`ROOT.xml`。
注意：文件名blog.xml 和配置中d:/blog 没有关系，文件名可以写成其他，如blog111.xml，那么在访问时就要访问http://localhost:8080/blog111

### Tomcat 的虚拟主机

首先我们先了解虚拟主机的用处，一个虚拟主机也就是一个网站。比如我们`只有一个服务器`，一个服务器上只有一个80端口，我们需要发布两个web项目，那么我们可以使用虚拟目录，把两个项目发布到两个不同的路径之下，但是如果我们有两个不同的域名需要把两个项目对应两个域名，我们就需要配置虚拟主机了。

示例：两个域名
一个是`www.aaaa.com`
一个是 `www.bbbb.com`

为了实验效果，我们配置C:\Windows\System32\drivers\etc\hosts 文件，添加两行映射。
```
127.0.0.1	www.aaaa.com
127.0.0.1	www.bbbb.com
```
此时我们本机可以使用 localhost / www.aaaa.com / www.bbbb.com 进行访问。

配置虚拟主机指定www.aaaa.com访问是的内容。
1：在server.xml中Engine元素中添加一个host子元素
```xml
<Host name="www.aaaa.com" debug="0" appBase="d:/aaaa" unpackWARs="true"
autoDeploy="true" xmlValidation="false" xmlNamespaceAware="false">
	<Logger className="org.apache.catalina.logger.FileLogger" directory="logs"
	prefix="aaaa_log." suffix=".txt" timestamp="true" />
</Host>
```
其中的
1. name表示在访问的域名是www.aaaa.com时会使用此配置。
1. appBase="d:/aaaa"  指定了项目的发布路径。

启动Tomcat后，此时在使用www.aaaa.com进行访问时候，会默认显示d:/aaaa/ROOT中的内容。

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/c437877b962ffd9aac1f3beb2b9c074c.gif)

如果存在 d:/aaaa/test，则可以通过http://www.aaaa.com:8080/test  进行访问。  
此时test也就是www.aaaa.com的虚拟目录。
添加www.bbbb.com 访问同上。

### Tomcat 的单例多实例配置
参考之前文章
[Linux配置Tomcat的单机多实例](https://www.wdbyte.com/2018/08/develop/install-tomcat-many-instance/)