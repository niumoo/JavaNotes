package com.wdbyte;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author niulang
 * @date 2021/07/26
 */
public class Java8BiFunctionAndThen {

    public static void main(String[] args) {
        // 两个字符串长度和
        BiFunction<String, String, Integer> lengthBiFun = (s1, s2) -> s1.length() + s2.length();
        Function<Integer, String> function = s -> "长度和:" + s;

        String result = lengthBiFun.andThen(function).apply("java", "www.byte.com");
        System.out.println(result);
    }
}
