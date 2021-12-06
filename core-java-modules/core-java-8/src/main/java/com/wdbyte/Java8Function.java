package com.wdbyte;

import java.util.function.Function;

/**
 * @author niulang
 * @website https://www.wdbyte.com
 * @date 2021/07/17
 */
public class Java8Function {
    public static void main(String[] args) {
        Function<String, String> toUpperCase = str -> str.toUpperCase();
        String result = toUpperCase.apply("www.wdbyte.com");
        System.out.println(result);
    }
}
