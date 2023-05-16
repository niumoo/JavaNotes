package com.wdbyte.exception;

/**
 * @author https://www.wdbyte.com
 */
public class JavaException4 {

    public static void main(String[] args) {
        String[] strArr = {"www", "wdbyte", "com"};
        try {
            System.out.println(strArr[0]);
            System.out.println(strArr[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            //throw e;
            throw new RuntimeException(e);
        }
        System.out.println("执行完成。");
    }
}
