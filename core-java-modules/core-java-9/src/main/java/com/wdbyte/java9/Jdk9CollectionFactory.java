package com.wdbyte.java9;

import java.util.*;

/**
 * java 9 - 集合工厂方法
 * 集合工厂方法创建的集合都是不可变只读集合，在改变时会抛出异常。
 * 对于 Set 和 Map 工厂方法常见的,创建时如果有 key 值重复，也会直接抛出异常。
 * 对于 Set 和 Map 工厂方法常见的,每个 JVM 运行周期里遍历的顺序是不定的。
 * 
 * @author 达西 - 公众号：未读代码
 */
public class Jdk9CollectionFactory {

    public static void main(String[] args) {
        // 工厂方法创建集合
        List<String> stringList = List.of("a", "b", "c", "d");
        Set<String> stringSet = Set.of("a", "b", "c", "d");
        Map<String, Integer> stringIntegerMap = Map.of("key1", 1, "key2", 2, "key3", 3);
        Map<String, Integer> stringIntegerMap2 = Map.ofEntries(Map.entry("key1", 1), Map.entry("key2", 2));

        // 集合输出
        System.out.println(stringList);
        System.out.println(stringSet);
        System.out.println(stringIntegerMap);
        System.out.println(stringIntegerMap2);

    }

    public void hashCodeOf(){
        // 工厂可以自由创建新的实例或者复用现有实例，所以 使用 of 创建的集合，避免 == 或者 hashCode 判断操作
        List<String> stringList = List.of("a", "b", "c", "d");
        List<String> stringList2 = List.of("a", "b", "c", "d");
        System.out.println(stringList.hashCode());
        System.out.println(stringList2.hashCode());
    }

    /**
     * jdk 9 之前创建只读集合方式
     */
    public void jdk9Before() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("达西");
        arrayList.add("未读代码");
        // 设置为只读集合
        arrayList = Collections.unmodifiableList(arrayList);
        // java.lang.UnsupportedOperationException
        // arrayList.add("test");
        // Set,Map 同理
    }
}
