package com.wdbyte;

import java.util.function.Predicate;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/19
 */
public class Java8PredicateTest {
    public static void main(String[] args) {
        Predicate<String> isEmpty = String::isEmpty;
        System.out.println(isEmpty.test(""));
        System.out.println(isEmpty.test("www.wdbyte.com"));
    }
}
