package com.wdbyte.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author niulang
 * @date 2023/03/23
 */
public class JavaArray {

    public static void main(String[] args) {

        // 数组定义
        String[] aaa = new String[11];
        System.out.println(aaa.length);
        //  数组访问
        int[] arr = {1, 2, 3};
        System.out.println(arr.toString()); // 数组地址
        arr[0] = 10;
        System.out.println(arr[0]);

        for (int a : arr) {
            System.out.println(a);
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        // copy
        String[] strArr = {"hello", "world", "!"};
        String[] strArrByClone = strArr.clone();
        System.out.println(String.join(" ", strArrByClone));
        // 输出：hello world !
        String[] strAyyByCopy = new String[5];
        System.arraycopy(strArr,1,strAyyByCopy,3,2);
        for (String s : strAyyByCopy) {
            System.out.println(s);
        }
        String[] newArr = Arrays.copyOfRange(strArr, 0, 2);
        System.out.println(Arrays.toString(newArr));
        // 输出：null
        //null
        //null
        //world
        //!

        Person[][] personArr = new Person[2][2];
        personArr[0][0] = new Person("www");
        personArr[1][1] = new Person("wdbyte.com");
        Person[][] personArrByClone = personArr.clone();
        personArrByClone[0][0] = new Person("https://www");
        System.out.println(personArr[0][0].name);
        System.out.println(personArrByClone[0][0].name);

        int[] numArr = {1, 3, 5, 7, 9, 2, 4, 6, 8, 10};
        // 对数组进行排序
        Arrays.sort(numArr);
        // 将数组转换成字符串输出
        System.out.println(Arrays.toString(numArr));
        // 将数组转换成 List
        String[] strArray = new String[3];
        // 将数组的所有元素都赋为指定的值
        Arrays.fill(strArray,"byte");
        System.out.println(Arrays.toString(strArray));
        List<String> stringList = Arrays.asList("a","b","c");
        System.out.println(stringList);
    }
    static class Person{
        public String name;
        public Person(String name) {
            this.name = name;
        }
    }

}
