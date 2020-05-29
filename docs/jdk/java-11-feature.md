---
title: 最通俗易懂的 Java 11 新特性讲解
date: 2020-03-01 08:01:01
url: jdk/jdk11-feature
tags:
 - Java11
categories:
 - Java 新特性
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![图片来自网络，作者：manotang](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/Oxz5l3JUFaHrCNm.jpg)

大多数开发者还是沉浸在 `Java 8` 中，而 `Java 14` 将要在 2020 年 3 月 17 日发布了，而我还在写着 `Java 11` 的新特性。`Java 11` 是 `Java 8` 之后的第一个 LTS 版本，但是也自从 `Java 11` 开始， Oracle JDK 不再可以免费的用于商业用途，当然如果你是个人使用，或者是使用 Open JDK ，那么还是可以免费使用的。

> 有些人很关心 `Java 11` 是否收费，Oracle 表示除非你在生产中使用，否则可以不用收费。
>
> 即使收费，免费的 Open JDK 不也很香吗。

可免费用于生产环境的 Open JDK 官网：[https://jdk.java.net/11/](https://jdk.java.net/11/)

再 6 个月后，`Java 15` 都要来了，这种发布节奏不仅让人有点应接不暇，更有点眼花缭乱。但是不管怎么说，发展的趋势不可逆，所以补习一波 `Java 11` 也是很有必要的。

<!-- more-->

## 1.  String API

字符串绝对是 Java 中最常用的一个类了，String 类的方法使用率也都非常的高，在 `Java 11` 中又为 String 类带来了一系列的好用操作。

1. isBlank() 判空。

   ```java
   // 判空，blank里我放入了全角空格，半角空格，TAB
   String blank = "　　  ";
   System.out.println(blank.isBlank());
   
   // 输出
   // true
   ```

2. lines() 分割获取字符串流。

   ```java
   // lines 返回一个 Stream
   String line = "a\nb\nc";
   Stream<String> lines = line.lines();
   // 使用 lambda 遍历
   lines.forEach(System.out::println);
   
   // 输出
   // a
   // b
   // c
   ```

3. repeat() 复制字符串

   ```java
   // 复制字符串
   String repeat = "我的微信:wn8398,";
   String repeat3 = repeat.repeat(3);
   System.out.println(repeat3);
   
   // 输出
   // 我的微信:wn8398,我的微信:wn8398,我的微信:wn8398,
   ```

4. strip() 去除前后空白字符。

   ```java
   // 去除前后空白
   String strip = "   　 https://www.wdbyte.com 　";
   System.out.println("==" + strip.trim() + "==");
   // 去除前后空白字符，如全角空格，TAB
   System.out.println("==" + strip.strip() + "==");
   // 去前面空白字符，如全角空格，TAB
   System.out.println("==" + strip.stripLeading() + "==");
   // 去后面空白字符，如全角空格，TAB
   System.out.println("==" + strip.stripTrailing() + "==");
   
   // 输出
   // ==　 https://www.wdbyte.com 　==
   // ==https://www.wdbyte.com==
   // ==https://www.wdbyte.com 　==
   // ==   　 https://www.wdbyte.com==
   ```

   这里注意，`trim` 只能去除半角空格，而 `strip` 是**去除各种空白符**。

## 2. File API

读写文件变得更加方便。

```
// 创建临时文件
Path path = Files.writeString(Files.createTempFile("test", ".txt"), "https://www.wdbyte.com");
System.out.println(path);
// 读取文件
// String ss = Files.readString(Path.of("file.json"));
String s = Files.readString(path);
System.out.println(s);

// 结果
// https://www.wdbyte.com
```

## 3. JEP 321 - HTTP Client

在 `Java 11` 中 Http Client API 得到了标准化的支持。且支持 HTTP/1.1 和 HTTP/2 ，也支持 websockets。

你可以像这样发起一个请求。

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://www.hao123.com"))
        .build();
// 异步
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(System.out::println)
        .join();

// 同步
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println(response.body());
```

更多的如同步异步请求，并发访问，设置代理等方式，可以参考 OpenJDK 官方文档。

[http://openjdk.java.net/groups/net/httpclient/recipes-incubating.html](http://openjdk.java.net/groups/net/httpclient/recipes-incubating.html)

你现在还需要各种 HTTP Client 依赖包吗？

## 4. JEP 323 - Lambda 局部变量推断

在 `Java 10` 中引入了 `var` 语法，可以自动推断变量类型。在 `Java 11` 中这个语法糖可以在 Lambda 表达式中使用了。

```java
var hashMap = new HashMap<String, Object>();
hashMap.put("wechat", "wn8398");
hashMap.put("website", "https://www.wdbyte.com");
hashMap.forEach((var k, var v) -> {
    System.out.println(k + ": " + v);
});
```

这里需要注意的是，`(var k,var v)` 中，k 和 v  的类型要么都用 var ，要么都不写，要么都写正确的变量类型。而不能 var 和其他变量类型混用。

![Lambda 中 var 不能混用](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/Lgjh2n6qAr34lK8.jpg)

## 5. JEP 330 - 单命令运行 Java

自从学习 Java 的第一天，我们就知道运行一个 Java 文件，要先用 `javac` 命令编译，再用 `java` 命令运行，而现在只要一个 `java` 命令就可以运行了。

```shell
$ cat Main.java

public class Main {

    public static void main(String[] args) {
        System.out.println("wechat:wn8398");
    }
}

$ java Main.java
wechat:wn8398
```

## 6. 免费的飞行记录器

商业版 JDK 中一直有一款低开销的事件信息收集工具，也就是飞行记录器（Java Flight Recorder），它可以对 JVM 进行检查、分析、记录等。当出现未知异常时可以通过记录进行故障分析。这个好用的工具在 `Java 11` 中将开源免费。所有人都可以使用这个功能了。

## 其他更新

1. JEP 309 - 添加动态文件常量。
2. JEP 318 - 添加 Epsilon 垃圾收集器。
3. JEP 320 - 删除 Java EE 和 corba 模块（java.xml.ws, java.xml.bind, java.activation, java.xml.ws.annotation, java.corba, java.transaction, java.se.ee, jdk.xml.ws, jdk.xml.bind）。
4. JEP 329 - 增加加密算法 chacha20,poly1305 的实现。
5. JEP 333 - 引入实验性的 ZGC 垃圾收集器，保证停摆时间不会超过 10ms。
6. JEP 335 - 废弃 Nashorn JavaScript 引擎

文章案例都已经上传到 Github：[niumoo/jdk-feature](https://github.com/niumoo/jdk-feature)

### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)