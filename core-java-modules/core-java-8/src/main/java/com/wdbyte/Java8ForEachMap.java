package com.wdbyte;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author niulang
 * @date 2021/07/18
 */
public class Java8ForEachMap {

    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("java", "JAVA");
        hashMap.put("nodejs", "NODEJS");
        hashMap.put("c++", "C++");
        hashMap.put("wdbyte.com", "WDBYTE.COM");
        hashMap.put(null, "OTHER");

        hashMap.forEach((k, v) -> System.out.println(k + ":\t" + v));
    }
}
