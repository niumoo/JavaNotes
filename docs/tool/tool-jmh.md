---
title: JMH-大厂是如何使用JMH进行Java代码性能测试的？必须掌握！
date: 2020-08-25 08:01:01
url: develop/tool-jmh
tags:
 - 性能测试
 - jmh
categories:
 - 生产工具
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

## Java 性能测试难题

现在的 JVM 已经越来越为智能，它可以在编译阶段、加载阶段、运行阶段对代码进行优化。比如你写了一段不怎么聪明的代码，到了 JVM 这里，它发现几处可以优化的地方，就顺手帮你优化了一把。这对程序的运行固然美妙，却让开发者不能准确了解程序的运行情况。在需要进行性能测试时，如果不知道 JVM 优化细节，可能会导致你的测试结果差之毫厘，失之千里，同样的，Java 诞生之初就有一次编译、随处运行的口号，JVM 提供了底层支持，也提供了内存管理机制，这些机制都会对我们的性能测试结果造成不可预测的影响。

```java
long start = System.currentTimeMillis();
// ....
long end = System.currentTimeMillis();
System.out.println(end - start);
```

上面可能就是你最常见的性能测试了，这样的测试结果真的准确吗？答案是否定的，它有下面几个问题。

1. 时间精度问题，本身获取到的时间戳就是存在**误差**的，它和操作系统有关。
2. JVM 在运行时会进行**代码预热**，说白了就是**越跑越快**。因为类需要装载、需要准备操作。
3. JVM 会在各个阶段都有可能对你的代码进行**优化处理**。
4. **资源回收**的不确定性，可能运行很快，回收很慢。

带着这些问题，突然发现进行一次严格的基准测试的难度大大增加。那么如何才能进行一次严格的基准测试呢？

## JMH 介绍

那么如何对 Java 程序进行一次精准的性能测试呢？难道需要掌握很多 JVM 优化细节吗？难道要研究如何避免，并进行正确编码才能进行严格的性能测试吗？显然不是，如果是这样的话，未免过于困难了，好在有一款一款官方的微基准测试工具 - **JMH**.

**JMH** 的全名是 Java Microbenchmark Harness，它是由 **Java 虚拟机团队**开发的一款用于 Java **微基准测试工具**。用自己开发的工具测试自己开发的另一款工具，以子之矛，攻子之盾果真手到擒来，如臂使指。使用 **JMH** 可以让你方便快速的进行一次严格的代码基准测试，并且有多种测试模式，多种测试维度可供选择；而且使用简单、增加注解便可启动测试。

## JMH 使用 

JMH 的使用首先引入 maven 所需依赖，当前最新版 为 1.23 版本。

``` xml
<!--jmh 基准测试 -->
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.23</version>
</dependency>
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.23</version>
    <scope>provided</scope>
</dependency>
```

### 快速测试

下面使用注解的方式指定测试参数，通过一个例子展示 JMH 基准测试的具体用法，先看一次运行效果，然后再了解每个注解的具体含义。

这个例子是使用 JMH 测试，使用加号拼接字符串和使用 `StringBuilder` 的 `append` 方法拼接字符串时的速度如何，每次拼接1000个数字进行平均速度比较。

```java
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * <p>
 * JMH 基准测试入门
 *
 * @author niujinpeng
 * @Date 2020/8/21 1:13
 */
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JmhHello {

    String string = "";
    StringBuilder stringBuilder = new StringBuilder();

    @Benchmark
    public String stringAdd() {
        for (int i = 0; i < 1000; i++) {
            string = string + i;
        }
        return string;
    }

    @Benchmark
    public String stringBuilderAppend() {
        for (int i = 0; i < 1000; i++) {
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(JmhHello.class.getSimpleName())
            .build();
        new Runner(opt).run();
    }
}

```

代码很简单，不做解释，`stringAdd` 使用加号拼接字符串 1000次，`stringBuilderAppend` 使用 `append` 拼接字符串 1000次。直接运行 main 方法，稍等片刻后可以得到详细的运行输出结果。

