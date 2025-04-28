package com.wdbyte.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author www.wdbyte.com
 * @date 2023/10/19
 */
public class ArrayListTest {

    public static void main(String[] args) {
        //String[] arr = new String[]{"w","d","b","y","t","e"};
        ////arr[7] = "f";
        //
        //List<String> list = new ArrayList();
        //list.add("a");
        //list.add("b");
        //list.add("c");
        //list.add("d");
        //
        //System.out.println(list.get(2));
        //list.remove(2);
        //System.out.println(list.get(2));

        //List<String> list2 = List.of("www", "wdbyte", "com");
        //list2.add("a");
        //System.out.println(list2);

        List<String> list = new ArrayList();
        list.add("b");
        list.add("c");
        list.add("a");
        list.add("d");

        System.out.println(list);

        list = list.stream().sorted().collect(Collectors.toList());
        System.out.println(list);

        // 排序时指定降序还是升序：Comparator.reverseOrder() 降序
        list = list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        System.out.println(list);

        System.out.println("----------");

        Collections.sort(list);
        System.out.println(list);
        // 降序
        Collections.sort(list,Comparator.reverseOrder());
        System.out.println(list);

        list.sort(Comparator.comparing(Function.identity()));
        System.out.println(list);



    }
}
