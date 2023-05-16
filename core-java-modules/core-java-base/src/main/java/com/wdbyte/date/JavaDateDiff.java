package com.wdbyte.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/25
 */
public class JavaDateDiff {

    public static void main(String[] args) throws InterruptedException {
        // 获取当前时间
        Date date1 = new Date();
        // 休眠 3 秒
        Thread.sleep(3000);
        // 再获取一次当前时间
        Date date2 = new Date();

        // 很明显 date1 在 date2 之前，,所以 true
        boolean before = date1.before(date2);
        System.out.println(before); // true

        // 很明显 date2 在 date1 之后,所以 true
        boolean after = date2.after(date1);
        System.out.println(after); // true

        // date2 不在 date1 之前,所以 false
        System.out.println(date2.before(date1)); // false

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("date1："+sdf.format(date1));
        System.out.println("date2："+sdf.format(date2));
        Long diff = date1.getTime() - date2.getTime();
        if (diff > 0) {
            System.out.println("date1 > date2");
        }
        if (diff < 0) {
            System.out.println("date1 < date2");
        }
        if (diff == 0) {
            System.out.println("date1 = date2");
        }
        exec();
    }

    public static void exec() throws InterruptedException {
        long start = System.currentTimeMillis();
        // 做点什么
        Thread.sleep(3000);
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start) + "ms");
    }
}
