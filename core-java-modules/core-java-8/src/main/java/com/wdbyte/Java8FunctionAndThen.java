package com.wdbyte;

import java.util.function.Function;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/17
 */
public class Java8FunctionAndThen {
    public static void main(String[] args) {
        Function<String, Integer> lengthFunction = str -> str.length();
        Function<Integer, Integer> doubleFunction = length -> length * 2;
        Integer doubleLength = lengthFunction.andThen(doubleFunction).apply("www.wdbyte.com");
        System.out.println(doubleLength);
    }
}
