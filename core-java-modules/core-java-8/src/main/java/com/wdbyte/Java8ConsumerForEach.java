package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/20
 */
public class Java8ConsumerForEach {

    public static void main(String[] args) {
        Consumer<String> printConsumer = System.out::println;
        List<String> list = Arrays.asList("java", "node", "www.wdbyte.com");
        forEach(list, printConsumer);
        forEach(list, s -> System.out.println(s.length()));
    }

    public static <T> void forEach(List<T> list, Consumer<T> consumer) {
        for (T t : list) {
            consumer.accept(t);
        }
    }
}
