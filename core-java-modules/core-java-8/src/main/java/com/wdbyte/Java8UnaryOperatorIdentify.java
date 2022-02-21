package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author https://www.wdbyte.com
 * @date 2021/08/02
 */
public class Java8UnaryOperatorIdentify {

    public static void main(String[] args) {
        Function<String, String> upperFun1 = s -> s.toUpperCase();
        UnaryOperator<String> upperFun2 = s -> s.toUpperCase();

        List<String> list = Arrays.asList("java", "node", "c++", "rust", "www.wdbyte.com");

        Map<String, String> map1 = list.stream()
            .collect(Collectors.toMap(upperFun1::apply, Function.identity()));

        Map<String, String> map2 = list.stream()
            .collect(Collectors.toMap(upperFun2::apply, UnaryOperator.identity()));

        Map<String, String> map3 = list.stream()
            .collect(Collectors.toMap(upperFun2::apply, t -> t));

        System.out.println(map1);
        System.out.println(map2);
        System.out.println(map3);
    }
}
