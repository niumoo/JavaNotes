package com.wdbyte.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/25
 */
public class JavaDateFormat {

    public static void main(String[] args) throws ParseException {
        Date date = new Date();

        // 时间格式化，yyyy 年份，MM 月份， dd 当月第多少天, hh:mm:ss 分别为时分秒
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formatDate = simpleDateFormat.format(date);
        System.out.println(formatDate);

        SimpleDateFormat sdf1 = new SimpleDateFormat("当前日期是: yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("当前时间是: hh:mm:ss");
        System.out.println(sdf1.format(date));
        System.out.println(sdf2.format(date));


        String strDate = "2023-01-19 10:30:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date myDate = sdf.parse(strDate);
        System.out.println(sdf.format(myDate));
    }
}
