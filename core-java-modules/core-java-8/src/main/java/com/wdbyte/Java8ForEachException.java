package com.wdbyte;

import java.util.Arrays;
import java.util.List;

/**
 * @author niulang
 * @date 2021/07/18
 */
public class Java8ForEachException {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("java", null, "nodejs", "wdbyte.com");
        //list.forEach(Java8ForEachException::length);
        list.forEach(s->{
            try {
                System.out.println(s.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void length(String s) {
        try {
            System.out.println(s.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
