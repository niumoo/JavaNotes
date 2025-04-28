package com.wdbyte.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author www.wdbyte.com
 * @date 2023/10/19
 */
public class ArrayListTest2 {

    public static void main(String[] args) {
        List<String> list = new ArrayList();
        list.add("a");
        list.add("b");
        System.out.println("当前集合：" + list);
        System.out.println("获取第一个元素：" + list.get(0));
        list.set(0, "x");
        System.out.println("修改第一个元素为 x：" + list);
        list.remove(1);
        System.out.println("移除第2个元素后剩余：" + list);
        System.out.println("判断元素 x 是否存在：" + list.contains("x"));
        System.out.println("判断元素 a 是否存在：" + list.contains("a"));
        System.out.println("当前 list 大小：" + list.size());

        list.add("y");
        list.add("z");
        System.out.println("添加两个元素：" + list);

        System.out.println("遍历元素：");
        for (String string : list) {
            System.out.print(string);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
        }
        list.forEach(s -> {
            System.out.println(s);
        });

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

    }
}