```log
// 开始测试 stringAdd 方法
# JMH version: 1.23
# VM version: JDK 1.8.0_181, Java HotSpot(TM) 64-Bit Server VM, 25.181-b13
# VM invoker: D:\develop\Java\jdk8_181\jre\bin\java.exe
# VM options: -javaagent:C:\ideaIU-2020.1.3.win\lib\idea_rt.jar=50363:C:\ideaIU-2020.1.3.win\bin -Dfile.encoding=UTF-8
# Warmup: 3 iterations, 10 s each  // 预热运行三次
# Measurement: 5 iterations, 10 s each // 性能测试5次 
# Timeout: 10 min per iteration  // 超时时间10分钟
# Threads: 1 thread, will synchronize iterations  // 线程数量为1
# Benchmark mode: Average time, time/op  // 统计方法调用一次的平均时间
# Benchmark: net.codingme.jmh.JmhHello.stringAdd // 本次执行的方法

# Run progress: 0.00% complete, ETA 00:02:40
# Fork: 1 of 1
# Warmup Iteration   1: 95.153 ms/op  // 第一次预热，耗时95ms
# Warmup Iteration   2: 108.927 ms/op // 第二次预热，耗时108ms
# Warmup Iteration   3: 167.760 ms/op // 第三次预热，耗时167ms
Iteration   1: 198.897 ms/op  // 执行五次耗时度量
Iteration   2: 243.437 ms/op
Iteration   3: 271.171 ms/op
Iteration   4: 295.636 ms/op
Iteration   5: 327.822 ms/op


Result "net.codingme.jmh.JmhHello.stringAdd":
  267.393 ±(99.9%) 189.907 ms/op [Average]
  (min, avg, max) = (198.897, 267.393, 327.822), stdev = 49.318  // 执行的最小、平均、最大、误差值
  CI (99.9%): [77.486, 457.299] (assumes normal distribution)
  
// 开始测试 stringBuilderAppend 方法
# Benchmark: net.codingme.jmh.JmhHello.stringBuilderAppend

# Run progress: 50.00% complete, ETA 00:01:21
# Fork: 1 of 1
# Warmup Iteration   1: 1.872 ms/op
# Warmup Iteration   2: 4.491 ms/op
# Warmup Iteration   3: 5.866 ms/op
Iteration   1: 6.936 ms/op
Iteration   2: 8.465 ms/op
Iteration   3: 8.925 ms/op
Iteration   4: 9.766 ms/op
Iteration   5: 10.143 ms/op


Result "net.codingme.jmh.JmhHello.stringBuilderAppend":
  8.847 ±(99.9%) 4.844 ms/op [Average]
  (min, avg, max) = (6.936, 8.847, 10.143), stdev = 1.258
  CI (99.9%): [4.003, 13.691] (assumes normal distribution)


# Run complete. Total time: 00:02:42

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.
// 测试结果对比
Benchmark                     Mode  Cnt    Score     Error  Units
JmhHello.stringAdd            avgt    5  267.393 ± 189.907  ms/op
JmhHello.stringBuilderAppend  avgt    5    8.847 ±   4.844  ms/op

Process finished with exit code 0
```

上面日志里的 `//` 注释是我手动增加上去的，其实我们只需要看下面的最终结果就可以了，可以看到 `stringAdd` 方法平均耗时 267.393ms，而  `stringBuilderAppend` 方法平均耗时只有 8.847ms，可见 `StringBuilder` 的 `append` 方法进行字符串拼接速度快的多，这也是我们推荐使用` append` 进行字符串拼接的原因。

### 注解说明

经过上面的示例，想必你也可以快速的使用 JMH 进行基准测试了，不过上面的诸多注解你可能还有疑惑，下面一一介绍。

**类上**使用了六个注解。

```
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
```

**@BenchmarkMode(Mode.AverageTime)** 表示统计平均响应时间，不仅可以用在类上，也可用在**测试方法**上。

除此之外还可以取值：

- Throughput：统计单位时间内可以对方法测试多少次。
- SampleTime：统计每个响应时间范围内的响应次数，比如 0-1ms，3次；1-2ms，5次。
- SingleShotTime：跳过预热阶段，直接进行**一次****微基准**测试。

**@State(Scope.Thread)**：每个进行基准测试的线程都会独享一个对象示例。

除此之外还能取值：

- Benchmark：多线程共享一个示例。
- Group：线程组共享一个示例，在测试方法上使用 @Group 设置线程组。

**@Fork(1)**：表示开启一个线程进行测试。

**OutputTimeUnit(TimeUnit.MILLISECONDS)：输出的时间单位，这里写的是毫秒。

**@Warmup(iterations = 3)**：微基准测试前进行三次预热执行，也可用在**测试方法**上。

**@Measurement(iterations = 5)**：进行 5 次微基准测试，也可用在**测试方法**上。



在两个测试方法上只使用了一个注解 **@Benchmark**，这个注解表示这个方法是要进行基准测试的方法，它类似于 Junit 中的 **@Test** 注解。上面还提到某些注解还可以用到测试方法上，也就是使用了 **@Benchmark** 的方法之上，如果类上和测试方法同时存在注解，会以**方法上的注解**为准。

