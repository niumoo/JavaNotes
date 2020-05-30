---
title: 最通俗易懂的 Java 10 新特性讲解
# toc: false
date: 2020-02-27 08:01:01
url: jdk/jdk10-feature
tags:
 - Java10
categories:
 - Java 新特性
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![Java 10](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/Whats_New_Java10-881x441.png)

自从 `Java 9` 开始，Oracle 调整了 Java 版本的发布策略，不再是之前的 N 年一个大版本，取而代之的是 6 个月一个小版本，三年一个大版本，这样可以让 Java 的最新改变迅速上线，而小版本的维护周期缩短到下个版本发布之前，大版本的维护周期则是 3 年之久。而 10 就是这么一个小版本，因为 Java 的后续版本基本都会包含之前新特性，所以还是把 `Java 10` 带来的改变单独写一写。

<!-- more -->

## 1. JEP 322 - 基于时间的版本号

就像上面说的，Java 调整了发布策略，为了适应这种发布节奏，随着改变的还有 Java 版本号的记录方式。

版本号的新模式是：`$FEATURE.$INTERIM.$UPDATE.$PATCH`

- `$FEATURE` ：基于发布版本，如 Java 10 的 10 。
- `$INTERIM` ：问题修复和功能增强时 + 1，默认是 0 。
- `$UPDATE` ：在进行兼容更新，修复新功能安全问题时 +1。
- `$PATCH` ：特殊问题修复时 +1。

查看自己的  `Java 10 ` 版本。

```java
$ java -version
java version "10.0.1" 2018-04-17
Java(TM) SE Runtime Environment 18.3 (build 10.0.1+10)
Java HotSpot(TM) 64-Bit Server VM 18.3 (build 10.0.1+10, mixed mode)
```

## 2. JEP 286 - 局部类型推断

JEP 286 提案让 Java 增加了局部类型推断（Local-Variable Type Inference）功能，这让 Java 可以像 `Js` 里的 `var` 或者其他语言的 `auto` 一样可以自动推断数据类型。这其实只是一个新的语法糖，底层并没有变化，在编译时就已经把 `var` 转化成具体的数据类型了，但是这样可以减少代码的编写。

你可以像下面这样使用 `var` 语法。

```java
var hashMap = new HashMap<String, String>();
hashMap.put("微信","wn8398");
var string = "hello java 10";
var stream = Stream.of(1, 2, 3, 4);
var list = new ArrayList<String>();
```

如果你反编译编译后的这段代码，你会发现还是熟悉的代码片段。

```java
HashMap<String, String> hashMap = new HashMap();
hashMap.put("微信", "wn8398");
String string = "hello java 10";
Stream<Integer> stream = Stream.of(1, 2, 3, 4);
ArrayList<String> list = new ArrayList();
```

`var` 看似好用，其实也有很多限制，官方介绍了 `var` 只能用于下面的几种情况。

1. 仅限带有初始化的程序的局部变量。
2. `for` 循环或者`增强for` 循环中。
3. `for`循环中的声明。

下面演示三种使用情况。

```java
public static void testVar() {
    // 情况1，没有初始化会报错
    // var list;
    var list = List.of(1, 2, 3, 4);
    // 情况2
    for (var integer : list) {
        System.out.println(integer);
    }
    // 情况3
    for (var i = 0; i < list.size(); i++) {
        System.out.println(list.get(i));
    }
}
```

尽管对 `var` 的使用场景增加了很多限制，但在实际使用时你还是要注意，就像下面的代码，你可能一眼并不能看出 `result` 的数据类型。

```java
var query = "xxx";
var result = dbUtil.executeQuery(query);
```



## 3. JEP 317 - 基于 Java 的 JIT 编译器（实验性）

这个功能让基于 Java 开发的 JIT 编译器 `Graal` 结合 `Java 10` 用在 Linux / x64 平台上，这是一个实验性的 JIT 编译器，有人说这也是 `Java 10` 中最具有未来感的引入。Graal 其实在 `Java 9 ` 中就已经引入了，它带来了 Java 中的 AOT （Ahead Of Time）编译，还支持多种语言，如 Js、Python、Ruby、R、以及其他基于 JVM （如 Java、Kotlin）的和基于 LLVM （如 C、C++）的语言。

想切换到 `Graal` 可以使用下面的 `jvm` 参数。

```shell
-XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler
```

这里面有一点我觉得很有意思，看这个图。

![Graal 由 Java 编写](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20200223223401909.png)

