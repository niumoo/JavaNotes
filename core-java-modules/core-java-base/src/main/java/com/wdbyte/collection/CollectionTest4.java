package com.wdbyte.collection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author https://www.wdbyte.com
 */
public class CollectionTest4 {
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();
        // 向 List 中添加元素
        arrayList.add("www");
        arrayList.add("wdbyte");
        arrayList.add("com");

        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(arrayList);

        printCollection(arrayList.iterator());
        System.out.println("--------------");
        printCollection(hashSet.iterator());
    }

    public static void printCollection(Iterator<String> iterator) {
        // 当前日期
        LocalDate now = LocalDate.now();
        while (iterator.hasNext()) {
            String obj = iterator.next();
            System.out.println(now + ":" + obj.toString());
        }
    }

}