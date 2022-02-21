package com.wdbyte;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author https://www.wdbyte.com
 * @date 2021/08/02
 */
public class Java8UnaryOperator {

    public static void main(String[] args) {
        Function<String, String> upperFun1 = s -> s.toUpperCase();
        UnaryOperator<String> upperFun2 = s -> s.toUpperCase();

        String res1 = upperFun1.apply("wdbyte.com");
        String res2 = upperFun2.apply("wdbyte.com");

        System.out.println(res1);
        System.out.println(res2);
    }
}
