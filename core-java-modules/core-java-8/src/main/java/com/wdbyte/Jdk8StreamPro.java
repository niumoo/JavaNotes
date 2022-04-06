package com.wdbyte;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * @author darcy
 * @date 2020/12/04
 */
public class Jdk8StreamPro {

    /**
     * 计算前 N 个自然数的和
     *
     * @param adder
     * @param n
     * @return
     */
    public static long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            if (duration < fastest) { fastest = duration; }
        }
        return fastest;
    }

    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
            .limit(n)
            .reduce(0L, Long::sum);
    }

    public static void main(String[] args) {

    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> results = new ArrayList<>();
        for (T s : list) {
            if (p.test(s)) {
                results.add(s);
            }
        }
        return results;
    }

    static List<String> list = new ArrayList<>();

    static {
        list.add("Hello");
        list.add("Java");
        list.add("Python");
    }

    @Test
    public void testPredicate() {
        System.out.println(list);
        System.out.println(filter(list, s -> s.length() > 4));
        System.out.println(filter(list, String::isEmpty));
        System.out.println(filter(list, s -> s.startsWith("J")));
    }

    public static <Type extends Object> void consumer(List<Type> list, Consumer<Type> c) {
        for (Type type : list) {
            c.accept(type);
        }
    }

    @Test
    public void testConsumer() {
        consumer(list, s -> System.out.println(s));
    }

    public static <T extends Object, R extends Object> List<R> map(List<T> list, Function<T, R> function) {
        List result = new ArrayList<>(list.size());
        for (T t : list) {
            result.add(function.apply(t));
        }
        return result;
    }

    @Test
    public void testMap() {
        List<String> result = map(list, s -> "@" + s);
        System.out.println(result);
    }

}
