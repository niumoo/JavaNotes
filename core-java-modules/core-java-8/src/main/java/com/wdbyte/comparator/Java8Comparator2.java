package com.wdbyte.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author niulang
 * @date 2022/04/02
 */
public class Java8Comparator2 {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("Chris", 20));
        list.add(new Person("Linda", 10));
        list.add(new Person("Jack", 30));
        sort(list);
        list.forEach(System.out::println);
    }

    private static List<Person> sort(List<Person> list) {
        //Comparator<Person> byAge = (Person o1, Person o2) -> o1.getAge().compareTo(o2.getAge());
        Comparator<Person> byAge = Comparator.comparing(Person::getAge);
        list.sort(byAge);
        return list;
    }
}

