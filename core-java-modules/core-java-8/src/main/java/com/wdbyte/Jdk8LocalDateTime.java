package com.wdbyte;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * JDK 8 date / time api
 * <p>
 * 优点：
 * <br/>
 * - 都是final 不可变类， 适用于多线程 <br/>
 * - 用法清晰，如都适用的now(),of(),parse(),format()方法<br/>
 * - 实现了大部分常用操作方法
 * </p>
 * 
*  @Author https://www.wdbyte.com
 * @Date 2019/2/19 17:48
 */
public class Jdk8LocalDateTime {

    /**
     * 时间获取
     * 
     * 
     */
    @Test
    public void nowTimeTest() {
        // 当前精确时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前精确时间：" + now);
        System.out.println("当前精确时间：" + now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + " "
            + now.getHour() + "-" + now.getMinute() + "-" + now.getSecond());

        // 获取当前日期
        LocalDate localDate = LocalDate.now();
        System.out.println("当前日期：" + localDate);
        System.out
            .println("当前日期：" + localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth());

        // 获取当天时间
        LocalTime localTime = LocalTime.now();
        System.out.println("当天时间：" + localTime);
        System.out.println("当天时间：" + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond());

        // 有时区的当前精确时间
        ZonedDateTime nowZone = LocalDateTime.now().atZone(ZoneId.systemDefault());
        System.out.println("当前精确时间（有时区）：" + nowZone);
        System.out
            .println("当前精确时间（有时区）：" + nowZone.getYear() + "-" + nowZone.getMonthValue() + "-" + nowZone.getDayOfMonth()
                + " " + nowZone.getHour() + "-" + nowZone.getMinute() + "-" + nowZone.getSecond());
    }

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
        System.out.println("LocalDateTime 转换成 Date：" + toDate);

        // 当前时间转时间戳
        long epochMilli = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println("当前时间转时间戳：" + epochMilli);
        // 时间戳转换成时间
        LocalDateTime epochMilliTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
        System.out.println("时间戳转换成时间：" + epochMilliTime);
    }

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

    /**
     * 日期加减
     */
    @Test
    public void calcTest() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间：" + now);
        LocalDateTime plusTime = now.plusMonths(1).plusDays(1).plusHours(1).plusMinutes(1).plusSeconds(1);
        System.out.println("增加1月1天1小时1分钟1秒时间后：" + plusTime);
        LocalDateTime minusTime = now.minusMonths(2);
        System.out.println("减少2个月时间后：" + minusTime);
    }

    /**
     * 时间方法
     */
    @Test
    public void timeFunctionTest() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间：" + now);

        // LocalDateTime 本月第一天
        // 方法1
        LocalDateTime firstDay = now.withDayOfMonth(1);
        System.out.println("本月第一天：" + firstDay);
        // 方法2
        firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
        System.out.println("本月第一天：" + firstDay);

        // LocalDateTime 本月最后一天
        LocalDateTime lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("本月最后一天:" + lastDay);


        // LocalDateTime 当天最后一秒
        // 方法1
        LocalDateTime lastSecondOfDay1 = now.withHour(23).withMinute(59).withSecond(59);
        System.out.println("当天最后一秒：" + lastSecondOfDay1);
        // 方法2
        LocalDateTime lastSecondOfDay2 = LocalDateTime.now().with(LocalTime.MAX);
        System.out.println("当天最后一秒：" + lastSecondOfDay2);


        // 是否闰年
        System.out.println("今年是否闰年：" + Year.isLeap(now.getYear()));
    }

}
