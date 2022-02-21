package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/19
 */
public class Java8PredicateOr {

    public static void main(String[] args) {
        List<Integer> numberList = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10);

        Predicate<Integer> lessThan5 = number -> number <= 5;
        Predicate<Integer> greaterThan8 = number -> number >= 9;

        numberList = numberList.stream().filter(lessThan5.or(greaterThan8)).collect(Collectors.toList());
        System.out.println(numberList);
    }
}
