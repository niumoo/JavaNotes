package com.wdbyte.comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author niulang
 * @date 2022/04/02
 */
public class Java8Comparator4 {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("Chris", 20));
        list.add(new Person("Linda", 10));
        list.add(new Person("Jack", 30));
        sort(list);
        for (Person person : list) {
            System.out.println(person);
        }
    }

    private static List<Person> sort(List<Person> list) {
        list.sort(Comparator.comparing(Person::getName));
        return list;
    }
}

