package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author niulang
 * @date 2021/07/19
 */
public class Java8PredicateNeagete {

    public static void main(String[] args) {
        List<Integer> numberList = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10);
        Predicate<Integer> greaterThan5 = number -> number > 5;

        numberList = numberList.stream().filter(greaterThan5.negate()).collect(Collectors.toList());
        System.out.println(numberList);
    }
}
