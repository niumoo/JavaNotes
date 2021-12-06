package com.wdbyte;

import java.util.function.BiFunction;

/**
 * @author niulang
 * @date 2021/07/26
 */
public class JavaBiFunctionFactory {

    public static void main(String[] args) {
        System.out.println(dogFactory("牧羊犬", 1, Dog::new));
        System.out.println(dogFactory("哈士奇", 2, Dog::new));
    }

    public static <R extends Dog> Dog dogFactory(String name, Integer age, BiFunction<String, Integer, R> biFunction) {
        return biFunction.apply(name, age);
    }
}
