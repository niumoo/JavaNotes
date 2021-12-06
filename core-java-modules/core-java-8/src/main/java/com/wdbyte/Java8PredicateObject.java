package com.wdbyte;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author niulang
 * @date 2021/07/19
 */
public class Java8PredicateObject {

    public static void main(String[] args) {
        List<Dog> dogList = new ArrayList<>();
        dogList.add(new Dog("哈士奇", 1));
        dogList.add(new Dog("牧羊犬", 2));
        dogList.add(new Dog("柯基", 3));
        dogList.add(new Dog("柴犬", 3));

        // 找到 3岁的狗
        System.out.println(filter(dogList, dog -> dog.getAge().equals(3)));
        // 找到哈士奇信息
        Predicate<Dog> predicate = dog -> ("哈士奇").equals(dog.getName());
        System.out.println(filter(dogList, predicate));
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> resultList = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) { resultList.add(t); }
        }
        return resultList;
    }
}