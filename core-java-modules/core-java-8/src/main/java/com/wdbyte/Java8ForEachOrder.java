package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/18
 */
public class Java8ForEachOrder {

    public static void main(String[] args) {
        Stream<String> stream = Stream.of("java", "nodejs", "c++", "wdbyte.com");
        stream.parallel().forEachOrdered(System.out::println);
    }
}