其实 JMH 也可以把这些参数直接在 main 方法中指定，这时 main 方法中指定的级别最高。

```java
public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
            .include(JmhHello.class.getSimpleName())
            .forks(1)
            .warmupIterations(5)
            .measurementIterations(10)
            .build();
    new Runner(opt).run();
}
```

## 正确的微基准测试

如果编写的代码本身就存在着诸多问题，那么即使使用正确的测试方法，也不可能得到正确的测试结果。这些测试代码中的问题应该由我们进行主动避免，那么有哪些常见问题呢？下面介绍两种最常见的情况。

### 无用代码消除 （ Dead Code Elimination ）

也有网友形象的翻译成**死代码**，死代码是指那些 JVM 经过检查发现的根本不会使用到的代码。比如下面这个代码片段。

```java
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * <p>
 * 测试死代码消除
 *
 * @author niujinpeng
 * @Date 2020/8/21 8:04
 */
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
public class JmhDCE {

    @Benchmark
    public double test1() {
        return Math.log(Math.PI);
    }
    @Benchmark
    public void test2() {
        double result = Math.log(Math.PI);
        result = Math.log(result);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhDCE.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
```

在这个代码片段里里，`test1` 方法对圆周率进行对数计算，并返回计算结果；而 `test2` 中不仅对圆周率进行对数计算，还对计算的结果再次对数计算，看起来复杂一些，但是因为没有用到计算结果，所以 JVM 会自动消除这段代码， 因为它没有任何意义。

```shell
Benchmark     Mode  Cnt   Score    Error  Units
JmhDCE.test1  avgt    5   0.002 ±  0.001  us/op
JmhDCE.test2  avgt    5  ≈ 10⁻⁴           us/op
```

测试结果里也可以看到 `test` 平均耗时 0.0004 微秒，而 `test1` 平均耗时 0.002 微秒。

### 常量折叠 （Constant Folding）

在对 Java 源文件编译的过程中，编译器通过语法分析，可以发现某些能直接得到计算结果而不会再次更改的代码，然后会将计算结果记录下来，这样在执行的过程中就不需要再次运算了。比如这段代码。

```java
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * <p>
 * 测试常量折叠
 * 
 * @author niujinpeng
 * @Date 2020/8/21 8:23
 */
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
public class JmhConstantFolding {

    final double PI1 = 3.14159265358979323846;
    double PI2 = 3.14159265358979323846;

    @Benchmark
    public double test1() {
        return Math.log(PI1) * Math.log(PI1);
    }

    @Benchmark
    public double test2() {
        return Math.log(PI2) * Math.log(PI2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(JmhConstantFolding.class.getSimpleName()).build();
        new Runner(opt).run();
    }
}
```

`test`1 中使用 `final` 修饰的 PI1 进行对象计算，因为 PI1 不能再次更改，所以 `test1` 的计算结果必定是不会更改的，所以 JVM 会进行常量折叠优化，而 `test2` 使用的 `PI2` 可能会被修改，所以只能每次进行计算。

```shell
Benchmark                 Mode  Cnt  Score    Error  Units
JmhConstantFolding.test1  avgt    5  0.002 ±  0.001  us/op
JmhConstantFolding.test2  avgt    5  0.019 ±  0.001  us/op
```

可以看到 `test2` 耗时要多的多，达到了 0.019 微秒。

其实 JVM 做的优化操作远不止上面这些，还有比如常量传播（Constant Propagation）、循环展开（Loop Unwinding）、循环表达式外提（Loop Expression Hoisting）、消除公共子表达式（Common Subexpression Elimination）、本块重排序（Basic Block Reordering）、范围检查消除（Range Check Elimination）等。

## 总结

JMH 进行基准测试的使用过程并不复杂，同为 Java 虚拟机团队开发，准确性毋容置疑。但是在进行基准测试时还是要注意自己的代码问题，如果编写的要进行测试的代码本身存在问题，那么测试的结果必定是不准的。掌握了 JMH 基准测试之后，可以尝试测试一些常用的工具或者框架的性能如何，看看哪个工具的性能最好，比如 FastJSON 真的比 GSON 在进行 JSON 转换时更 Fast 吗？



**参考：**

- https://www.ibm.com/developerworks/cn/java/j-benchmark1.html

- http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
- 深入理解Java虚拟机：JVM高级特性与最佳实践（第3版）第11章 后端编译与优化



**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)
