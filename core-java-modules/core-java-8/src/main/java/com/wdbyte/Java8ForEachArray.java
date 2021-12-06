package com.wdbyte;

import java.util.Arrays;
import java.util.List;

/**
 * @author niulang
 * @date 2021/07/18
 */
public class Java8ForEachArray {
    public static void main(String[] args) {
        String[] array = {"java", "nodejs", "wdbyte.com"};
        Arrays.stream(array).forEach(System.out::println);
    }
}
