---
title: 还看不懂同事代码？快来补一波 Java 7 语法特性
# toc: false
date: 2020-01-08 08:01:01
url: jdk/jdk7-start
tags:
 - Java7
 - AutoCloseable
categories:
 - Java 新特性
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

# 前言

Java 平台自出现到目前为止，已经 20 多个年头了，这 20 多年间 Java 也一直作为最流行的程序设计语言之一，不断面临着其他新兴编程语言的挑战与冲击。Java 语言是一种**静态强类型**语言，这样的语言特性可以让 Java 编译器在**编译阶段**发现错误，这对于构建出一个**稳定安全且健壮**的应用来说，尤为重要。但是也因为这种特性，让 Java 开发似乎变得缺少灵活性，开发某些功能的应用时，代码量可能是其他语言的几倍。Java 开发的不足之处也体现越来越复杂的 JDK 上，越来越复杂的 JDK 让开发者完全理解的难度变的非常大。以至于开发者有时会重复实现一个 JDK 中已经提供了的功能。
<!-- more -->
为了跟上互联网应用编程发展的脚步， Java 从 9 版本开始调整了 JDK 发布的节奏，JDK 的每次更新都注重**提高生产效率**，提高 **JVM 性能**，推行**模块化**等，让开发者可以更多的专注于业务本身，而不是浪费过多的时间在语言特性上。 Java 语言的更新要在语言的严谨性和灵活性上找到一个平衡点，毕竟灵活性可以减少编码的复杂度，而严谨性是构建复杂且健壮应用的基石。

# Java 7 语言特性

Java 重要的更新版本是在 Java 5 版本，这个版本中增加了如泛型、增强 for、自动装箱拆箱、枚举类型，可变参数、注解等一系列**重要功能**，但是随后的 Java 6 中并没有增加新的重要的语言特性。Java 5 的发布是在 2004 年，已经很久远了，网上关于 Java 的教程也大多是基于 Java 6 的，也因此我准备从 Java 7 开始介绍每个 Java 版本的新特性。

下面所有代码的运行演示都是基于 **Java 7 ** ，所以你如果尝试下面的代码，需要**安装并配置**  Jdk 1.7 或者已上版本。

# 1. switch String

在 Java 7 之前，switch 语法中只支持整数类型以及这些整数类型的封装类进行判断，在 Java 7 中，支持了 string 字符串类型的判断，使用起来非常的简单，但是实用性是很高的。

## 1.1. switch String 基本用法

编写一个简单的 switch 判断字符串的测试类。

```java
public class SwitchWithString {

    public static void main(String[] args) {
        String gender = "男";
        System.out.println(gender.hashCode());
        switch (gender) {
            case "男":
                System.out.println("先生你好");
                break;
            case "女":
                System.out.println("女士你好");
                break;
            default:
                System.out.println("你好");
        }
    }
}
```

switch 判断字符串使用起来很简单，结果也显而易见会先输出 gender 变量的 hashCode，然后输出匹配结果“先生你好”。

```java
30007
先生你好
```

在使用 switch string 时候，如果结合 Java 5 的**枚举类**，那么效果会更好，Java 7 之前使用 switch 结合枚举类要为每个枚举值编数字代号，Java 7 之后可以枚举进行 switch。

## 1.2. switch String 实现原理

但是这个支持**只是编译器层面的支持**， Java 虚拟机依旧是不支持的。在对字符串进行 switch 时，编译器会把字符串**转换成整数**类型再进行判断。为了验证上面说的只是编译器层面的支持，我们反编译（可以使用 Jad 反编译工具，也可以在 Idea 中双击编译生成的 class ）生成的 class 文件，看到编译器把 switch string 转换成了字符串 hashCode 判断，为了防止 hashCode 冲突，又使用了 equals 再次判断。

```java
public class SwitchWithString {
    public SwitchWithString() {
    }
    
    public static void main(String[] args) {
        String gender = "男";
        System.out.println(gender.hashCode());
        byte var3 = -1;
        switch(gender.hashCode()) {
        case 22899:
            if (gender.equals("女")) {
                var3 = 1;
            }
            break;
        case 30007:
            if (gender.equals("男")) {
                var3 = 0;
            }
        }

        switch(var3) {
        case 0:
            System.out.println("先生你好");
            break;
        case 1:
            System.out.println("女士你好");
            break;
        default:
            System.out.println("你好");
        }

    }
}
```

# 2. try-with-resource

