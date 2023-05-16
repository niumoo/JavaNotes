package com.wdbyte.exception;

/**
 * @author https://www.wdbyte.com
 */
public class JavaException3 {

    public static void main(String[] args) {
        String[] strArr = {"www", "wdbyte", "com"};
        try {
            System.out.println(strArr[0]);
            System.out.println(strArr[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("发生数组访问越界异常，不能访问超过数组长度的元素，数组长度为:" + strArr.length);
            System.out.println("异常描述:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
