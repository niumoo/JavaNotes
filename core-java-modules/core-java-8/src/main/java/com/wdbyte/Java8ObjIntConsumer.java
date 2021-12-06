package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * @author niulang
 * @date 2021/07/20
 */
public class Java8ObjIntConsumer {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", "node", "www.wdbyte.com");
        ObjIntConsumer<String> consumer = (str, i) -> {
            if (str.length() == i) {
                System.out.println(str);
            }
        };
        forEach(list, consumer, 4);
    }

    public static <T> void forEach(List<T> list, ObjIntConsumer<T> consumer, int i) {
        for (T t : list) {
            consumer.accept(t, i);
        }
    }
}
