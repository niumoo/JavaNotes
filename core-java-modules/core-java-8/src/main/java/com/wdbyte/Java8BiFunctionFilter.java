package com.wdbyte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/26
 */
public class Java8BiFunctionFilter {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        // 筛选 2 的倍数
        List<Integer> result1 = filter(list, 2, Java8BiFunctionFilter::divisible);
        System.out.println(result1);
        // 筛选 3 的倍数
        List<Integer> result2 = filter(list, 3, Java8BiFunctionFilter::divisible);
        System.out.println(result2);
        // 筛选 4 的倍数
        List<Integer> result3 = filter(list, 4, Java8BiFunctionFilter::divisible);
        System.out.println(result3);

        // 筛选长度为 4 的字符串
        List<String> stringList = Arrays.asList("java", "node", "c++", "rust", "www.wdbyte.com");
        List<String> stringList1 = filter(stringList, 4, (s, n) -> s.length() == 4 ? true : null);
        System.out.println(stringList1);
    }

    /**
     * n1 / n2 是否可以除尽
     *
     * @param n1
     * @param n2
     * @return
     */
    private static Boolean divisible(Integer n1, Integer n2) {
        if (n1 % n2 == 0) {
            return true;
        }
        return null;
    }

    /**
     * 过滤集合 List 中，符合 BiFunction<T, U, R> biFunction 的元素
     *
     * @param list
     * @param u
     * @param biFunction
     * @param <T>
     * @param <U>
     * @param <R>
     * @return
     */
    private static <T, U, R> List<T> filter(List<T> list,
                                         U u,
                                         BiFunction<T, U, R> biFunction) {
        List<T> resultList = new ArrayList<>();
        for (T t : list) {
            if (biFunction.apply(t, u) != null) {
                resultList.add(t);
            }
        }
        return resultList;
    }
}
