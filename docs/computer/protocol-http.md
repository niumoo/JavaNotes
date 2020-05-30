---
title: 网络协议之HTTP
date: 2018-07-14 00:37:19
updated: 2018-07-14 00:37:19
url: computer/protocol-http
categories:
- 计算机
tags:
- 协议
- HTTP
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![HTTP](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/http-to-https-Image-1024x576.png)   

### HTTP的简介

超文本传输协议（HTTP，HyperText Transfer Protocol)是互联网上应用最为广泛的一种网络协议。所有的WWW文件都必须遵守这个标准。

<!-- more -->

 ![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/5008f0a9cb84c78ffc040ef0464d10df.jpg) 

HTTP是一个基于TCP/IP通信协议来传递数据（HTML 文件, 图片文件, 查询结果等）。

HTTP是一个属于应用层的面向对象的协议，由于其简捷、快速的方式，适用于分布式超媒体信息系统。它于1990年提出，经过几年的使用与发展，得到不断地完善和扩展。目前在WWW中使用的是HTTP/1.0的第六版，HTTP/1.1的规范化工作正在进行之中，而且HTTP-NG(Next Generation of HTTP)的建议已经提出。

HTTP协议工作于客户端-服务端架构为上。浏览器作为HTTP客户端通过URL向HTTP服务端即WEB服务器发送所有请求。Web服务器根据接收到的请求后，向客户端发送响应信息。

### HTTP的特点
1. 支持客户/服务器模式。
1. 简单快速：客户向服务器请求服务时，只需传送请求方法和路径。请求方法常用的有GET、HEAD、POST。每种方法规定了客户与服务器联系的类型不同。
由于HTTP协议简单，使得HTTP服务器的程序规模小，因而通信速度很快。
1. 灵活：HTTP允许传输任意类型的数据对象。正在传输的类型由Content-Type加以标记。
1. 无连接：无连接的含义是限制每次连接只处理一个请求。服务器处理完客户的请求，并收到客户的应答后，即断开连接。采用这种方式可以节省传输时间。
1. 无状态：HTTP协议是无状态协议。无状态是指协议对于事务处理没有记忆能力。缺少状态意味着如果后续处理需要前面的信息，则它必须重传，这样可能导致每次连接传送的数据量增大。另一方面，在服务器不需要先前信息时它的应答就较快。

### HTTP的URL(UniformResourceLocator)
URL格式：
 >　http://host[:port][abs_path]

`http`表示要通过HTTP协议来定位网络资源。
`host`表示合法的Internet主机域名或IP地址（以点分十进制格式表示）。
`port`用于指定一个端口号，拥有被请求资源的服务器主机监听该端口的TCP连接。
如果port是空，则使用`缺省的端口80`。当服务器的端口不是80的时候，需要显式指定端口号。
`abs_path`指定请求资源的URI(Uniform Resource Identifier，统一资源定位符)，如果URL中没有给出abs_path，那么当它作为请求URI时，必须以“/”的形式给出。通常这个工作浏览器就帮我们完成了。

### HTTP的请求消息Request
消息格式：
 ![HTTP 消息格式](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/ae3fe2acba5a78462d247d03cccaa3ec.jpg) 