Java 不同于 C++，需要开发者自己管理每一块内存，大多时候 Java 虚拟机都可以很好的帮我们进行资源管理，但是也有时候需要手动释放一些资源，比如数据库连接、磁盘文件连接、网络连接等。换句话说，只要是资源数量有限的，都需要我们手动的进行释放。

## 2.1. try-catch-finally

在操作有限资源的时候，可能会出现各种异常，不管是读取阶段还是在最后关闭资源的过程中，都有可能出现问题，我们通常会使用下面的方式 `try-catch-finally` 保证资源的释放。

像下面这样。

```java
/**
 * 释放资源
 *
 * @author www.wdbyte.com
 */
public class TryCatachFinally {

    /**
     * 异常处理
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("jdk-feature-7.iml");
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }
}
```

看看这恶心的代码结构，为了捕获异常，我们写了一个 `catch`，为了能保证释放资源，我们又写了 `finally` 进行资源释放，在资源释放时为了捕捉 `close` 时抛出的异常，我们又写了一个 `try-catch`。最后看着这复杂的代码，如果有人告诉你这段代码有 `bug`，那你一定不会相信。但是确实是这样，看起来严密的代码逻辑，当 `try` 中的代码逻辑和 `close` 方法同时产生异常的时候，`try` 中的异常信息会丢失。

可以看这里例子。

```java
package net.codingme.feature.jdk7;

import java.io.IOException;

/**
 * 释放资源
 *
 * @author www.wdbyte.com
 */
public class TryCatachFinallyThrow {

    /**
     * 异常处理
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        read();
    }

    public static void read() throws Exception {
        FileRead fileRead = null;
        try {
            fileRead = new FileRead();
            fileRead.read();
        } catch (Exception e) {
            throw e;
        } finally {
            if (fileRead != null) {
                try {
                    fileRead.close();
                } catch (Exception e) {
                    throw e;
                }
            }
        }

    }
}

class FileRead {

    public void read() throws Exception {
        throw new IOException("读取异常");
    }

    public void close() throws Exception {
        System.out.println("资源关闭");
        throw new IOException("关闭异常");
    }
}
```

很明显代码里 `read` 和 `close` 方法都会产生异常，但是运行程序发现只能收到 `close` 的异常信息。

```log
资源关闭
Exception in thread "main" java.io.IOException: 关闭异常
	at net.codingme.feature.jdk7.FileRead.close(TryCatachFinallyThrow.java:51)
	at net.codingme.feature.jdk7.TryCatachFinallyThrow.read(TryCatachFinallyThrow.java:33)
	at net.codingme.feature.jdk7.TryCatachFinallyThrow.main(TryCatachFinallyThrow.java:20)
```

**异常信息丢失**了，可怕的是你以为只是 `close` 时发生了异常而已。

## 2.2. try-autocloseable

上面的问题在 Java 7 中其实已经提供了新的解决方式，Java 7 中对 `try` 进行了增强，可以保证资源**总能被正确释放** 。使用增强 `try` 的前提是 `try` 中的类实现了 `AutoCloseable` 接口，在 Java 7 中大量的需要释放资源的操作其实都已经实现了此接口了。

![AutoCloseable 实现类](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/image-20200107084113348.png)

实现了 `AutoCloseable` 的类，在增强 `try`中使用时，不用担心资源的关闭，在使用完毕会自动的调用 `close`方法，并且**异常不会丢失**。

让我们编写的模拟资源操作的类实现 `AutoCloseable`  接口，然后时候增强 `try` 看看效果。

```java
package net.codingme.feature.jdk7;

/**
 * 自动关闭
 *
 * @author www.wdbyte.com
 */
public class AutoCloseResource {
    public static void main(String[] args) throws Exception {
        try (Mysql mysql = new Mysql();
             OracleDatabase oracleDatabase = new OracleDatabase()) {
            mysql.conn();
            oracleDatabase.conn();
        }
    }
}

class Mysql implements AutoCloseable {

    @Override
    public void close() throws Exception {
        System.out.println("mysql 已关闭");
    }

    public void conn() {
        System.out.println("mysql 已连接");
    }
}

class OracleDatabase implements AutoCloseable {

    @Override
    public void close() throws Exception {
        System.out.println("OracleDatabase 已关闭");
    }

    public void conn() {
        System.out.println("OracleDatabase 已连接");
    }
}
```

测试类 Mysql 和  OracleDatabase 都是实现了 AutoCloseable，运行查看结果。

