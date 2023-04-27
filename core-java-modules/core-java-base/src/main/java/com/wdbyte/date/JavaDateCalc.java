package com.wdbyte.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author niulang
 * @date 2023/04/26
 */
public class JavaDateCalc {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        System.out.println(String.format("当前时间：%s", sdf.format(date)));
        date = new JavaDateCalc().addDay(date, 3);
        System.out.println(String.format("增加三天：%s", sdf.format(date)));
        date = new JavaDateCalc().minDay(date, 1);
        System.out.println(String.format("减去一天：%s", sdf.format(date)));
        date = new JavaDateCalc().addMonth(date, 2);
        System.out.println(String.format("增加两个月：%s", sdf.format(date)));
    }

    /**
     * 增加天数
     *
     * @param date
     * @param addDay
     * @return
     */
    public Date addDay(Date date, int addDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, addDay);
        Date newDate = cal.getTime();
        return newDate;
    }

    /**
     * 减少天数
     *
     * @param date
     * @param minDay
     * @return
     */
    public Date minDay(Date date, int minDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -minDay);
        Date newDate = cal.getTime();
        return newDate;
    }

    /**
     * 增加月份
     *
     * @param date
     * @param addMonth
     * @return
     */
    public Date addMonth(Date date, int addMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, addMonth);
        Date newDate = cal.getTime();
        return newDate;
    }
}
