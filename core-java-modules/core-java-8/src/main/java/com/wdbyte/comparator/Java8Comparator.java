package com.wdbyte.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author https://www.wdbyte.com
 * @date 2022/04/02
 */
public class Java8Comparator {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("Linda", 10));
        list.add(new Person("Jack", 30));
        list.add(new Person("Chris", 20));
        sortListJava7(list);
    }

    private static void sortListJava7(List<Person> list) {
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge() - o2.getAge();
            }
        });
        for (Person person : list) {
            System.out.println(person);
        }
    }
}

class Person {
    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