```java
mysql 已连接
OracleDatabase 已连接
OracleDatabase 已关闭
mysql 已关闭
```

确认在发生异常时候异常信息不会丢失，写一个有异常的模拟测试类进行测试。

```java
package net.codingme.feature.jdk7;

import java.io.IOException;

/**
 * 释放资源
 *
 * @author www.wdbyte.com
 */
public class AutoCloseThrow {

    public static void main(String[] args) throws Exception {
        try (FileReadAutoClose fileRead = new FileReadAutoClose()) {
            fileRead.read();
        }
    }
}

class FileReadAutoClose implements AutoCloseable {

    public void read() throws Exception {
        System.out.println("资源读取");
        throw new IOException("读取异常");
    }

    @Override
    public void close() throws Exception {
        System.out.println("资源关闭");
        throw new IOException("关闭异常");
    }
}
```

运行查看异常信息。

```log
资源读取
资源关闭
Exception in thread "main" java.io.IOException: 读取异常
	at net.codingme.feature.jdk7.FileReadAutoClose.read(AutoCloseThrow.java:23)
	at net.codingme.feature.jdk7.AutoCloseThrow.main(AutoCloseThrow.java:14)
	Suppressed: java.io.IOException: 关闭异常
		at net.codingme.feature.jdk7.FileReadAutoClose.close(AutoCloseThrow.java:29)
		at net.codingme.feature.jdk7.AutoCloseThrow.main(AutoCloseThrow.java:15)
```

自动关闭，异常清晰，关闭异常存在于 `Suppressed` ，称为抑制异常，后续文章会详细介绍。

# 3. try-catch

在 Java 7 之前，一个 catch 只能捕获一个异常信息，当异常种类非常多的时候就很麻烦，但是在 Java 7 中，一个 catch 可以捕获多个异常信息，每个异常捕获之间使用 `|` 分割，

```java
package net.codingme.feature.jdk7;

import java.io.IOException;

/**
 * 多异常捕获
 */
public class TryCatchMany {

    public static void main(String[] args) {
        try (TxtRead txtRead = new TxtRead()) {
            txtRead.reader();
        } catch (IOException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class TxtRead implements AutoCloseable {

    @Override
    public void close() throws Exception {
        System.out.println("资源释放");
    }

    public void reader() throws IOException, NoSuchFieldException {
        System.out.println("数据读取");
    }
}
```

需要注意的是，一个 catch 捕获多个异常时，不能出现重复的异常类型，也不能出现一个异常类型是另一个类的子类的情况。

# 4. 二进制

Java 7 开始，可以直接指定不同的进制数字。

1. 二进制指定数字值，只需要使用 `0b` 或者 `OB` 开头。
2. 八进制指定数字值，使用 `0` 开头。
3. 十六进制指定数字值，使用 `0x` 开头。

```java
/**
 * 二进制
 *
 * @author www.wdbyte.com
 */
public class Binary {
    public static void main(String[] args) {
        // 二进制
        System.out.println("------2进制-----");
        int a = 0b001;
        int b = 0b010;
        System.out.println(a);
        System.out.println(b);
        // 八进制
        System.out.println("------8进制-----");
        int a1 = 010;
        int b1 = 020;
        System.out.println(a1);
        System.out.println(b1);
        // 十六进制
        System.out.println("------16进制-----");
        int a2 = 0x10;
        int b2 = 0x20;
        System.out.println(a2);
        System.out.println(b2);
    }
}
```

输出结果。

```log
------2进制-----
1
2
------8进制-----
8
16
------16进制-----
16
32
```

# 5. 数字下划线

Java 7 开始支持在数字定义时候使用下划线分割，增加了数字的可读性。

```java
/**
 * 数字下环线
 *
 * @author www.wdbyte.com
 */
public class NumberLine {
    public static void main(String[] args) {
        int a = 1_000;
        int b = 1_0__0_0_0_____00;
        System.out.println(a);
        System.out.println(b);
    }
}
```

得到结果。

```log
1000
1000000
```

# 6. 结束语

虽然 Java 7 早在 2011 年就已经发布了，但是据我发现，使用到 Java 7 开始的新特性新语法的并不多，所以我的 JDK 新特性系列文章计划从 Java 7 开始，一直介绍到目前已经发布的  Java 13，以后 Java 新版本更新的同时，这个新特性系列文章也会持续更新。

此去山高水远，愿能一路坚持，愿你我一路同行。

**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)