package com.wdbyte.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author niulang
 * @date 2023/04/26
 */
public class JavaDateCalc2 {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        System.out.println(String.format("当前时间：%s", sdf.format(date)));
        // 三天的毫秒数：3 * 24 * 3600 * 1000
        date = new Date(date.getTime() + 3 * 24 * 3600 * 1000);
        System.out.println(String.format("增加三天：%s", sdf.format(date)));

        date = new Date(date.getTime() - 1 * 60 * 1000);
        System.out.println(String.format("减去一分钟：%s", sdf.format(date)));
    }

}
