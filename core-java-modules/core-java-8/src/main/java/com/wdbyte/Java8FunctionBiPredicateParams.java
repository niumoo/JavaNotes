package com.wdbyte;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * @author https://www.wdbyte.com
 * @date 2021/08/01
 */
public class Java8FunctionBiPredicateParams {

    public static void main(String[] args) {
        List<Dog> list = new ArrayList<>();
        list.add(new Dog("牧羊犬", 1));
        list.add(new Dog("牧羊犬", 2));
        list.add(new Dog("哈士奇", 2));
        list.add(new Dog("田园犬", 3));
        // 筛选2岁的狗
        BiPredicate<String, Integer> age = (n, a) -> a == 2;
        // 筛选牧羊犬
        BiPredicate<String, Integer> name = (n, a) -> "牧羊犬".equals(n);
        // 筛选2岁的狗或者筛选牧羊犬
        BiPredicate<String, Integer> ageAndName = (n, a) -> "牧羊犬".equals(n) || a == 2;
        System.out.println(filter(list, age));
        System.out.println(filter(list, name));
        System.out.println(filter(list, ageAndName));

    }

    public static <T extends Dog> List<T> filter(List<T> list, BiPredicate<String, Integer> biPredicate) {
        return list.stream()
            .filter(dog -> biPredicate.test(dog.getName(), dog.getAge()))
            .collect(Collectors.toList());
    }
}
