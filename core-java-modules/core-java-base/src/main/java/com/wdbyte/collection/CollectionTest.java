package com.wdbyte.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author niulang
 */
public class CollectionTest {
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();
        // 向 List 中添加元素
        arrayList.add("www");
        arrayList.add("wdbyte");
        arrayList.add("com");
        arrayList.add("com");
        // 输出
        System.out.println(arrayList);
    }
}
