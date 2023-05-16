package com.wdbyte.comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author https://www.wdbyte.com
 * @date 2022/04/02
 */
public class Java8Comparator7 {
    public static void main(String[] args) {
        List<com.wdbyte.comparator.Person> list = new ArrayList<>();
        list.add(new com.wdbyte.comparator.Person("Chris", 20));
        list.add(new com.wdbyte.comparator.Person("Linda", 10));
        list.add(new com.wdbyte.comparator.Person("Jack", 30));

        list.stream()
            .sorted(Comparator.comparing(Person::getAge))
            .forEach(System.out::println);
        System.out.println("----------");
        list.stream()
            .sorted(Comparator.comparing(Person::getAge).reversed())
            .forEach(System.out::println);
    }

}

