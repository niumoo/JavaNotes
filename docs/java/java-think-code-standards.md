---
title: Java 开发的编程噩梦，这些坑你没踩过算我输
date: 2020-08-07 08:01:01
url: java/java-code-standards
tags:
 - 开发技巧
categories:
 - Java 开发 
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

很多 Java 初学者在开始编程时会出现一些问题，这些问题并不是指某个特定领域的问题，也不是指对某个业务不熟悉而导致的问题，而是对基础知识不够熟悉导致的问题。而就是这些问题让我们编写了一些不够健壮的代码。
这篇文章会列举几种编程初学者常常出现的一些问题，我相信这些问题多多少少也曾困扰着现在或曾经的你。如果觉得文章不错，不妨点赞分享，让更多人跳过这些开发中的坑。

## 随处可见的 Null 值
我见过很多的代码会把 Null 值作为返回值，当你预期是一个字符串时，意外得到了一个 Null 值；当你预期得到一个 List 时，意外又得到了一个 Null 值，如果你不进行处理，那么你还会意外得到 `NullPointerException`.
就像下面这样。

```java
// 情况1
String userTag = getUserTag();
if (userTag.equals("admin")) { // NullPointerException
   // ...
}

// 情况2
List<String> carList = getCarList();
for (String car : carList) { // NullPointerException
    // ...
}

```
为了防止这种情况，你可以在 List 返回时给出一个空的集合而不是 Null，如果是字符串，你可以把要确定有值对象放在比较的前面。

```java
if ("admin".equals(userTag)) {
    // ...
}
// 或者
if (Objects.equals(userTag,"admin")){
    // ...
}
```

## 没有进行空值检查
可能你考虑到了上面的 Null 值情况，但是在实际处理时没有考虑空值情况，比如字符空串空串 ""，或者集合为空。那么在后续处理时又有可能得到一个 `NullPointerException`. 所以你应该进行空值判断。
```java
String userTag = getUserTag();
if (userTag != null && userTag.trim() != "") {
    // ...
}

List<String> carList = getCarList();
if (carList != null && !carList.isEmpty()) {
    // ...
}
```
## 忽略的异常处理
异常处理总是一件烦人的事，而忽略异常似乎总有一种吸引人的魔力。我见过像下面这样的代码。

```java
try {
    List<String> result= request();
    // ...
}catch (Exception e){
    
}
```

你没有看错，catch 中没有任何内容，后来出现了问题，看着日志文件一片太平无迹可寻。异常是故意抛出来的，你应该正确处理它们或者继续抛出。而且同时，你该输出一行日志用来记录这个异常，方便以后的问题追踪。

## 没有释放资源

在读取文件或者请求网络资源时，总是需要进行 close 操作，这很重要，否则可能会阻塞其他线程的使用。但是初学者可能会忘记这一步操作。其实在 Java 7 开始，就提供了 `try-with-resources` 自动关闭资源的特性，只需要把打开的资源放入 `try` 中。

```java
try (FileReader fileReader = new FileReader("setting.xml")) {
    // fileReader.read();
    // ...
} catch (Exception e) {
    e.printStackTrace();
}
```
像上面这样，不需要在 `finally` 里手动调用 `fileReader` 的 `close` 方法关闭资源，因为放在 `try` 里的资源调用会在使用完毕时自动调用 `close`. 而且不管是否有异常抛出，这很实用。

## ConcuretModificationException

总有一天你会遇到 `ConcuretModificationException` ，然后开始百度搜索它的解决方式，这个异常最常见的场景是你在遍历一个集合时进行更新操作，比如像下面这样。
```java
List<String> list = new ArrayList<>();
list.add("a1");
list.add("b1");
list.add("b2");
list.add("c1");
for (String s : list) {
    if ("b1".equals(s)) {
        list.remove(s);
    }
}
```

