package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author niulang
 * @date 2021/07/25
 */
public class Java8ForEachDiy {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", "nodejs", "c++", "wdbyte.com");
        list.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });

        list.forEach(s -> System.out.println(s));
        list.forEach(System.out::println);
    }
}
