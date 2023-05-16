package com.wdbyte.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author https://www.wdbyte.com
 */
public class CollectionTest3 {
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();
        // 向 List 中添加元素
        arrayList.add("www");
        arrayList.add("wdbyte");
        arrayList.add("com");
        //   普通遍历
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(0));
        }
        System.out.println("--------");
        // iterator 迭代器遍历
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public void printCollection(Collection collection) {
        for (Object o : collection) {
            System.out.println(o);
        }
    }
}