这个异常很有用处，因为 ArrayList 不是线程安全的集合，假设你这边一边遍历，另一个线程不断更新，非线程安全集合会导致你的遍历结果不正确，所以这个异常的存在是合理的。同理 HashMap 也是如此，关于 HashMap 之前已经有一篇文章详细介绍了，可以参考 [最通俗易懂的 HashMap 源码分析解读](https://mp.weixin.qq.com/s/q-rWq79HmzPe08gyfOjaIA)。

## 缺少注释

**准确的注释可以救人于水火**，这点有时候一点也不夸张。虽然说优秀的代码本身就是非常好的注释，但是这实际开发起来，很少发生。注释并不需要你事无巨细的一一记录，但是你该在核心逻辑添加应有的注释，比如复杂逻辑的实现思路，当前逻辑业务需求。某个判断的添加原因，某个异常的发生情况等等。这可以让你在未来的某一天需要回看现在的代码时感谢自己。更可以**让你在某天的甩锅中轻松胜出**。

## 不进行代码测试

我见过有些同事在功能开发完毕后直接扔给对接同事使用，而自己却没有经过任何测试，或者只是测试了某个简单的情况。测试是开发过程中的重要环节，没有经过严格测试的代码很难说没有问题，我觉得在功能开发完毕后至少需要**单元测试**，**特殊用例**测试，**集成测试**以及其他形式的测试。**严格的测试不仅可以第一时间发现问题，更可以减少后面不必要的对接调试时间**。

## 重复造轮子

你知道的，Java 社区非常活跃，存在着大量的第三方类库，开源作者可能花费了数年时间去维护和完善类库，这些类库非常优秀。同时 JDK 也提供了大量的常用的功能封装。这些都可以**为我们的开发速度插上翅膀**。所以，当你需要一个功能时候，应该首先看下 JDK 和已经引入的类库中是否已经存在相同功能，而不是自己重复造轮子，而且大部分情况下你造的轮子还不如别人好。

下面举些例子。

- 你需要日志记录，可以使用 logback.
- 你需要网络操作，可以使用 netty.
- 你需要解析 JSON，可以使用 gson.
- 你需要解析表格，可以使用 apache poi.
- 你需要通用操作，可以使用 apache commons.

另外一种情况是，你可能不知道某个功能在 JDK 中已经实现，这时候你应该多多查看 JDK Document. 我就在工作中见到过同事手写字符串 split，为了获取时间戳把 Date 对象转换到 Calendar.

## 缺少必要的沟通

这个部分是和开发没有关系的，但是这个环节往往会影响最终的开发结果。进行具体的开发之前，你应该详细的沟通并理解功能的需求，这样你才能针对具体的需求写出不偏离实际需要的代码。有时候你很有可能因为缺少必要的沟通，错误了理解了需求，最终在开发完毕后发现自己写的功能完全没有用处。

## 没有代码规范

代码规范性非常重要，如果一个项目里充斥着各种稀奇古怪的代码规范，会让维护者十分头疼。而且软件行业高速发展，对开发者的综合素质要求也越来越高，优秀的编程习惯也可以提高软件的最终质量。比如：标新立异的命名风格挑战阅读习惯；五花八门的错误码人为地 增加排查问题的难度；工程结构混 乱导致后续项目维护艰难；没有鉴权的漏洞代码易被黑客攻击；质量低下的代码上线之后漏洞百出等等。因为没有统一的代码规范，开发中的问题可能层出不穷。

下面简单列举些应该统一的开发规范。如**命名风格如何是好；常量名称结构怎样；代码格式怎么统一；日期时间格式如何处理；集合处理注意事项；日志打印有无规范；前后交互具体规约**等。

上面所说的开发规范代码规范推荐阿里推出的 《**Java 开发手册**》，里面详细列举了在 Java 开发中各个方面应该遵守的规约和规范。最新版本在 8月 3日已经发布，可以在公众号 “**未读代码**” 直接回复 "java " **获取最新版 pdf.**

## 总结

Bug 和技术上的误解都是美丽的谜团，福尔摩斯般的我们终将解决这些问题。命运自己掌握，每一次探清的这些技术误解，都会增加我们对开发编码的理解。**尽情接招吧，色彩斑斓才有趣，万般体验才是人生，不管是多样的技术，还是多样的问题，我都想看见。**



**参考：**

[1] [A beginner’s guide to Java programming nightmares](https://jaxenter.com/java-programming-nightmares-156749.html)

[2] [Java™ Platform, Standard Edition 8 API Specification](https://docs.oracle.com/javase/8/docs/api/)

**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)