这就很有意思了，`Graal` 是 Java 语言编写的，用 Java 编写的编译器，然后用来将 Java 字节码编译机器代码。

`Graal` 官网：[https://www.graalvm.org/](https://www.graalvm.org/)

## 4. JEP 310 - 类数据共享

JVM 启动时有一步是需要在内存中加载类，而如果有多个 jar，加载第一个 jar  的速度是最慢的。这就延长了程序的启动时间，为了减少这个时间，`Java 10` 引入了应用程序类数据共享（CDS）机制，它可以把你想共享的类共享在程序之间，使不同的 Java 进程之间共享这个类来减少这个类占用的空间以及加载速度。

## 5. JEP 307 - G1 并行全GC

早在 `Java 9` 时就已经引入了 G1 垃圾收集器，G1 的优点很多。而在 `Java 10` 中还是做了小小调整，当 G1 的并发收集线程不能快速的完成全 GC 时，就会自动切换到**并行**收集，这可以减少在最坏情况下的 GC 速度。

## 6. JEP 314 - Unicode 语言标签扩展

这个提案让 JDK 实现了最新的 [LDML 规范](http://www.unicode.org/reports/tr35/tr35.html#Locale_Extension_Key_and_Type_Data)中指定的更多的扩展。

主要增加了下面几个扩展方法。

```java
java.time.temporal.WeekFields::of
java.util.Calendar::{getFirstDayOfWeek,getMinimalDaysInWeek}
java.util.Currency::getInstance
java.util.Locale::getDisplayName
java.util.spi.LocaleNameProvider
java.text.DateFormat::get*Instance
java.text.DateFormatSymbols::getInstance
java.text.DecimalFormatSymbols::getInstance
java.text.NumberFormat::get*Instance
java.time.format.DateTimeFormatter::localizedBy
java.time.format.DateTimeFormatterBuilder::getLocalizedDateTimePattern
java.time.format.DecimalStyle::of
```

尝试一下。

```java
Currency chinaCurrency = Currency.getInstance(Locale.CHINA);
Currency usCurrency = Currency.getInstance(Locale.US);
System.out.println("本地货币：" + chinaCurrency);
System.out.println("US.货币：" + usCurrency);

String displayName = Locale.getDefault().getDisplayName();
String displayLanguage = Locale.getDefault().getDisplayLanguage();
String displayCountry = Locale.getDefault().getDisplayCountry();
System.out.println("本地名称：" + displayName);
System.out.println("本地语言：" + displayLanguage);
System.out.println("本地国家：" + displayCountry);
int firstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek();
System.out.println("本地每周第一天：" + firstDayOfWeek);
```

输出结果。

```shell
本地货币：CNY
US.货币：USD
本地名称：中文 (中国)
本地语言：中文
本地国家：中国
本地每周第一天：1
```

## 7. API 更新

`Java 10` 删除了部分 API，也增加了一些实用方法。比如可以通过 `Collection.copyOf` 复制得到一个不可改变集合，即使原来的集合元素发生了变化也不会有影响。

```java
var list = new ArrayList<String>();
list.add("wechat");
list.add("wn8398");
List<String> copyList = List.copyOf(list);
list.add("test");
System.out.println(copyList);
// result
// [wechat, wn8398]
```

也为 `Optional` 增加了一个新的方法 `orElseThrow`。调用这个方法也可以获取到 `optional` 中的 `value` , 但是如果 `value` 为 `null` ，就会抛出异常。

另外在 `Stream` 最后收集数据的时候，`Collectors` 可以直接指定收集的集合为不可变集合，像下面这样。

```java
list.stream().collect(Collectors.toUnmodifiableList());
list.stream().collect(Collectors.toUnmodifiableSet());
```

## 其他更新 

`Java 10` 的更新内容不止这些，上面只是列举了常用的以及比较有意思的新特性。还有部分更新如：

1. JEP 312：Thread-Local Handshakes，JVM 内部功能，可以提高 JVM 性能。
2. JEP 313：删除了 `javah` 工具，说是删除，其实功能已经包含在 `Java 8` 中的 `javac` 里。
3. JEP 316：让 JVM 可以在备用的存储设备（如 NV-DIMM）上分配堆内存，而不用更改程序代码。
4. JEP 319：在JDK中提供一组默认的根证书颁发机构（CA）证书。


文章案例都已经上传到 Github：[niumoo/jdk-feature](https://github.com/niumoo/jdk-feature)

**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)