package com.wdbyte.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

/**
 * @author https://www.wdbyte.com
 */
public class CollectionTest2 {
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();
        List<String> linkedList = new LinkedList<>();
        Set<String> hashSet = new HashSet<>();
        Set<String> treeSet = new TreeSet<>();
        Stack<String> stack = new Stack<>();
        // 添加元素
        arrayList.add("wdbyte.com");
        linkedList.add("wdbyte.com");
        hashSet.add("wdbyte.com");
        treeSet.add("wdbyte.com");
        stack.add("wdbyte.com");
        // 输出
        System.out.println(arrayList);
        System.out.println(linkedList);
        System.out.println(hashSet);
        System.out.println(treeSet);
        System.out.println(stack);
    }

}
