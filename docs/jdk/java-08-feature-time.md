---
title: Jdk14 都要出了，Jdk8 的时间处理姿势还不了解一下？
# toc: false
date: 2019-10-24 08:01:01
url: jdk/jdk8-time
tags:
 - Java8
 - LocalDateTime
 - LocalDate
categories:
 - Java 新特性
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

当前时间：2019年10月24日。距离 JDK 14 发布时间（2020年3月17日）还有多少天？

```java
// 距离JDK 14 发布还有多少天？
LocalDate jdk14 = LocalDate.of(2020, 3, 17);
LocalDate nowDate = LocalDate.now();
System.out.println("距离JDK 14 发布还有："+nowDate.until(jdk14,ChronoUnit.DAYS)+"天");
```
<!-- more -->
JDK 8 已经在 2014年 3月 18日正式可用 ，距离现在已经 5年多时间过去了。5年时间里很多企业也都换上了 JDK 8，明年 3月份 Jdk14 也要来了，那么 Jdk 8 的新特性你真的用起来了吗？我准备写一个 Jdk 8开始的新特性介绍以及使用的系列文章，后续 Jdk 也会继续更新，你如果需要的话不妨关注下博客或者公众号。

## 1. 时间处理类

Jdk8 带来了全新的时间处理工具类，用于代替之前存在缺陷的时间处理。新的时间处理相比之前更加简单好用。

![Jdk8 时间处理类](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1571847428464-1571848139345.png)

常用的类有以下几个类。

| 时间相关类        | 介绍                       |
| ----------------- | -------------------------- |
| LocalDateTime     | 时间处理类，最高精确到纳秒 |
| LocalDate         | 时间处理类，最高精确到天   |
| DateTimeFormatter | 时间格式化                 |
| ZoneId            | 时区设置类                 |

## 2. 时间获取

使用不同的类可以获取不同精度的时间。

```java
/**
 * 时间获取
*/
@Test
public void nowTimeTest() {
    // 当前精确时间
    LocalDateTime now = LocalDateTime.now();
    System.out.println("当前精确时间：" + now);
    System.out.println("当前精确时间：" + now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + " " + now.getHour() + "-" + now.getMinute() + "-" + now.getSecond());

    // 获取当前日期
    LocalDate localDate = LocalDate.now();
    System.out.println("当前日期：" + localDate);
    System.out.println("当前日期：" + localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth());

    // 获取当天时间
    LocalTime localTime = LocalTime.now();
    System.out.println("当天时间：" + localTime);
    System.out.println("当天时间：" + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond());

    // 有时区的当前精确时间
    ZonedDateTime nowZone = LocalDateTime.now().atZone(ZoneId.systemDefault());
    System.out.println("当前精确时间（有时区）：" + nowZone);
    System.out.println("当前精确时间（有时区）：" + nowZone.getYear() + "-" + nowZone.getMonthValue() + "-" + nowZone.getDayOfMonth() + " " + nowZone.getHour() + "-" + nowZone.getMinute() + "-" + nowZone.getSecond());
} 
```

获取到的时间：

```shell
当前精确时间：2019-10-24T00:26:41.724
当前精确时间：2019-10-24 0-26-41
当前日期：2019-10-24
当前日期：2019-10-24
当前精确时间（有时区）：2019-10-24T00:26:41.725+08:00[GMT+08:00]
当前精确时间（有时区）：2019-10-24 0-26-41
当天时间：00:26:41.725
当天时间：0:26:41
```

## 3. 时间创建

可以指定年月日时分秒创建一个时间类，也可以使用字符串直接转换成时间。

```java
/**
 * 时间创建
 */
@Test
public void createTime() {
    LocalDateTime ofTime = LocalDateTime.of(2019, 10, 1, 8, 8, 8);
    System.out.println("当前精确时间：" + ofTime);

    LocalDate localDate = LocalDate.of(2019, 10, 01);
    System.out.println("当前日期：" + localDate);

    LocalTime localTime = LocalTime.of(12, 01, 01);
    System.out.println("当天时间：" + localTime);
}
```

创建的时间：

```shell
当前精确时间：2019-10-01T08:08:08
当前日期：2019-10-01
当天时间：12:01:01
```

## 4. 时间转换

```java
/**
* 日期转换
*/
@Test
public void convertTimeTest() {
    LocalDateTime parseTime = LocalDateTime.parse("2019-10-01T22:22:22.222");
    System.out.println("字符串时间转换：" + parseTime);

    LocalDate formatted = LocalDate.parse("20190101", DateTimeFormatter.BASIC_ISO_DATE);
    System.out.println("字符串时间转换-指定格式：" + formatted);

    // Date 转换成 LocalDateTime
    Date date = new Date();
    ZoneId zoneId = ZoneId.systemDefault();
    System.out.println("Date 转换成 LocalDateTime：" + LocalDateTime.ofInstant(date.toInstant(), zoneId));

    // LocalDateTime 转换成 Date
    LocalDateTime localDateTime = LocalDateTime.now();
    Date toDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    System.out.println("LocalDateTime 转换成 Date：" + toDate);\
        
    // 当前时间转时间戳
    long epochMilli = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    System.out.println("当前时间转时间戳：" + epochMilli);
    // 时间戳转换成时间
    LocalDateTime epochMilliTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    System.out.println("时间戳转换成时间：" + epochMilliTime);
}
```

