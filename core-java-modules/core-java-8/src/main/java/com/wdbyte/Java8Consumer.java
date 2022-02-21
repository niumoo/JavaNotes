package com.wdbyte;

import java.util.function.Consumer;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/20
 */
public class Java8Consumer {

    public static void main(String[] args) {
        Consumer<String> lengthConsumer = s -> System.out.println(s.length());
        lengthConsumer.accept("www.wdbyte.com");

        Consumer<String> printConsumer = System.out::println;
        printConsumer.accept("www.wdbyte.com");
    }
}
