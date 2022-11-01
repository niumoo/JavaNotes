package com.wdbyte.string;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * apache common lang StringUtils test
 * @author niulang
 * @date 2022/11/01
 */
public class StringUtilsTest {

    public static void testA() {
        String str = "aabbccdd";
        String[] resultArray = StringUtils.split(str, "bc");
        for (String s : resultArray) {
            System.out.println(s);
        }
    }

    public static void testB() {
        String str = "abc";
        String[] resultArray = StringUtils.split(str, "ac");
        for (String s : resultArray) {
            System.out.println(s);
        }
    }

    public static void testC() {
        String str = "abcd";
        String[] resultArray = StringUtils.split(str, "ac");
        for (String s : resultArray) {
            System.out.println(s);
        }
    }

    public static void testD() {
        String str = "aabbccdd";
        String[] resultArray = StringUtils.splitByWholeSeparator(str, "bc");
        for (String s : resultArray) {
            System.out.println(s);
        }
    }

    public static void testE() {
        //String str = "aabbccdd";
        //Iterable<String> iterable = Splitter.on("bc")
        //    .omitEmptyStrings() // 忽略空值
        //    .trimResults() // 过滤结果中的空白
        //    .split(str);
        //iterable.forEach(System.out::println);
    }

    public static void testF() {
        String str = "aabbccdd";
        String[] res = str.split("bc");
        for (String re : res) {
            System.out.println(re);
        }

    }

    public static void testG() {
        String str = ",a,,b,";
        String[] splitArr = str.split(",");
        Arrays.stream(splitArr).forEach(System.out::println);
    }

}
