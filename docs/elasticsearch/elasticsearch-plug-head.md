---
title: 全文搜索ElasticSearch（三）Head插件的安装与使用
date: 2018-10-12 19:14:17
url: lucene/elasticsearch-head
tags:
- Elasticsearch
- 插件
- Elasticsearch Head
categories:
- Elasticsearch
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。


![Chrome head安装](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/36fc267332d7fa3aeb1a49fb651c48bc.png)
### elasticsearch-head 介绍

`elasticsearch-head`是一个用于浏览器和与`elasticsearch`进行交互的Web前端程序。 `elasticsearch-head`是托管在github上的，可以自由的下载安装使用。

GitHub地址：https://github.com/mobz/elasticsearch-head
<!-- more -->
### elasticsearch-head 下载

head插件可以直接在github页面上点击`clone or download`进行下载然后解压，也可以使用git命令进行下载。  

`git clone git://github.com/mobz/elasticsearch-head.git`

### elasticsearch-head 启动

#### 方式1：使用nodejs启动

这种方式需要使用[nodejs](https://nodejs.org/en/download/)环境进行启动。

1. `git clone git://github.com/mobz/elasticsearch-head.git`
1. `cd elasticsearch-head`
1. `npm install`
1. `npm run start`
1. `open` <http://localhost:9100/>

#### 方式2：使用Tomcat启动

观察解压的`elasticsearch-head` 目录和文件，我们发现head插件只是一个前端页面，因此我们可以运行于任何web服务器，如`Nginx`，`Tomcat`等。因为笔者开发环境是`JDK`，所以使用`Tomcat`进行测试。
1. 解压Tomcat，进入webapps目录。
2. 拷贝解压后的head插件内容到`webapps`文件夹。
![image.png](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/7405907d8c23c444dc105dc6c73997a5.png) 
3. Tomcat启动。
4. `open` http://localhost:8080/elasticsearch-head/

这时候如果启动了`elasticsearch`，会发现head插件并不能连接到`elasticsearch`，打开浏览器控制台会发现由于跨域问题产生的错误日志。
![head跨域错误](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/5a54b9ec54e63d18f4fa3454a04ad548.png)

这时候我们需要配置`elasticsearch`允许跨域访问，打开`elasticsearch`的配置文件config/elasticsearch.yml在里面添加允许跨域配置。
```
# 跨域问题
http.cors.enabled: true
http.cors.allow-origin: "*"
```
再次启动`elasticsearch`，会发现`head`可以正常连接到ES了。

![head插件](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/4d718cf7b7bba9f61c67cd6f7958b8df.png)

#### 方式3：使用chrome扩展插件
此种方式安装的`head`插件，安装简单，没有跨域问题，但是需要可以上`外网`。

1. 打开chrome插件地址 [Elasticsearch Head](https://chrome.google.com/webstore/detail/elasticsearch-head/ffmkiejjmecolpfloofpjologoblkegm/)

2. 点击添加至Chrome
    ![Chrome head安装](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/36fc267332d7fa3aeb1a49fb651c48bc.png)
3. 等待安装完毕
4. 点击chrome扩展中的head图标运行
![Chrome-Head插件](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/4e8dec7a1910de042607676b3e475796.png)


**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)