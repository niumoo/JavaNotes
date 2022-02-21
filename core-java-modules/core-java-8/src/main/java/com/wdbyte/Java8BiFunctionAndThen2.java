package com.wdbyte;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/26
 */
public class Java8BiFunctionAndThen2 {

    public static void main(String[] args) {
        String result = convert("java",
                                    "www.wdbyte.com",
                                    (a1, a2) -> a1.length() + a2.length(),
                                    r1 -> "长度和:" + r1);
        System.out.println(result);

        String convert = convert(1, 2,
            (a1, a2) -> a1 + a2,
            r1 -> "和是:" + r1);
        System.out.println(convert);
    }

    public static <T1, T2, R1, R2> R2 convert(  T1 t1,
                                                T2 t2,
                                                BiFunction<T1, T2, R1> biFunction,
                                                Function<R1, R2> function) {
        return biFunction.andThen(function).apply(t1, t2);
    }
}
