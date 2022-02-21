package com.wdbyte;

import java.util.function.Function;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/17
 */
public class Java8FunctionLength {
    public static void main(String[] args) {
        Function<String, Integer> lengthFunction = str -> str.length();
        Integer length = lengthFunction.apply("www.wdbyte.com");
        System.out.println(length);
    }
}
