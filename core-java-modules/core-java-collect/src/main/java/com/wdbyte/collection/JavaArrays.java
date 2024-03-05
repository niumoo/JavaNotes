package com.wdbyte.collection;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import org.junit.jupiter.api.Test;

/**
 * @author niulang
 * @date 2024/03/04
 */
public class JavaArrays {

    @Test
    public void print() {
        String[] arr = new String[] {"a", "b", "c", "d"};
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void init() {
        String[] arr = new String[] {"a", "b", "c", "d"};
        String[] copyOf2 = Arrays.copyOf(arr, 2);
        String[] copyOfRange = Arrays.copyOfRange(arr, 1, 3);

        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(copyOf2));
        System.out.println(Arrays.toString(copyOfRange));

        // 目标大小大于原始大小，则copyOf 会用null填充数组 。
        String[] copyOf10 = Arrays.copyOf(arr, 10);
        System.out.println(Arrays.toString(copyOf10));
    }

    @Test
    public void fill() {
        String[] arr = new String[5];
        Arrays.fill(arr, "java");
        System.out.println(Arrays.toString(arr));

        // 生成 100以内的 随机数
        IntFunction<Integer> intFunction = i -> new Random().nextInt(100);
        Integer[] intArr = new Integer[5];
        Arrays.setAll(intArr, intFunction);
        System.out.println(Arrays.toString(intArr));
    }

    @Test
    public void diff() {
        String[] arr = new String[] {"a", "b", "c", "d"};
        Object[] arr1 = new Object[] {arr, new String[] {"a", "b", "c", "d"}};
        Object[] arr2 = new Object[] {arr, arr};

        System.out.println(Arrays.equals(arr1, arr2));
        System.out.println(Arrays.deepEquals(arr1, arr2));

        // hashCode
        System.out.println(Arrays.hashCode(arr2));
        System.out.println(Arrays.deepHashCode(arr2));

        arr[0] = null;
        System.out.println(Arrays.hashCode(arr2));
        System.out.println(Arrays.deepHashCode(arr2));
    }

    @Test
    public void sort() {
        // 生成 100以内的 随机数
        IntFunction<Integer> intFunction = i -> new Random().nextInt(100);
        Integer[] intArr = new Integer[5];
        Arrays.setAll(intArr, intFunction);
        System.out.println(Arrays.toString(intArr));

        Arrays.sort(intArr);
        System.out.println(Arrays.toString(intArr));

        //long cost = 0;
        //for (int ii = 0; ii < 10; ii++) {
        //    IntFunction<Integer> intFunction = i -> new Random().nextInt(100000);
        //    Integer[] intArr = new Integer[100000];
        //    Arrays.setAll(intArr, intFunction);
        //
        //    long start = System.currentTimeMillis();
        //    Arrays.sort(intArr);
        //    long end = System.currentTimeMillis();
        //    cost = cost + (end - start);
        //}
        //System.out.println(cost / 10);
        //
        //cost = 0;
        //for (int ii = 0; ii < 10; ii++) {
        //    IntFunction<Integer> intFunction = i -> new Random().nextInt(100000);
        //    Integer[] intArr = new Integer[100000];
        //    Arrays.setAll(intArr, intFunction);
        //
        //    long start = System.currentTimeMillis();
        //    Arrays.parallelSort(intArr);
        //    long end = System.currentTimeMillis();
        //    cost = cost + (end - start);
        //}
        //System.out.println(cost / 10);
    }

    @Test
    public void search() {
        Integer[] intArr = new Integer[] {2, 3, 4, 5, 6, 7, 8, 9};
        int index = Arrays.binarySearch(intArr, 3);
        System.out.println("index:"+index);
        System.out.println(intArr[index]);
    }

    @Test
    public void stream() {
        Integer[] intArr = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(Arrays.stream(intArr).count());
        ToIntFunction toIntFunction = i -> (int)i;
        System.out.println(Arrays.stream(intArr).mapToInt(toIntFunction).sum());
    }

    @Test
    public void cast() {
        String[] arr = new String[] {"a", "b", "c", "d"};
        Object[] arr2 = new Object[] {arr, arr};

        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(arr2));

        System.out.println(Arrays.deepToString(arr));
        System.out.println(Arrays.deepToString(arr2));

        List<String> list = Arrays.asList(arr);
        System.out.println(list);
        System.out.println(list.getClass());
        // list.add("e"); 报错
    }

    @Test
    public void prefix() {
        Integer[] intArr = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Arrays.parallelPrefix(intArr, (left, right) -> left + right);
        System.out.println(Arrays.toString(intArr));
    }
}
