package com.wdbyte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author niulang
 * @date 2021/08/02
 */
public class Java8UnaryOperatorParam {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", "node", "c++", "rust", "www.wdbyte.com");
        UnaryOperator<String> upperFun = s -> s.toUpperCase();
        List<String> resultList = map(list, upperFun);
        System.out.println(resultList);

        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
        UnaryOperator<Integer> doubleInt = i -> i * 2;
        List<Integer> integers = map(intList, doubleInt);
        System.out.println(integers);
    }

    public static <T> List<T> map(List<T> list, UnaryOperator<T> unaryOperator) {
        List<T> resultList = new ArrayList<>();
        for (T t : list) {
            resultList.add(unaryOperator.apply(t));
        }
        return resultList;
    }
}
