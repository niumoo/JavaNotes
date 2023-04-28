package com.wdbyte.exception;

/**
 * @author niulang
 */
public class JavaException8 {

    public static void main(String[] args) {
        int result = 0;
        try {
            result = calc(4, 2);
            System.out.println("4 / 2 = " + result);
        } catch (MyException e) { // 不处理异常会报错
            e.printStackTrace();
        }

        try {
            result = calc(4, 0);
            System.out.println("4 / 0 = " + result);
        } catch (MyException e) { // 不处理异常会报错
            e.printStackTrace();
        }
    }

    public static int calc(int a, int b) throws MyException {
        if (b == 0) {
            throw new MyException("除数不能为0");
        }
        return a / b;
    }
}