转换结果：

```shell
字符串时间转换：2019-10-01T22:22:22.222
字符串时间转换-指定格式：2019-01-01
Date 转换成 LocalDateTime：2019-10-24T00:46:41.251
LocalDateTime 转换成 Date：Thu Oct 24 00:46:41 GMT+08:00 2019
当前时间转时间戳：1571849201258
时间戳转换成时间：2019-10-24T00:46:41.258
```

## 5. 时间格式化

```java
/**
 * 日期格式化
 */
@Test
public void formatTest() {
    LocalDateTime now = LocalDateTime.now();
    System.out.println("当前时间：" + now);
    System.out.println("格式化后：" + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    System.out.println("格式化后：" + now.format(DateTimeFormatter.ISO_LOCAL_DATE));
    System.out.println("格式化后：" + now.format(DateTimeFormatter.ISO_LOCAL_TIME));
    System.out.println("格式化后：" + now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss")));
}
```

格式化后：

```shell
当前时间：2019-10-24T00:37:44.867
格式化后：2019-10-24T00:37:44.867
格式化后：2019-10-24
格式化后：00:37:44.867
格式化后：2019-10-24 12:37:44
```

## 6. 时间比较

```java
/**
 * 时间比较
 */
@Test
public void diffTest() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime yestory = now.minusDays(1);
    System.out.println(now + "在" + yestory + "之后吗?" + now.isAfter(yestory));
    System.out.println(now + "在" + yestory + "之前吗?" + now.isBefore(yestory));

    // 时间差
    long day = yestory.until(now, ChronoUnit.DAYS);
    long month = yestory.until(now, ChronoUnit.MONTHS);
    long hours = yestory.until(now, ChronoUnit.HOURS);
    long minutes = yestory.until(now, ChronoUnit.MINUTES);
    System.out.println("相差月份" + month);
    System.out.println("相差天数" + day);
    System.out.println("相差小时" + hours);
    System.out.println("相差分钟" + minutes);

    // 距离JDK 14 发布还有多少天？
    LocalDate jdk14 = LocalDate.of(2020, 3, 17);
    LocalDate nowDate = LocalDate.now();
    System.out.println("距离JDK 14 发布还有：" + nowDate.until(jdk14, ChronoUnit.DAYS) + "天");
}
```

比较结果：

```shell
2019-10-24T00:39:01.589在2019-10-23T00:39:01.589之后吗?true
2019-10-24T00:39:01.589在2019-10-23T00:39:01.589之前吗?false
相差月份0
相差天数1
相差小时24
相差分钟1440
距离JDK 14 发布还有：145天
```

## 7. 时间加减

```java
/**
 * 日期加减
 */
@Test
public void calcTest() {
    LocalDateTime now = LocalDateTime.now();
    System.out.println("当前时间："+now);
    LocalDateTime plusTime = now.plusMonths(1).plusDays(1).plusHours(1).plusMinutes(1).plusSeconds(1);
    System.out.println("增加1月1天1小时1分钟1秒时间后：" + plusTime);
    LocalDateTime minusTime = now.minusMonths(2);
    System.out.println("减少2个月时间后：" + minusTime);
}
```

操作结果：

```shell
当前时间：2019-10-24T00:41:08.877
增加1月1天1小时1分钟1秒时间后：2019-11-25T01:42:09.877
减少2个月时间后：2019-08-24T00:41:08.877	
```

## 8. 时间扩展方法

```java
/**
 * 时间方法
 */
@Test
public void timeFunctionTest() {
    LocalDateTime now = LocalDateTime.now();
    System.out.println("当前时间：" + now);
    // 第一天
    LocalDateTime firstDay = now.withDayOfMonth(1);
    System.out.println("本月第一天：" + firstDay);
    // 当天最后一秒
    LocalDateTime lastSecondOfDay = now.withHour(23).withMinute(59).withSecond(59);
    System.out.println("当天最后一秒：" + lastSecondOfDay);
    // 最后一天
    LocalDateTime lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
    System.out.println("本月最后一天:" + lastDay);
    // 是否闰年
    System.out.println("今年是否闰年：" + Year.isLeap(now.getYear()));
}
```

输出结果：

```java
当前时间：2019-10-24T00:43:28.296
本月第一天：2019-10-01T00:43:28.296
当天最后一秒：2019-10-24T23:59:59.296
本月最后一天:2019-10-31T00:43:28.296
今年是否闰年：false
```

Jdk 8 新的时间类使用起来相比之前显得更加方便简单。

![JDK8 之前时间处理](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1571850210772.png)

Jdk 8 也把时间处理成独立成一个包，并且使用不同的类名加以区分。而不是像之前相同的类名不同的包。这样的方式使用起来也更加清晰。

🚀 代码已经上传到 [Github(https://github.com/niumoo/jdk-feature)](https://github.com/niumoo/jdk-feature) 。


**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)