先来看下HTTP的请求消息头：
 - Accept: text/html,image/* 【浏览器告诉服务器，它支持的数据类型】
 - Accept-Charset: ISO-8859-1 【浏览器告诉服务器，它支持哪种字符集】
 - Accept-Encoding: gzip,compress 【浏览器告诉服务器，它支持的压缩格式】
 - Accept-Language: en-us,zh-cn 【浏览器告诉服务器，它的语言环境】
 - Host: www.codingme.net:80【浏览器告诉服务器，它的想访问哪台主机】
 - If-Modified-Since: Tue, 11 Jul 2000 18:23:51 GMT【浏览器告诉服务器，缓存数据的时间】
 - Referer: http://www.codingme.net/index.jsp【浏览器告诉服务器，客户机是从那个页面来的—反盗链】
 - 8.User-Agent: Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0)【浏览器告诉服务器，浏览器的内核是什么】
 - Cookie【浏览器告诉服务器，带来的Cookie是什么】
 - Connection: close/Keep-Alive 【浏览器告诉服务器，请求完后是断开链接还是保持链接】
 - Date: Tue, 11 Jul 2000 18:23:51 GMT【浏览器告诉服务器，请求的时间

使用Chrome请求网址进行观察：
 ![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/6e442fc98cc8988ef631f7103f213b0b.jpg) 
Chrome请求信息

```
Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Accept-Encoding:gzip, deflate
Accept-Language:zh-CN,zh;q=0.8
Connection:keep-alive
Cookie:JSESSIONID=2A40A51EDDE663C840A2D03B7587D660; Hm_lvt_1b51c3ea9a3e7b1a2bc55df97ab4efd3=1500964170,1500976171,1500994669,1501056813; Hm_lpvt_1b51c3ea9a3e7b1a2bc55df97ab4efd3=1501056816
Host:blog.codingme.net
Upgrade-Insecure-Requests:1
User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3080.5 Safari/537.36
```
### HTTP的响应消息Response
一个完整的HTTP响应应该包含四个部分：

1. 一个状态行【用于描述服务器对请求的处理结果。】
1. 多个消息头【用于描述服务器的基本信息，以及数据的描述，服务器通过这些数据的描述信息，可以通知客户端如何处理等一会儿它回送的数据】
1. 一个空行
1. 实体内容【服务器向客户端回送的数据】

响应消息头的详细解释：

 - Location: http://www.codingme.net/index.jsp 【服务器告诉浏览器要跳转到哪个页面】
 - Server:apache tomcat【服务器告诉浏览器，服务器的型号是什么】
 - Content-Encoding: gzip 【服务器告诉浏览器数据压缩的格式】
 - Content-Length: 80 【服务器告诉浏览器回送数据的长度】
 - Content-Language: zh-cn 【服务器告诉浏览器，服务器的语言环境】
 - Content-Type: text/html; charset=GB2312 【服务器告诉浏览器，回送数据的类型】
 - Last-Modified: Tue, 11 Jul 2000 18:23:51 GMT【服务器告诉浏览器该资源上次更新时间】
 - Refresh: 1;url=http://www.codingme.net【服务器告诉浏览器要定时刷新】
 - Content-Disposition: attachment; filename=aaa.zip【服务器告诉浏览器以下载方式打开数据】
 - Transfer-Encoding: chunked 【服务器告诉浏览器数据以分块方式回送】
 - Set-Cookie:SS=Q0=5Lb_nQ; path=/search【服务器告诉浏览器要保存Cookie】
 - Expires: -1【服务器告诉浏览器不要设置缓存】
 - Cache-Control: no-cache 【服务器告诉浏览器不要设置缓存】
 - Pragma: no-cache 【服务器告诉浏览器不要设置缓存】
 - Connection: close/Keep-Alive 【服务器告诉浏览器连接方式】
 - Date: Tue, 11 Jul 2000 18:23:51 GMT【服务器告诉浏览器回送数据的时间】

Chrome请求网址http://bing.codingme.net  抓包展示;
 ![Chrome 请求信息](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/09766111948104bd7ac918219cf7eedf.jpg) 


### HTTP的状态码
状态代码有三位数字组成，第一个数字定义了响应的类别，共分五种类别:

1xx：指示信息--表示请求已接收，继续处理

2xx：成功--表示请求已被成功接收、理解、接受

3xx：重定向--要完成请求必须进行更进一步的操作

4xx：客户端错误--请求有语法错误或请求无法实现

5xx：服务器端错误--服务器未能实现合法的请求

常见状态码：

- 200 OK                        //客户端请求成功
- 400 Bad Request               //客户端请求有语法错误，不能被服务器所理解
- 401 Unauthorized              //请求未经授权，这个状态代码必须和WWW-Authenticate报头域一起使用 
- 403 Forbidden                 //服务器收到请求，但是拒绝提供服务
- 404 Not Found                 //请求资源不存在，eg：输入了错误的URL
- 500 Internal Server Error     //服务器发生不可预期的错误
- 503 Server Unavailable        //服务器当前不能处理客户端的请求，一段时间后可能恢复正常

详情可以查看[更多状态码](http://kb.cnblogs.com/page/168720/)


### HTTP的请求方法（动作）
HTTP协议中定义了8种方法来表明不同的动作
1. OPTIONS
    返回服务器针对特定资源所支持的HTTP请求方法，也可以利用向web服务器发送‘*’的请求来测试服务器的功能性。

1. HEAD
    向服务器索与GET请求相一致的响应，只不过响应体将不会被返回。这一方法可以再不必传输整个响应内容的情况下，就可以获取包含在响应小消息头中的元信息。

1. GET
    向特定的资源发出请求。注意：GET方法不应当被用于产生“副作用”的操作中，例如在Web Application中，其中一个原因是GET可能会被网络蜘蛛等随意访问。
    Loadrunner中对应get请求函数：web_link和web_url.

1. POST
    向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST请求可能会导致新的资源的建立和/或已有资源的修改。 Loadrunner中对应POST请求函数：web_submit_data,web_submit_form.

1. PUT
    向指定资源位置上传其最新内容。

1. DELETE
    请求服务器删除Request-URL所标识的资源。

1. TRACE
    回显服务器收到的请求，主要用于测试或诊断。

1. CONNECT
HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器。

### TCP的三次握手
HTTP使用TCP进行输出传输，在建立TCP连接时会进行三次握手。

所谓三次握手（Three-Way Handshake）即建立TCP连接，就是指建立一个TCP连接时，需要客户端和服务端总共发送3个包以确认连接的建立。在socket编程中，这一过程由客户端执行connect来触发，整个流程如下图所示：

 ![TCP 三次握手](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/7026e1d3c06ff8407683f0447073e406.jpg) 

1. 建立连接时，客户端发送SYN包（SYN=i）到服务器，并进入到SYN-SEND状态，等待服务器确认
1. 服务器收到SYN包，必须确认客户的SYN（ack=i+1）,同时自己也发送一个SYN包（SYN=k）,即SYN+ACK包，此时服务器进入SYN-RECV状态
1. 客户端收到服务器的SYN+ACK包，向服务器发送确认报ACK（ack=k+1）,此包发送完毕，客户端和服务器进入ESTABLISHED状态，完成三次握手，客户端与服务器开始传送数据。

### HTTP协议工作流程
以访问网站为例，在回车之后所发生的动作：

1. 浏览器向 DNS 服务器请求解析该 URL 中的域名所对应的 IP 地址;
1. 解析出 IP 地址后，根据该 IP 地址和默认端口 80，和服务器建立TCP连接;
1. 浏览器发出读取文件(URL 中域名后面部分对应的文件)的HTTP 请求，该请求报文作为 TCP 三次握手的第三个报文的数据发送给服务器;
1. 服务器对浏览器请求作出响应，并把对应的 html 文本发送给浏览器;
1. 释放 TCP连接;
1. 浏览器将该 html 文本并显示内容; 　

### GET请求和POST请求的区别
使用Chrome浏览器进行GET请求测试：
>Request URL:http://localhost:8888/01-web_servlet/loginServlet?username=zxy&password=123
Request Method:GET
Status Code:200 
Remote Address:[::1]:8888
Referrer Policy:no-referrer-when-downgrade

使用Chrome浏览器进行POST请求测试：
>Request URL:http://localhost:8888/01-web_servlet/loginServlet
Request Method:POST
Status Code:200 
Remote Address:[::1]:8888
Referrer Policy:no-referrer-when-downgrade

由测试可以看到GET和POST最明显的区别就是
1. GET携带的参数传递置于URL中以?分割URL和传输数据，多个参数用&连接，如果数据是英文字母/数字，原样发送，如果是空格，转换为+，如果是中文/其他字符，则直接把字符串用BASE64加密，`不安全`，
POST把提交的数据放置在是HTTP包的包体中。因此，GET提交的数据会在地址栏中显示出来，而`POST`提交，地址栏`不会改变`
1. 网上搜索得到如下区别
1. GET提交的数据大小有限制（因为浏览器对URL的长度有限制），而POST方法提交的数据没有限制.
1. GET方式需要使用Request.QueryString来取得变量的值，而POST方式通过Request.Form来获取变量的值。
1. GET方式提交数据，会带来安全问题，比如一个登录页面，通过GET方式提交数据时，用户名和密码将出现在URL上，如果页面可以被缓存或者其他人可以访问这台机器，就可以从历史记录获得该用户的账号和密码.
1. GET产生一个TCP数据包;POST产生两个TCP数据包。



**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)

