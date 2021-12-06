package com.wdbyte;

import java.util.Arrays;
import java.util.List;

/**
@website https://www.wdbyte.com
 */
public class Java8ForEachList {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", "nodejs", "c++", "wdbyte.com");
        // 方法引用
        list.forEach(System.out::println);
        System.out.println("-------------");
        // lambda
        list.forEach(s -> System.out.println(s));
    }
}
