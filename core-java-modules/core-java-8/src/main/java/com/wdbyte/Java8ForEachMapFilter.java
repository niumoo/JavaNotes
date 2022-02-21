package com.wdbyte;

import java.util.HashMap;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/18
 */
public class Java8ForEachMapFilter {

    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("java", "JAVA");
        hashMap.put("nodejs", "NODEJS");
        hashMap.put("c++", "C++");
        hashMap.put("wdbyte", "WDBYTE.COM");
        hashMap.put(null, "OTHER");

        hashMap.forEach((k, v) -> {
            if (k != null) {
                System.out.println(k + ":\t" + v);
            }
        });
    }
}
