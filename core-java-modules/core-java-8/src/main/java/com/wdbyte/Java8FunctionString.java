package com.wdbyte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author niulang
 * @date 2021/07/17
 */
public class Java8FunctionString {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java", "Nodejs", "Wdbyte.com");
        // 方法引用方式
        List<String> upperList = map(list, String::toUpperCase);
        List<String> lowerList = map(list, String::toLowerCase);
        System.out.println(upperList);
        System.out.println(lowerList);

        // Lambda 方式
        List<String> upperList2 = map(list, x -> x.toUpperCase());
        List<String> lowerList2 = map(list, x -> x.toLowerCase());
        System.out.println(upperList2);
        System.out.println(lowerList2);

    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
        List<R> resultList = new ArrayList<>(list.size());
        for (T t : list) {
            resultList.add(function.apply(t));
        }
        return resultList;
    }
}
