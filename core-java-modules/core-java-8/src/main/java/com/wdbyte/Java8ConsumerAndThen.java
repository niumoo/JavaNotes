package com.wdbyte;

import java.util.function.Consumer;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/20
 */
public class Java8ConsumerAndThen {

    public static void main(String[] args) {
        Consumer<String> lengthConsumer = s -> System.out.println(s.length());
        Consumer<String> printConsumer = lengthConsumer.andThen(System.out::println);
        printConsumer.accept("www.wdbyte.com");
    }
}
