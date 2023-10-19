package com.wdbyte.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

/**
 * @author niulang
 * @date 2023/10/19
 */
public class ArrayListTest3 {

    public static void main(String[] args) {
        List<String> list = new ArrayList();
        list.add("a");
        list.add("b");

        String[] array = list.toArray(new String[0]);
        System.out.println(Arrays.toString(array));

        String[] array2 = list.stream().toArray(String[]::new);
        System.out.println(Arrays.toString(array2));

        // 数组 -> List
        // 方式1
        List<String> list0 = Lists.newArrayList(array);
        list0.add("e");
        System.out.println(list0);

        // 方式2
        List<String> list1 = Arrays.stream(array).toList();
        // list1.add("e"); 报错，不能修改
        System.out.println(list1);

        // 方式3
        List<String> list2 = Arrays.asList(array);
        //list2.add("e"); 报错，不能修改
        System.out.println(list2);

        // 方式4
        List<String> list3 = new ArrayList<>(Arrays.asList(array));
        list3.add("e");
        System.out.println(list3);

        // 方式5
        List<String> list4 = Arrays.stream(array).collect(Collectors.toList());
        list4.add("e");
        System.out.println(list4);
    }
}
