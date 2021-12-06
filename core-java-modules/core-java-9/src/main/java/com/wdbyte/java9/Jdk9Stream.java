package com.wdbyte.java9;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *  * @author 达西 - 公众号：未读代码
 */
public class Jdk9Stream {

    /**
     * takeWhile ,从头开始筛选，遇到不满足的就结束了
     * 
     * @param args
     */
    public static void main(String[] args) {
        // takeWhile ,从头开始筛选，遇到不满足的就结束了
        List<Integer> list1 = List.of(1, 2, 3, 4, 5);
        List<Integer> listResult = list1.stream().takeWhile(x -> x < 3).collect(Collectors.toList());
        System.out.println(listResult);
        // takeWhile ,从头开始筛选，遇到不满足的就结束
        List<Integer> list2 = List.of(1, 2, 3, 4, 3, 0);
        List<Integer> listResult2 = list2.stream().takeWhile(x -> x < 3).collect(Collectors.toList());
        System.out.println(listResult2);
    }

    /**
     * dropWhile ,从头开始删除，遇到不满足的就结束了
     */
    public void dropWhile() {
        // dropWhile ,从头开始删除，遇到不满足的就结束了
        List<Integer> list1 = List.of(1, 2, 3, 4, 5);
        List<Integer> listResult = list1.stream().dropWhile(x -> x < 3).collect(Collectors.toList());
        System.out.println(listResult);
        // dropWhile ,从头开始删除，遇到不满足的就结束
        List<Integer> list2 = List.of(1, 2, 3, 4, 3, 0);
        List<Integer> listResult2 = list2.stream().dropWhile(x -> x < 3).collect(Collectors.toList());
        System.out.println(listResult2);
    }

    /**
     * 使用 ofNullable 创建支持 null 的 stream
     */
    public void ofNullable() {
        Stream<Integer> stream = Stream.of(1, 2, null);
        stream.forEach(System.out::print);
        System.out.println();
        // 空指针异常
        // stream = Stream.of(null);
        stream = Stream.ofNullable(null);
        stream.forEach(System.out::print);
    }

    /**
     * 重载迭代器
     */
    public void iterate() {
        IntStream.iterate(0, x -> x < 10, x -> x + 1).forEach(System.out::print);
    }

    /**
     * Optional 转 Stream
     */
    public void optionalToStream() {
        Stream<Integer> s = Optional.of(1).stream();
        s.forEach(System.out::print);
    }
}
