package com.wdbyte.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

/**
 * @author www.wdbyte.com
 * @date 2023/10/19
 */
public class ArrayListTest4 {

    public static void main(String[] args) {
        List<Dog> list = new ArrayList();
        list.add(new Dog("大黄", 1));
        list.add(new Dog("小黑", 2));

        Map<String, Dog> dogMap = list.stream()
            .collect(Collectors.toMap(Dog::getName, Function.identity(), (o, n) -> o));
        System.out.println(dogMap.get("大黄"));

        Map<String, Dog> dogMap2 = new HashMap<>();
        for (Dog dog : list) {
            dogMap2.put(dog.getName(), dog);
        }
        System.out.println(dogMap.get("大黄"));

        List<String> arrayList = new ArrayList();
        arrayList.add("a");
        arrayList.add("b");

        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if ("a".equals(next)) {
                iterator.remove();
            }
        }
        System.out.println(arrayList);
    }

}

class Dog {
    String name;
    Integer age;

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

    public Dog(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Dog{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }
}