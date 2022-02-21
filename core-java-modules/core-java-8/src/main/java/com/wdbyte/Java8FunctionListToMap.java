package com.wdbyte;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/17
 */
public class Java8FunctionListToMap {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", "nodejs", "wdbyte.com");
        // lambda 方式
        Function<String, Integer> lengthFunction = str -> str.length();
        Map<String, Integer> listToMap = listToMap(list, lengthFunction);
        System.out.println(listToMap);

        // 方法引用方式
        Map<String, Integer> listToMap2 = listToMap(list, String::length);
        System.out.println(listToMap2);
    }

    public static <T, R> Map<T, R> listToMap(List<T> list, Function<T, R> function) {
        HashMap<T, R> hashMap = new HashMap<>();
        for (T t : list) {
            hashMap.put(t, function.apply(t));
        }
        return hashMap;
    }
}
