---
title: Jdk14 都要出了，Jdk9 的新特性还不了解一下？
# toc: false
date: 2020-02-19 08:01:01
url: jdk/jdk9-feature
tags:
 - Java9
categories:
 - Java 新特性
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![Java 9](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/140916143852191.png)

`Java 9` 中最大的亮点是 **Java 平台模块化**的引入，以及模块化 JDK。但是 `Java 9` 还有很多其他新功能，这篇文字会将重点介绍开发人员特别感兴趣的几种功能。

这篇文章也是 Java 新特性系列文章中的一篇，往期文章可以查看下面链接。

[还看不懂同事的代码？超强的 Stream 流操作姿势还不学习一下](https://www.wdbyte.com/2019/11/jdk/jdk8-stream/)

[还看不懂同事的代码？Lambda 表达式、函数接口了解一下](https://www.wdbyte.com/2019/11/jdk/jdk8-lambda/)

[Jdk14 都要出了，还不能使用 Optional优雅的处理空指针？](https://www.wdbyte.com/2019/11/jdk/jdk8-optional/)

[Jdk14 都要出了，Jdk8 的时间处理姿势还不了解一下？](https://www.wdbyte.com/2019/10/jdk/jdk8-time/)

[还看不懂同事代码？快来补一波 Java 7 语法特性](https://www.wdbyte.com/2020/01/jdk/jdk7-start/)

<!-- more -->

## 1.  模块化

`Java 9` 中的**模块化**是对 `Java` 的一次重大改进。但是**模块化**并不是最近才提出来的，我们经常使用的  `maven` 构建工具，就是典型的模块化构建工具。**模块化**不仅让模块命名清晰，写出高内聚低耦合的代码，更可以方便处理模块之间的调用关系。

![Java 9 模块系统](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/Explicit-modules.png)

在 Oracle 官方中为 `Java 9` 中的模块系统的定义如下：

> the module, which is a named, self-describing collection of code and data. This module system.

直白翻译：模块是一个命名的，自我描述的代码和数据的集合。

`Java 9` 不仅支持了模块化开发，更是直接把 `JDK` 自身进行了模块化处理。`JDK` 自身的模块化可以带来很多好处，比如：

- 方便管理，越来越大的 JDK 在模块化下结构变得更加清晰。
- 模块化 JDK 和 JRE 运行时镜像可以提高性能、安全性、维护性。
- 可以定制 JRE，使用更小的运行时镜像，比如网络应用不需要 swing 库，可以在打包时选择不用，减少性能消耗。
- 清晰明了的模块调用关系，避免调用不当出来的各种问题。

上面提到了 JDK 自身的模块化，我们通过浏览 JDK 9 的目录结构也可以发现一些变化。

![JDK 模块化](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20200217111754648.png)

最明显的是在 JDK 9 中 jre 文件夹不存在了。下面是在 IDEA 中查看的 JDK 9 的依赖，命名规范的模块看起来是不是让人赏心悦目呢？

![JDK 9 在 IDEA](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20200217112025069.png)

当然，这篇文章主要介绍 Java 9 的新特性，而模块化是一个巨大改变，结合示例介绍下来篇幅会比较长，这里就不占用太多篇幅了。

模块化文章预告：如何编写一个模块化系统，如何打包让没有安装 Java 环境的系统运行编写的代码，都可以通过模块化选择运行时模块实现。我后面的文章就会通过一个模块化项目介绍到，有兴趣的可以关注我后续文章 😎。

## 2. 集合工厂方法

在 Java 9 中为集合的创建增加了静态工厂创建方式，也就是 `of` 方法，通过静态工厂 `of` 方法创建的集合是**只读集合**，里面的对象**不可改变**。并在**不能存在 `null` 值**，对于 `set` 和 `map` 集合，也**不能存在 `key` 值重复**。这样不仅**线程安全**，而且**消耗的内存也更小**。

下面是三种集合通过静态工厂创建的方式。

```java
// 工厂方法创建集合
List<String> stringList = List.of("a", "b", "c", "d");
Set<String> stringSet = Set.of("a", "b", "c", "d");
Map<String, Integer> stringIntegerMap = Map.of("key1", 1, "key2", 2, "key3", 3);
Map<String, Integer> stringIntegerMap2 = Map.ofEntries(Map.entry("key1", 1), Map.entry("key2", 2));

// 集合输出
System.out.println(stringList);
System.out.println(stringSet);
System.out.println(stringIntegerMap);
System.out.println(stringIntegerMap2);
```

得到输出结果。

``` shell
[a, b, c, d]
[d, a, c, b]
{key2=2, key1=1, key3=3}
{key2=2, key1=1}
```

再次运行，得到输出结果。

```shell
[a, b, c, d]
[a, c, b, d]
{key3=3, key2=2, key1=1}
{key2=2, key1=1}
```

为什么我贴了两次运行结果呢？主要是要展示通过 `of` 方法创建的 `set` 和 `map` 集合在遍历时，在每个 JVM 周期遍历顺序是随机的，这样的机制可以发下代码中有没有对于顺序敏感的异常代码。

这种只读集合在 Java 9 之前创建是通过 `Collections.unmodifiableList` 修改集合操作权限实现的。

```java
List<String> arrayList = new ArrayList<>();
arrayList.add("达西");
arrayList.add("未读代码");
// 设置为只读集合
arrayList = Collections.unmodifiableList(arrayList);
```

静态工厂 `of` 方法创建的集合还有一个特性，就是工厂内部会自由复用已有实例或者创建新的实例，所以应该避免对 `of` 创建的集合进行判等或者 `haseCode` 比较等操作。

像下面这样，创建两个 `List`，你会发现两个 `List` 的 `hashCode` 是一样的。

```java
// 工厂可以自由创建新的实例或者复用现有实例，所以 使用 of 创建的集合，避免 == 或者 hashCode 判断操作
List<String> stringList = List.of("a", "b", "c", "d");
List<String> stringList2 = List.of("a", "b", "c", "d");
System.out.println(stringList.hashCode());
System.out.println(stringList2.hashCode());
// 输出结果
// 3910595
// 3910596
```

这也是使用 `of` 方法创建集合的优势之一，消耗更少的系统资源。这一点也体现在 `of` 创建的集合的数据结构实现上，有兴趣的同学可以自行研究下。

## 3. Stream API

`Stream` 流操作自从 `Java 8` 引入以来，一直广受好评。便捷丰富的 `Stream` 操作让人爱不释手，更让没看过的同事眼花缭乱，在介绍 `Java 8` 新特性时已经对 `Stream` 进行了详细的介绍，没看过的同学可以看下这篇：

[还看不懂同事的代码？超强的 Stream 流操作姿势还不学习一下](https://www.wdbyte.com/2019/11/jdk/jdk8-stream/) 

当然，学习 `Stream` 之前要先学习 `Lambda` ，如果你还没有看过，也可以看下之前这篇：

[还看不懂同事的代码？Lambda 表达式、函数接口了解一下](https://www.wdbyte.com/2019/11/jdk/jdk8-lambda/)

在 `Java 9` 中，又对 `Stream` 进行了增强，主要增加了 4 个新的操作方法：*dropWhile，takeWhile，ofNullable，iterate*。

下面对这几个方法分别做个介绍。

1. takeWhile: 从头开始筛选，遇到不满足的就结束了。

   ```java
   // takeWhile ,从头开始筛选，遇到不满足的就结束了
   List<Integer> list1 = List.of(1, 2, 3, 4, 5);
   List<Integer> listResult = list1.stream().takeWhile(x -> x < 3).collect(Collectors.toList());
   System.out.println(listResult);
   // takeWhile ,从头开始筛选，遇到不满足的就结束
   List<Integer> list2 = List.of(1, 2, 3, 4, 3, 0);
   List<Integer> listResult2 = list2.stream().takeWhile(x -> x < 3).collect(Collectors.toList());
   System.out.println(listResult2);
   ```

   输出结果。

   ```shell
   [1, 2]
   [1, 2]
   ```

2. dropWhile: 从头开始删除，遇到不满足的就结束了。

   ```java
   // dropWhile ,从头开始删除，遇到不满足的就结束了
   List<Integer> list1 = List.of(1, 2, 3, 4, 5);
   List<Integer> listResult = list1.stream().dropWhile(x -> x < 3).collect(Collectors.toList());
   System.out.println(listResult);
   // dropWhile ,从头开始删除，遇到不满足的就结束
   List<Integer> list2 = List.of(1, 2, 3, 4, 3, 0);
   List<Integer> listResult2 = list2.stream().dropWhile(x -> x < 3).collect(Collectors.toList());
   System.out.println(listResult2);
   ```

   输出结果。

   ```shell
   [3, 4, 5]
   [3, 4, 3, 0]
   ```

3. ofNullable: 创建支持全 null 的 Stream.

   ```java
   Stream<Integer> stream = Stream.of(1, 2, null);
   stream.forEach(System.out::print);
   System.out.println();
   // 空指针异常
   // stream = Stream.of(null);
   stream = Stream.ofNullable(null);
   stream.forEach(System.out::print);
   ```

   输出结果。

   ```shell
   12null
   ```

4. iterate: 可以重载迭代器。

   ```java
   IntStream.iterate(0, x -> x < 10, x -> x + 1).forEach(System.out::print);
   ```

   输出结果。

   ```shell
   0123456789
   ```

在 `Stream` 增强之外，还增强了 `Optional` ，`Optional` 增加了可以转换成 `Stream` 的方法。

```java
Stream<Integer> s = Optional.of(1).stream();
s.forEach(System.out::print);
```

## 4. 接口私有方法

在 `Java 8 ` 中增加了默认方法，在 `Java 9 ` 中又增加了私有方法，这时开始接口中不仅仅有了定义，还具有了行为。我想这是出于代码构造上的考虑，如果没有私有方法，那么当多个默认方法的行为一样时，就要写多个相同的代码。而有了私有方法，事情就变得不一样了。

就像下面的例子。

```java
/**
 * @author 达西 - 公众号：未读代码
 */
public class Jdk9Interface {
    public static void main(String[] args) {
        ChinaPeople chinaPeople = new ChinaPeople();
        chinaPeople.sleep();
        chinaPeople.eat();
        chinaPeople.doXxx();
    }

}

class ChinaPeople implements People {
    @Override
    public void sleep() {
        System.out.println("躺着睡");
    }
}

interface People {
    void sleep();

    default void eat() {
        drink();
    }

    default void doXxx() {
        drink();
    }

    private void drink() {
        System.out.println("喝水");
    }
}
```

例子中的接口 `people` 中的 `eat()` 和 `doXxx()` 默认行为一致，使用私有方法可以方便的抽取一个方法出来。

输出结果。

```shell
躺着睡
喝水
喝水
```

## 5. HTTP / 2 Client

`Java 9` 内置了新的 HTTP/2 客户端，请求更加方便。

随便访问一个不存在的网页。

```java
HttpClient client = HttpClient.newHttpClient();
URI uri = URI.create("http://www.tianqiapi.com/api/xxx");
HttpRequest req = HttpRequest.newBuilder(uri).header("User-Agent", "Java").GET().build();
HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandler.asString());
String body = resp.body();
System.out.println(body);
```

输出得到的结果，这里是这个网站的报错信息。

```java
There is no method xxxAction in ApiController
```

可能你运行的时候会报找不到 `httpClient` 模块之类的问题，这时候需要你在你项目代码目录添加 `httpClient 模块` 才能解决，添加方式看下面的图。

![Java 9 导入导出模块](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20200217225456437.png)

`export ` 写自己的包路径，`requires` 写引入的模块名。



## 6. Java REPL - JShell

交互式的编程环境在其他语言如 Python 上早就有了，而 Java 上的交互式语言只到 `Java 9`才出现。交互式的编程可以让开发者在输入代码的时候就获取到程序的运行结果，而不用像之前一样新建文件、创建类、导包、测试一系列流程。

`JShell` 中支持 `tab` 补全代码以及自动添加分号，下面通过一个例子演示 `JShell` 的使用。

1. 进入 JShell. 查看帮助文档。

   ```shell
   C:\Users>jshell
   |  欢迎使用 JShell -- 版本 9
   |  要大致了解该版本, 请键入: /help intro
   jshell> /help
   |  键入 Java 语言表达式, 语句或声明。
   |  或者键入以下命令之一:
   |  /list [<名称或 id>|-all|-start]
   |       列出您键入的源
   |  /edit <名称或 id>
   |       编辑按名称或 id 引用的源条目
   |  /drop <名称或 id>
   |       删除按名称或 id 引用的源条目
   |  /save [-all|-history|-start] <文件>
   |       将片段源保存到文件。
   |  /open <file>
   |       打开文件作为源输入
   |  /vars [<名称或 id>|-all|-start]
   |       列出已声明变量及其值
   |  /methods [<名称或 id>|-all|-start]
   |       列出已声明方法及其签名
   |  /types [<名称或 id>|-all|-start]
   |       列出已声明的类型
   |  /imports
   |       列出导入的项
   |  /exit
   |       退出 jshell
   |  /env [-class-path <路径>] [-module-path <路径>] [-add-modules <模块>] ...
   |       查看或更改评估上下文
   |  /reset [-class-path <路径>] [-module-path <路径>] [-add-modules <模块>]...
   |       重启 jshell
   |  /reload [-restore] [-quiet] [-class-path <路径>] [-module-path <路径>]...
   |       重置和重放相关历史记录 -- 当前历史记录或上一个历史记录 (-restore)
   |  /history
   |       您键入的内容的历史记录
   |  /help [<command>|<subject>]
   |       获取 jshell 的相关信息
   |  /set editor|start|feedback|mode|prompt|truncation|format ...
   |       设置 jshell 配置信息
   |  /? [<command>|<subject>]
   |       获取 jshell 的相关信息
   |  /!
   |       重新运行上一个片段
   |  /<id>
   |       按 id 重新运行片段
   |  /-<n>
   |       重新运行前面的第 n 个片段
   |
   |  有关详细信息, 请键入 '/help', 后跟
   |  命令或主题的名称。
   |  例如 '/help /list' 或 '/help intro'。主题:
   |
   |  intro
   |       jshell 工具的简介
   |  shortcuts
   |       片段和命令输入提示, 信息访问以及
   |       自动代码生成的按键说明
   |  context
   |       /env /reload 和 /reset 的评估上下文选项
   
   jshell>
   ```

2. 定义一个变量：a = 10，遍历从 0 到 a 的数字。

   ```java
   jshell> int a =10;
   a ==> 10
   jshell> for(int i=0;i<a;i++){System.out.println(i);}
   0
   1
   2
   3
   4
   5
   6
   7
   8
   9
   ```

3. 定义一个集合，赋值1,2,3,4,5。然后输出集合。

   ```java
   jshell> List list = List.of(1,2,3,4,5);
   list ==> [1, 2, 3, 4, 5]
   jshell> list
   list ==> [1, 2, 3, 4, 5]
   ```

4. 查看输入过的代码。

   ```java
   jshell> /list
      1 : int a =10;
      2 : for(int i=0;i<a;i++){System.out.println(i);}
      3 : List list = List.of(1,2,3,4,5);
      4 : list
   ```

5. 列出导入的包。

   ```java
   jshell> /imports
   |    import java.io.*
   |    import java.math.*
   |    import java.net.*
   |    import java.nio.file.*
   |    import java.util.*
   |    import java.util.concurrent.*
   |    import java.util.function.*
   |    import java.util.prefs.*
   |    import java.util.regex.*
   |    import java.util.stream.*
   ```

6. 将代码保存到文件并退出。

   ```java
   jshell> /save d:/JShell.java
   jshell> /exit
     再见
   ```

   在 D 盘看到的保存的代码片段。

   ![JShell 保存的代码](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20200217232617028.png)

   

操作起来还是挺简单的，还记得上面介绍集合工厂 `of` 方法创建出来的 `set` 和 `map` 数据在每个 `JVM` 周期里是无序的嘛？也可以用 `JShell` 实验下。

![Set.of 的随机遍历](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20200218113246293.png)

## 7. 其他更新

`Java 9` 中增加或者优化的功能远不止这些，上面只是列举了常用的一些新特性，更多的新特性如：

- 不能使用下划线 _ 作为变量名，因为它是一个关键字。
- Javadoc 支持 HTML5 并且支持搜索功能。
- Nashorn 引擎升级，更好的支持 Javascript.
- String 存储结构变更从 char -> byte.
- .........

新特性很多，感兴趣的可以自己了解下。

**再次预告**，后续文章会结合案例图文并茂详细介绍 **Java 9 开始的模块系统**，感兴趣的可以关注我。此去山高水远，愿你我一路同行。

文章案例都已经上传到 Github：[niumoo/jdk-feature](https://github.com/niumoo/jdk-feature)

**参考资料**

- [Java Platform, Standard Edition What’s New in Oracle JDK 9](https://docs.oracle.com/javase/9/whatsnew/toc.htm)


**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)