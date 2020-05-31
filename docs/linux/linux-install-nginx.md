---
title: 在CentOS7下Nginx的简单使用
date: 2018-07-10 23:05:10
url: linux/install-nginx
tags:
 - Nginx
categories:
 - Linux
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

<!-- ![Nginx Logo](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/c4f4c4d1ee971b2faa3ee4d504837a18.jpg)-->
![Nginx Logo](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/060ce880b9ad8d2688139d38ce01b355.jpg)
### Nginx
Nginx是一个异步框架的 Web服务器，也可以用作反向代理，负载平衡器 和 HTTP缓存。该软件由 Igor Sysoev 创建，并于2004年首次公开发布。 同名公司成立于2011年，以提供支持。

安装
```shell
yum -y install nginx
```
启动
```shell
// 启动使用start 检查是否启动使用status 停止stop
service nginx start
```
<!-- more -->
测试
输入IP地址进行访问，出现下图说明已经成功
![nginx访问](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/cd56f7d9e0b1df3d6bf1e9e668d09cf9.jpg)

### Nginx简单配置 - 发布web目录
nginx 的配置文件位置在/etc/nginx/nginx.conf ，在修改之前建议进行备份！
```shell
// 备份配置文件
cp nginx.conf nginx.conf.bak
// 编辑Nginx配置文件
vim /etc/nginx/nginx.conf
// 在配置文件的http{}中添加如下配置
server {
		# 端口号
		listen       80;
		# 域名
		server_name  codingme.net;
		# web目录
		root /webroot;
		# 默认首页
		index index.html
		error_page   500 502 503 504  /50x.html;
		location / {
		}
    }
```
### Nginx简单配置 - 反向代理

什么是反向代理呢？服务器根据客户端的请求，从一组或多组后端服务器（如Web服务器）上获取资源，然后再将这些资源返回给客户端，客户端只会得知反向代理的IP地址，而不知道在代理服务器后面的服务器簇的存在。
一图胜千言：
![反向代理](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/ed853d7219f3414a1676b0e411e40018.png)
使用反向代理有如下功能，引用维基百科：

> 对客户端隐藏服务器（簇）的IP地址
> 安全：作为应用层防火墙，为网站提供对基于Web的攻击行为（例如DoS/DDoS）的防护，更容易排查恶意软件等
> 为后端服务器（簇）统一提供加密和SSL加速（如SSL终端代理）
> 负载均衡，若服务器簇中有负荷较高者，反向代理通过URL重写，根据连线请求从负荷较低者获取与所需相同的资源或备援
> 对于静态内容及短时间内有大量访问请求的动态内容提供缓存服务
> 对一些内容进行压缩，以节约带宽或为网络带宽不佳的网络提供服务
> 减速上传
> 为在私有网络下（如局域网）的服务器簇提供NAT穿透及外网发布服务
> 提供HTTP访问认证[2]
> 突破互联网封锁（不常用，因为反向代理与客户端之间的连线不一定是加密连线，非加密连线仍有遭内容审查进而遭封禁的风险；此外面对针对域名的关键字过滤、DNS缓存污染/投毒攻击乃至深度数据包检测也无能为力）

```shell
// 编辑Nginx配置文件
vim /etc/nginx/nginx.conf
// 在配置文件的http{}中添加如下配置
 server {
        listen       80;
        server_name  codingme.net;
        location / {
			# Tomcat访问路径
            proxy_pass http://127.0.0.1:8080/;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }        
    }  
```
### Nginx简单配置 - HTTPS
在Google对HTPPS的推动下，越来越多的网站开始启用HTTPS协议，以应对网络安全问题。

关于HTTPS的概念，推荐一篇文章：[HTTPS 的故事](https://qianduan.group/posts/5a6560b00cf6b624d2239c6f)

```shell
// 编辑Nginx配置文件
vim /etc/nginx/nginx.conf
// 在配置文件的http{}中添加如下配置
server {
		listen   80;
		server_name codingme.net;
		# HTTP请求跳转至HTTPS
        rewrite ^(.*) https://$host$1 permanent;
    }	
server {
        listen     443;
        server_name codingme.net;
        ssl on;
		# HTTPS证书位置
        ssl_certificate /webroot/xxx.crt; 
		# 密钥位置
        ssl_certificate_key /webroot/xxx.key; 
        ssl_session_timeout 5m;
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2; 
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
        ssl_prefer_server_ciphers on;
		# 网站目录
        root /webroot;
		error_page 500 502 503 504 /50x.html;
		location / {

	 }
    }
```

**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)