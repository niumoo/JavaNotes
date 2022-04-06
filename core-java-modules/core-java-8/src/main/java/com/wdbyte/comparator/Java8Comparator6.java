package com.wdbyte.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author niulang
 * @date 2022/04/02
 */
public class Java8Comparator6 {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("Chris", 20));
        list.add(new Person("Linda", 10));
        list.add(new Person("Jack", 30));

        Comparator<Person> comparing = Comparator.comparing(Person::getAge);
        list.sort(comparing);
        list.forEach(System.out::println);

        System.out.println("--------");

        list.sort(comparing.reversed());
        list.forEach(System.out::println);

        list.sort((p1, p2) -> p1.getAge() - p2.getAge());
        list.forEach(System.out::println);
        System.out.println("--------");
        list.sort((p1, p2) -> p2.getAge() - p1.getAge());
        list.forEach(System.out::println);
    }

}

