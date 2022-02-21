package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/19
 */
public class Java8PredicateAnd {

    public static void main(String[] args) {
        List<Integer> numberList = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10);
        numberList = numberList.stream().filter(x -> x > 5 && x < 9).collect(Collectors.toList());
        System.out.println(numberList);

        Predicate<Integer> greaterThan5 = number -> number > 5;
        Predicate<Integer> lessThan9 = number -> number < 9;
        Predicate<Integer> filter = greaterThan5.and(lessThan9);

        numberList = numberList.stream().filter(filter).collect(Collectors.toList());
        System.out.println(numberList);
    }
}
