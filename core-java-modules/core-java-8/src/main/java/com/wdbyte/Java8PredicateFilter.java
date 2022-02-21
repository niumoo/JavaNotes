package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/19
 */
public class Java8PredicateFilter {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", "node", "wdbyte.com");
        list = list.stream().filter(str -> str.length() == 4).collect(Collectors.toList());
        System.out.println(list);
    }
}
