package com.wdbyte;

import java.util.Arrays;
import java.util.List;

/**
 * @author niulang
 * @date 2021/07/18
 */
public class Java8ForEachListNormal {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", "nodejs", "c++", "wdbyte.com");
        for (String s : list) {
            System.out.println(s);
        }
    }
}
