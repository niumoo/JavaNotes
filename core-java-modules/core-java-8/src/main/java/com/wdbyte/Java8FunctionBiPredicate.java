package com.wdbyte;

import java.util.function.BiPredicate;

/**
 * @author niulang
 * @date 2021/08/01
 */
public class Java8FunctionBiPredicate {

    public static void main(String[] args) {
        // 判断字符串的长度是否是指定长度
        BiPredicate<String, Integer> biFunction = (s, i) -> s.length() == i;
        System.out.println(biFunction.test("java", 3));
        System.out.println(biFunction.test("java", 4));
        System.out.println(biFunction.test("www.wdbyte.com", 10));
        System.out.println(biFunction.test("www.wdbyte.com", 14));
    }
}
