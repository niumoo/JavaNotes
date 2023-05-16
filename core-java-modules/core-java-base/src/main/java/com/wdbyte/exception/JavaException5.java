package com.wdbyte.exception;

import org.apache.commons.lang3.ObjectUtils.Null;

/**
 * @author https://www.wdbyte.com
 */
public class JavaException5 {

    public static void main(String[] args) {
        String[] strArr = {"www", null, "com"};
        try {
            System.out.println(strArr[0].length());
            // 空指针异常
            System.out.println(strArr[1].length());
            System.out.println(strArr[3].length());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("发生数组访问越界异常，不能访问超过数组长度的元素，数组长度为:" + strArr.length);
        } catch (NullPointerException e) {
            System.out.println("发现空指针异常");
        }
        System.out.println("执行完成。");

        try {
            System.out.println(strArr[0].length());
            // 空指针异常
            System.out.println(strArr[1].length());
            // 越界异常
            System.out.println(strArr[3].length());
        } catch (Exception e) {
            System.out.println("发生异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
