package com.wdbyte.list_to_map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/23
 */
public class ListToMapConcurrentHashMap {

    public static void main(String[] args) {
        List<Dog> list = new ArrayList<>();
        list.add(new Dog("牧羊犬", 1));
        list.add(new Dog("牧羊犬", 2));
        list.add(new Dog("哈士奇", 2));
        list.add(new Dog("田园犬", 3));

        // to map,key dog name,value ,dog age
        Map<String, Integer> dogMap = list.stream()
            .collect(Collectors.toMap(Dog::getName, Dog::getAge, (oldData, newData) -> newData, ConcurrentHashMap::new));

        System.out.println(dogMap.getClass());
    }
}
