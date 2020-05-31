---
title: 还看不懂同事的代码？超强的 Stream 流操作姿势还不学习一下
# toc: false
date: 2019-11-18 09:00:00
url: jdk/jdk8-stream
tags:
 - Stream
 - Java8
categories:
 - Java 新特性
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![java-streams](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/java-streams.png)

Java 8 新特性系列文章索引。

1.  [Jdk14都要出了，还不能使用 Optional优雅的处理空指针？](http://mp.weixin.qq.com/s?__biz=MzI1MDIxNjQ1OQ==&mid=2247483879&idx=1&sn=1eb37f5a97fda31ebb9d80d6e96cfb88&chksm=e984e883def361957df3a954b0f28775404b5f0a278958e91c65b3f3175a37f5384b0988c564&scene=21#wechat_redirect) 
2.  [Jdk14 都要出了，Jdk8 的时间处理姿势还不了解一下？](http://mp.weixin.qq.com/s?__biz=MzI1MDIxNjQ1OQ==&mid=2247483801&idx=1&sn=eea69b039feb1ae86187ade222e6bfd8&chksm=e984e8fddef361ebd4acc58e11f3ccdeea9b06b6514957a5c203d52046c551f3ea3203d187e9&scene=21#wechat_redirect) 
3.  [还看不懂同事的代码？Lambda 表达式、函数接口了解一下](http://mp.weixin.qq.com/s?__biz=MzI1MDIxNjQ1OQ==&mid=2247483923&idx=1&sn=57c720a9ba7dbd79e84a069e0d6fa84f&chksm=e984eb77def36261de6f0b9edd8aaa9ef34f74f90c08e6d395880545b6bf1a7b054ecdbff483#rd) 

## 前言

我们都知道 `Lambda` 和 Stream 是 Java 8 的两大亮点功能，在前面的文章里已经介绍过 `Lambda` 相关知识，这次介绍下 Java 8 的 Stream 流操作。它完全不同于 java.io 包的 Input/Output Stream ，也不是大数据实时处理的 Stream 流。这个 Stream 流操作是 Java 8 对集合操作功能的增强，专注于对集合的各种高效、便利、优雅的**聚合操作**。借助于 `Lambda` 表达式，显著的提高**编程效率**和**可读性**。且 Stream 提供了**并行计算**模式，可以简洁的编写出并行代码，能充分发挥如今计算机的多核处理优势。

<!-- more -->

在使用 Stream 流操作之前你应该先了解 `Lambda` 相关知识，如果还不了解，可以参考之前文章：[还看不懂同事的代码？Lambda 表达式、函数接口了解一下](http://mp.weixin.qq.com/s?__biz=MzI1MDIxNjQ1OQ==&mid=2247483923&idx=1&sn=57c720a9ba7dbd79e84a069e0d6fa84f&chksm=e984eb77def36261de6f0b9edd8aaa9ef34f74f90c08e6d395880545b6bf1a7b054ecdbff483#rd) 。

## 1. Stream 流介绍

Stream 不同于其他集合框架，它也不是某种数据结构，也不会保存数据，但是它负责相关计算，使用起来更像一个高级的迭代器。在之前的迭代器中，我们只能先遍历然后在执行业务操作，而现在只需要指定执行什么操作， Stream 就会隐式的遍历然后做出想要的操作。另外 Stream 和迭代器一样的只能单向处理，如同奔腾长江之水一去而不复返。 

由于 Stream 流提供了**惰性计算**和**并行处理**的能力，在使用并行计算方式时数据会被自动分解成多段然后并行处理，最后将结果汇总。所以 Stream 操作可以让程序运行变得更加高效。

## 2. Stream 流概念

Stream 流的使用总是按照一定的步骤进行，可以抽象出下面的使用流程。

> 数据源（source） -> 数据处理/转换（intermedia） -> 结果处理（terminal ）

### 2.1. 数据源

`数据源（source）`也就是数据的来源，可以通过多种方式获得 Stream 数据源，下面列举几种常见的获取方式。

-   Collection.stream();  从集合获取流。
-   Collection.parallelStream();  从集合获取**并行流。**
-   Arrays.stream(T array) or Stream.of(); 从数组获取流。
-   BufferedReader.lines(); 从输入流中获取流。
-   IntStream.of() ; 从静态方法中获取流。
-   Stream.generate(); 自己生成流

### 2.2. 数据处理

`数据处理/转换（intermedia）`步骤可以有多个操作，这步也被称为`intermedia`（中间操作）。在这个步骤中不管怎样操作，它返回的都是一个新的流对象，原始数据不会发生任何改变，而且这个步骤是`惰性计算`处理的，也就是说只调用方法并不会开始处理，只有在真正的开始收集结果时，中间操作才会生效，而且如果遍历没有完成，想要的结果已经获取到了（比如获取第一个值），会停止遍历，然后返回结果。`惰性计算`可以显著提高运行效率。

数据处理演示。

```java
@Test
public void streamDemo(){
    List<String> nameList = Arrays.asList("Darcy", "Chris", "Linda", "Sid", "Kim", "Jack", "Poul", "Peter");
    // 1. 筛选出名字长度为4的
    // 2. 名字前面拼接 This is
    // 3. 遍历输出
    nameList.stream()
            .filter(name -> name.length() == 4)
            .map(name -> "This is "+name)
            .forEach(name -> System.out.println(name));
}
// 输出结果
// This is Jack
// This is Poul
```

`数据处理/转换`操作自然不止是上面演示的过滤 `filter` 和 `map`映射两种，另外还有 map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered 等。

### 2.3. 收集结果

`结果处理（terminal ）`是流处理的最后一步，执行完这一步之后流会被彻底用尽，流也不能继续操作了。也只有到了这个操作的时候，流的`数据处理/转换`等中间过程才会开始计算，也就是上面所说的`惰性计算`。`结果处理`也必定是流操作的最后一步。

常见的``结果处理``操作有 forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator 等。

下面演示了简单的``结果处理``的例子。

```java
/**
 * 转换成为大写然后收集结果，遍历输出
 */
@Test
public void toUpperCaseDemo() {
    List<String> nameList = Arrays.asList("Darcy", "Chris", "Linda", "Sid", "Kim", "Jack", "Poul", "Peter");
    List<String> upperCaseNameList = nameList.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    upperCaseNameList.forEach(name -> System.out.println(name + ","));
}
// 输出结果
// DARCY,CHRIS,LINDA,SID,KIM,JACK,POUL,PETER,
```

### 2.4. short-circuiting

有一种 Stream 操作被称作 `short-circuiting` ，它是指当 Stream 流**无限大**但是需要返回的 Stream 流是**有限**的时候，而又希望它能在**有限的时间**内计算出结果，那么这个操作就被称为`short-circuiting`。例如　`findFirst　`操作。

## 3. Stream 流使用

Stream 流在使用时候总是借助于 `Lambda` 表达式进行操作，Stream 流的操作也有很多种方式，下面列举的是常用的 11 种操作。

### 3.1. Stream 流获取

获取 Stream 的几种方式在上面的 Stream 数据源里已经介绍过了，下面是针对上面介绍的几种获取 Stream 流的使用示例。

```java
@Test
public void createStream() throws FileNotFoundException {
    List<String> nameList = Arrays.asList("Darcy", "Chris", "Linda", "Sid", "Kim", "Jack", "Poul", "Peter");
    String[] nameArr = {"Darcy", "Chris", "Linda", "Sid", "Kim", "Jack", "Poul", "Peter"};
    // 集合获取 Stream 流
    Stream<String> nameListStream = nameList.stream();
    // 集合获取并行 Stream 流
    Stream<String> nameListStream2 = nameList.parallelStream();
    // 数组获取 Stream 流
    Stream<String> nameArrStream = Stream.of(nameArr);
    // 数组获取 Stream 流
    Stream<String> nameArrStream1 = Arrays.stream(nameArr);
    // 文件流获取 Stream 流
    BufferedReader bufferedReader = new BufferedReader(new FileReader("README.md"));
    Stream<String> linesStream = bufferedReader.lines();
    // 从静态方法获取流操作
    IntStream rangeStream = IntStream.range(1, 10);
    rangeStream.limit(10).forEach(num -> System.out.print(num+","));
    System.out.println();
    IntStream intStream = IntStream.of(1, 2, 3, 3, 4);
    intStream.forEach(num -> System.out.print(num+","));
}
```

### 3.2. forEach

``forEach`` 是 `Stream` 流中的一个重要方法，用于遍历 `Stream` 流，它支持传入一个标准的 `Lambda` 表达式。但是它的遍历不能通过 return/break 进行终止。同时它也是一个 `terminal` 操作，执行之后 `Stream` 流中的数据会被消费掉。

如输出对象。

```java
List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
numberList.stream().forEach(number -> System.out.println(number+","));
// 输出结果
// 1,2,3,4,5,6,7,8,9,
```

### 3.3. map / flatMap

使用 `map` 把对象一对一映射成另一种对象或者形式。

```java
/**
 * 把数字值乘以2
 */
@Test
public void mapTest() {
    List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    // 映射成 2倍数字
    List<Integer> collect = numberList.stream()
            .map(number -> number * 2)
            .collect(Collectors.toList());
    collect.forEach(number -> System.out.print(number + ","));
    System.out.println();

    numberList.stream()
            .map(number -> "数字 " + number + ",")
            .forEach(number -> System.out.println(number));
}
// 输出结果
// 2,4,6,8,10,12,14,16,18,
// 数字 1,数字 2,数字 3,数字 4,数字 5,数字 6,数字 7,数字 8,数字 9,
```

上面的 `map` 可以把数据进行一对一的映射，而有些时候关系可能不止 1对 1那么简单，可能会有1对多。这时可以使用 `flatMap。下面演示`使用 `flatMap`把对象扁平化展开。

```java
/**
 * flatmap把对象扁平化
 */
@Test
public void flatMapTest() {
    Stream<List<Integer>> inputStream = Stream.of(
            Arrays.asList(1),
            Arrays.asList(2, 3),
            Arrays.asList(4, 5, 6)
    );
    List<Integer> collect = inputStream
            .flatMap((childList) -> childList.stream())
            .collect(Collectors.toList());
    collect.forEach(number -> System.out.print(number + ","));
}
// 输出结果
// 1,2,3,4,5,6,
```

### 3.4. filter

使用 `filter` 进行数据筛选，挑选出想要的元素，下面的例子演示怎么挑选出偶数数字。

```java
/**
 * filter 数据筛选
 * 筛选出偶数数字
 */
@Test
public void filterTest() {
    List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    List<Integer> collect = numberList.stream()
            .filter(number -> number % 2 == 0)
            .collect(Collectors.toList());
    collect.forEach(number -> System.out.print(number + ","));
}
```

得到如下结果。

```shell
2,4,6,8,
```

### 3.5. findFirst

`findFirst` 可以查找出 `Stream` 流中的第一个元素，它返回的是一个 Optional 类型，如果还不知道 Optional 类的用处，可以参考之前文章 [Jdk14都要出了，还不能使用 Optional优雅的处理空指针？](http://mp.weixin.qq.com/s?__biz=MzI1MDIxNjQ1OQ==&mid=2247483879&idx=1&sn=1eb37f5a97fda31ebb9d80d6e96cfb88&chksm=e984e883def361957df3a954b0f28775404b5f0a278958e91c65b3f3175a37f5384b0988c564&scene=21#wechat_redirect) 。

```java
/**
 * 查找第一个数据
 * 返回的是一个 Optional 对象
 */
@Test
public void findFirstTest(){
    List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    Optional<Integer> firstNumber = numberList.stream()
            .findFirst();
    System.out.println(firstNumber.orElse(-1));
}
// 输出结果
// 1
```

`findFirst` 方法在查找到需要的数据之后就会返回**不再遍历**数据了，也因此 `findFirst` 方法可以对有无限数据的  `Stream` 流进行操作，也可以说 `findFirst` 是一个 `short-circuiting` 操作。

### 3.6. collect / toArray

`Stream` 流可以轻松的转换为其他结构，下面是几种常见的示例。

```java
 /**
 * Stream 转换为其他数据结构
 */
@Test
public void collectTest() {
    List<Integer> numberList = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5);
    // to array
    Integer[] toArray = numberList.stream()
            .toArray(Integer[]::new);
    // to List
    List<Integer> integerList = numberList.stream()
            .collect(Collectors.toList());
    // to set
    Set<Integer> integerSet = numberList.stream()
            .collect(Collectors.toSet());
    System.out.println(integerSet);
    // to string
    String toString = numberList.stream()
            .map(number -> String.valueOf(number))
            .collect(Collectors.joining()).toString();
    System.out.println(toString);
    // to string split by ,
    String toStringbJoin = numberList.stream()
            .map(number -> String.valueOf(number))
            .collect(Collectors.joining(",")).toString();
    System.out.println(toStringbJoin);
}
// 输出结果
// [1, 2, 3, 4, 5]
// 112233445
// 1,1,2,2,3,3,4,4,5
```

### 3.7. limit / skip

获取或者扔掉前 n 个元素

```java
/**
 * 获取 / 扔掉前 n 个元素
 */
@Test
public void limitOrSkipTest() {
    // 生成自己的随机数流
    List<Integer> ageList = Arrays.asList(11, 22, 13, 14, 25, 26);
    ageList.stream()
            .limit(3)
            .forEach(age -> System.out.print(age+","));
    System.out.println();
    
    ageList.stream()
            .skip(3)
            .forEach(age -> System.out.print(age+","));
}
// 输出结果
// 11,22,13,
// 14,25,26,
```

### 3.8. Statistics 

数学统计功能，求一组数组的最大值、最小值、个数、数据和、平均数等。

```java
/**
 * 数学计算测试
 */
@Test
public void mathTest() {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
    IntSummaryStatistics stats = list.stream().mapToInt(x -> x).summaryStatistics();
    System.out.println("最小值：" + stats.getMin());
    System.out.println("最大值：" + stats.getMax());
    System.out.println("个数：" + stats.getCount());
    System.out.println("和：" + stats.getSum());
    System.out.println("平均数：" + stats.getAverage());
}
// 输出结果
// 最小值：1
// 最大值：6
// 个数：6
// 和：21
// 平均数：3.5
```

### 3.9. groupingBy 

分组聚合功能，和数据库的 Group by 的功能一致。

```java
/**
 * groupingBy
 * 按年龄分组
 */
@Test
public void groupByTest() {
    List<Integer> ageList = Arrays.asList(11, 22, 13, 14, 25, 26);
    Map<String, List<Integer>> ageGrouyByMap = ageList.stream()            
        .collect(Collectors.groupingBy(age -> String.valueOf(age / 10)));
    ageGrouyByMap.forEach((k, v) -> {
        System.out.println("年龄" + k + "0多岁的有：" + v);
    });
}
// 输出结果
// 年龄10多岁的有：[11, 13, 14]
// 年龄20多岁的有：[22, 25, 26]
```

### 3.10. partitioningBy

```java
/**
 * partitioningBy
 * 按某个条件分组
 * 给一组年龄，分出成年人和未成年人
 */
public void partitioningByTest() {
    List<Integer> ageList = Arrays.asList(11, 22, 13, 14, 25, 26);
    Map<Boolean, List<Integer>> ageMap = ageList.stream()
            .collect(Collectors.partitioningBy(age -> age > 18));
    System.out.println("未成年人：" + ageMap.get(false));
    System.out.println("成年人：" + ageMap.get(true));
}
// 输出结果
// 未成年人：[11, 13, 14]
// 成年人：[22, 25, 26]
```

### 3.11. 进阶 - 自己生成 Stream 流

```java
/**
 * 生成自己的 Stream 流
 */
@Test
public void generateTest(){
    // 生成自己的随机数流
    Random random = new Random();
    Stream<Integer> generateRandom = Stream.generate(random::nextInt);
    generateRandom.limit(5).forEach(System.out::println);
    // 生成自己的 UUID 流
    Stream<UUID> generate = Stream.generate(UUID::randomUUID);
    generate.limit(5).forEach(System.out::println);
}

// 输出结果
// 793776932
// -2051545609
// -917435897
// 298077102
// -1626306315
// 31277974-841a-4ad0-a809-80ae105228bd
// f14918aa-2f94-4774-afcf-fba08250674c
// d86ccefe-1cd2-4eb4-bb0c-74858f2a7864
// 4905724b-1df5-48f4-9948-fa9c64c7e1c9
// 3af2a07f-0855-455f-a339-6e890e533ab3
```

上面的例子中 `Stream` 流是无限的，但是获取到的结果是有限的，使用了 `Limit` 限制获取的数量，所以这个操作也是 `short-circuiting` 操作。

## 4. Stream 流优点

### 4.1. 简洁优雅

正确使用并且**正确格式化**的 `Stream` 流操作代码不仅**简洁优雅**，更让人赏心悦目。下面对比下在使用 `Stream` 流和不使用 `Stream` 流时相同操作的编码风格。

```java
/**
 * 使用流操作和不使用流操作的编码风格对比
 */
@Test
public void diffTest() {
    // 不使用流操作
    List<String> names = Arrays.asList("Jack", "Jill", "Nate", "Kara", "Kim", "Jullie", "Paul", "Peter");
    // 筛选出长度为4的名字
    List<String> subList = new ArrayList<>();
    for (String name : names) {
        if (name.length() == 4) {
            subList.add(name);
        }
    }
    // 把值用逗号分隔
    StringBuilder sbNames = new StringBuilder();
    for (int i = 0; i < subList.size() - 1; i++) {
        sbNames.append(subList.get(i));
        sbNames.append(", ");
    }
    // 去掉最后一个逗号
    if (subList.size() > 1) {
        sbNames.append(subList.get(subList.size() - 1));
    }
    System.out.println(sbNames);
}
// 输出结果
// Jack, Jill, Nate, Kara, Paul
```

如果是使用 `Stream` 流操作。

```java
// 使用 Stream 流操作
String nameString = names.stream()
       .filter(num -> num.length() == 4)
       .collect(Collectors.joining(", "));
System.out.println(nameString);
```

### 4.2. 惰性计算

上面有提到，`数据处理/转换（intermedia）` 操作 map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered 等这些操作，在调用方法时并不会立即调用，而是在真正使用的时候才会生效，这样可以让操作延迟到真正需要使用的时刻。

下面会举个例子演示这一点。

```java
 /**
  * 找出偶数
  */
 @Test
 public void lazyTest() {
     // 生成自己的随机数流
     List<Integer> numberLIst = Arrays.asList(1, 2, 3, 4, 5, 6);
     // 找出偶数
     Stream<Integer> integerStream = numberLIst.stream()
             .filter(number -> {
                 int temp = number % 2;
     			 if (temp == 0 ){
                     System.out.println(number);
                 }
                 return temp == 0;
             });

     System.out.println("分割线");
     List<Integer> collect = integerStream.collect(Collectors.toList());
 }
```

如果没有 `惰性计算`，那么很明显会先输出偶数，然后输出 `分割线`。而实际的效果是。

```shell
分割线
2
4
6
```

可见 `惰性计算` 把计算延迟到了真正需要的时候。

### 4.3. 并行计算

获取 `Stream` 流时可以使用 `parallelStream` 方法代替 `stream` 方法以获取并行处理流，并行处理可以充分的发挥多核优势，而且不增加编码的复杂性。

下面的代码演示了生成一千万个随机数后，把每个随机数乘以2然后求和时，串行计算和并行计算的耗时差异。

```java
  /**
  * 并行计算
  */
 @Test
 public void main() {
     // 生成自己的随机数流，取一千万个随机数
     Random random = new Random();
     Stream<Integer> generateRandom = Stream.generate(random::nextInt);
     List<Integer> numberList = generateRandom.limit(10000000).collect(Collectors.toList());

     // 串行 - 把一千万个随机数，每个随机数 * 2 ，然后求和
     long start = System.currentTimeMillis();
     int sum = numberList.stream()
         .map(number -> number * 2)
         .mapToInt(x -> x)
         .sum();
     long end = System.currentTimeMillis();
     System.out.println("串行耗时："+(end - start)+"ms，和是:"+sum);

     // 并行 - 把一千万个随机数，每个随机数 * 2 ，然后求和
     start = System.currentTimeMillis();
     sum = numberList.parallelStream()
         .map(number -> number * 2)
         .mapToInt(x -> x)
         .sum();
     end = System.currentTimeMillis();
     System.out.println("并行耗时："+(end - start)+"ms，和是:"+sum);
 }
```

得到如下输出。

```java
串行耗时：1005ms，和是:481385106
并行耗时：47ms，和是:481385106
```

效果显而易见，代码简洁优雅。

## 5. Stream 流建议

### 5.1 保证正确排版

从上面的使用案例中，可以发现使用 `Stream` 流操作的代码非常简洁，而且可读性更高。但是如果不正确的排版，那么看起来将会很糟糕，比如下面的同样功能的代码例子，多几层操作呢，是不是有些让人头大？

```java
// 不排版
String string = names.stream().filter(num -> num.length() == 4).map(name -> name.toUpperCase()).collect(Collectors.joining(","));
// 排版
String string = names.stream()
        .filter(num -> num.length() == 4)
        .map(name -> name.toUpperCase())
        .collect(Collectors.joining(","));
```

### 5.2 保证函数纯度

如果想要你的 `Stream` 流对于每次的相同操作的结果都是相同的话，那么你必须保证 `Lambda` 表达式的纯度，也就是下面两点。

- Lambda 中不会更改任何元素。
- Lambda 中不依赖于任何可能更改的元素。

这两点对于保证函数的幂等非常重要，不然你程序执行结果可能会变得难以预测，就像下面的例子。

```java
@Test
public void simpleTest(){
    List<Integer> numbers = Arrays.asList(1, 2, 3);
    int[] factor = new int[] { 2 };
    Stream<Integer> stream = numbers.stream()
            .map(e -> e * factor[0]);
    factor[0] = 0;
    stream.forEach(System.out::println);
}
// 输出结果
// 0
// 0
// 0
```



文中代码都已经上传到 [Github.com/niumoo/jdk-feature ](https://github.com/niumoo/jdk-feature)。


**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)