5---
title: 可以Postman也可以cURL.进来领略下cURL的独门绝技
date: 2020-06-01 08:00:00
url: tool/curl
categories:
- 工具
- Linux
tags:
- curl
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![curl logo](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/curl-logo.svg)



[**cURL**](https://curl.haxx.se/) 是一个开源免费项目，主要是命令行工具 **cURL** 和 libcurl，**cURL** 可以处理任何网络传输协议，但是不涉及任何具体的**数据处理**。

**cURL** 支持的通信协议非常丰富，如 DICT，FILE，FTP，FTPS，GOPHER，HTTP，HTTPS，IMAP，IMAPS，LDAP，LDAPS，MQTT，POP3，POP3S，RTMP， RTMPS，RTSP，SCP，SFTP，SMB，SMBS，SMTP，SMTPS，TELNET 以及 TFTP。查看 cURL 源代码可以访问官方 [Github](https://github.com/curl/curl)。

如果安装 **cURL** 呢？

ubuntu / Debian.

```shell
sudo apt install curl
```

CentOS / Fedora.

```shell
sudo yum install curl
```

Windows.

如果你已经安装了 Git，那么 Git Bash 自带 **cURL** . 如果作为开发者你 git 都没有，那么只能官方[手动下载](https://curl.haxx.se/download.html)。

### 1. 请求源码

直接 curl 。

```shell
$ curl http://wttr.in/
```

上面请求的示例网址是一个天气网站，很有意思，会根据你的请求 ip 信息返回你所在位置的天气情况。

![curl wttr.in](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/image-20200530175034329.png)

写这篇文字时我所在的上海正在下雨，窗外飘雨无休无止。

### 2. 文件下载

使用 `-o` 保存文件，类似于 wget 命令，比如下载 README 文本保存为 readme.txt 文件。如果你需要自定义文件名，可以使用 `-O`自定使用 url 中的文件名。

```shell
$ curl -o readme.txt https://mirrors.nju.edu.cn/kali/README
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   159  100   159    0     0   1939      0 --:--:-- --:--:-- --:--:--  1939
```

下载文件会显示下载状态，如数据量大小、传输速度、剩余时间等。可以使用 `-s` 参数禁用进度表。

```shell
$ curl -o readme.txt https://mirrors.nju.edu.cn/kali/README
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   159  100   159    0     0   1939      0 --:--:-- --:--:-- --:--:--  1939
$ 
$ curl -o readme.txt https://mirrors.nju.edu.cn/kali/README -s
```
也可以使用 `--process-bar` 参数让进度表显示为进度条。
```shell
$ curl -o readme.txt https://mirrors.nju.edu.cn/kali/README --progress-bar
########################################################################################## 100.0%
```

**cURL** 作为强大的代名词，断点续传自然手到擒来，使用 `-C -` 参数即可。下面是断点续传下载 ubuntu20.04 镜像的例子。

```shell
$ curl -O https://mirrors.nju.edu.cn/ubuntu-releases/20.04/ubuntu-20.04-desktop-amd64.iso --progress-bar
##                                                                                               1.7%
^C
$ curl -C - -O https://mirrors.nju.edu.cn/ubuntu-releases/20.04/ubuntu-20.04-desktop-amd64.iso --progress-bar
###                                                                                              2.4%
^C
$ curl -C - -O https://mirrors.nju.edu.cn/ubuntu-releases/20.04/ubuntu-20.04-desktop-amd64.iso --progress-bar
###                                                                                               2.7%
^C
$ 
```

什么？下载时不想占用太多网速？使用 `--limit-rate` 限个速吧。

```shell
curl -C - -O https://mirrors.nju.edu.cn/ubuntu-releases/20.04/ubuntu-20.04-desktop-amd64.iso --limit-rate 100k
```

什么？你又要从 FTP 服务器下载文件了？不慌。

```shell
curl -u user:password -O ftp://ftp_server/path/to/file/
```

### 3.  Response Headers

使用 `-i` 参数显示 Response Headers 信息。使用 `-I` 可以只显示 Response Headers 信息。

```shell
$ curl -I http://wttr.in
HTTP/1.1 200 OK
Server: nginx/1.10.3
Date: Sat, 30 May 2020 09:57:03 GMT
Content-Type: text/plain; charset=utf-8
Content-Length: 8678
Connection: keep-alive
Access-Control-Allow-Origin: *
```

### 4. 请求方式(GET/POST/...)

使用 `-X` 轻松更改请求方式。

```shell
$ curl -X GET http://wttr.in
$ curl -X POST http://wttr.in
$ curl -X PUT http://wttr.in
...
```

### 5. 请求参数

以传入参数 `name`  值为 `未读代码` 为例。

Get 方式参数直接url拼接参数。

```shell
$ curl -X GET http://wttr.in?name=未读代码
```

Post 方式使用 `--data` 设置参数。

```shell
$ curl -X POST --data "name=未读代码" http://wttr.in
```

请求时也可以自定义 **header** 参数，使用 `--harder` 添加。

```shell
$ curl --header "Content-Type:application/json" http://wttr.in
```

### 6. 文件上传

**cURL** 的强大远不止此，表单提交，上传文件内容也不在话下，只需要使用 `-F`  或者 `-D`参数，`-F` 会自动加上请求头 `Content-Type: multipart/form-data` ，而 `-D` 则是 `Content-Type : application/x-www-form-urlencoded`.

比如上传一个 protrait.jpg 图片。

```shell
$ curl -F profile=@portrait.jpg https://example.com/upload
```

提交一个具有 name 和 age 参数的 form 表单。

```shell
curl -F name=Darcy -F age=18 https://example.com/upload
```

参数对应的内容也可以从文件中读取。

```shell
curl -F "content=<达西的身世.txt" https://example.com/upload
```

上传时同时指定内容类型。

```shell
curl -F "content=<达西的身世.txt;type=text/html" https://example.com/upload
```

上传文件的和其他参数一起。

```shell
curl -F 'file=@"localfile";filename="nameinpost"' example.com/upload
```

### 7. 网址通配

**cURL** 可以实现多个网址的匹配，你可以使用 `{}` 结合逗号分割来标识使用 url 中的某一段，也可以使用 `[]` 来表示范围参数。

```shell
# 请求 www.baidu.com 和  pan.baidu.com 和 fanyi.baidu.com
$ curl http://{www,pan,fanyi}.baidu.com
# 虚构网址1-10开头的baidu.com，然后请求
$ curl http://[1-10].baidu.com
# 虚构网址a-z开头的baidu.com，然后请求
$ curl http://[a-z].baidu.com
```

这种方式有时候还是很有用处的，比如说你发现了某个网站的 url 规律。

### 8. 使用 cookie

请求时使用 `-c` 参数存储响应的 cookie，使用 `-b` 可以在请求时带上指定 cookie.

```shell
$ curl -c wdbyte_cookies http://www.wdbyte.com
$ curl -b wdbyte_cookes http://www.wdbyte.com
```

### 总结

以上就是 **cURL** 的常见用法了，最后告诉你一个小技巧，Chrome、Firefox 等浏览器可以直接拷贝请求为 cURL 语句。保存之后下次请求测试非常方便。

![Chrome 复制 cURL 请求](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/image-20200531151839505.png)


**参考资料**

1. https://curl.haxx.se/docs/manpage.html

**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)
