package com.wdbyte.enum2;

/**
 * @author niulang
 * @date 2023/05/01
 */
public class CalcTest {
    public static void main(String[] args) {
        // 加法
        int res = Calc.PLUS.apply(2, 3);
        System.out.println(res);
        // 减法
        res = Calc.MINUS.apply(2, 3);
        System.out.println(res);
        // 乘法
        res = Calc.MULTIPLY.apply(2, 3);
        System.out.println(res);
    }
}
