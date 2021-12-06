package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author niulang
 * @date 2021/07/18
 */
public class Java8ForEachDiyConsumer {
    public static void main(String[] args) {
        Stream<String> stream = Stream.of("java", "nodejs", "wdbyte.com");
        List<String> list = Arrays.asList("java", "nodejs", "wdbyte.com");

        Consumer<String> consumer = (s -> {
            String upperCase = s.toUpperCase();
            System.out.println(upperCase);
        });

        list.forEach(consumer);
        stream.forEach(consumer);
    }
}
