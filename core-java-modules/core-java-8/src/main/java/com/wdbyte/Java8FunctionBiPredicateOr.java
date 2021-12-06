package com.wdbyte;

import java.util.function.BiPredicate;

/**
 * @author niulang
 * @date 2021/08/01
 */
public class Java8FunctionBiPredicateOr {

    public static void main(String[] args) {
        BiPredicate<String, String> startPredicate = (s1, s2) -> s1.startsWith(s2);
        BiPredicate<String, String> endPredicate = (s1, s2) -> s1.endsWith(s2);

        boolean test = startPredicate.and(endPredicate).test("wdbyte", "w");
        System.out.println(test);
        boolean test1 = startPredicate.and(endPredicate).test("wsw", "w");
        System.out.println(test1);
    }
}
