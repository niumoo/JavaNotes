package com.wdbyte.date;

import java.util.Date;

/**
 * @author niulang
 * @date 2023/04/25
 */
public class JavaDateCreate {
    public static void main(String[] args) {
        Date date = new Date();
        // 输出时间
        System.out.println(date); // Tue Apr 25 20:28:23 CST 2023
        // 输出毫秒数
        System.out.println(date.getTime()); // 1682425703429

        // 当前毫秒数创建对象
        //Date date2 = new Date(System.currentTimeMillis());
        Date date2 = new Date(1682425703429L);
        System.out.println(date2); // Tue Apr 25 20:28:23 CST 2023
    }
}
