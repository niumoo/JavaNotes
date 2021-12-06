package com.wdbyte;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author niulang
 * @date 2021/07/18
 */
public class Java8ForEachMapNormal {

    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("java", "JAVA");
        hashMap.put("nodejs", "NODEJS");
        hashMap.put("c++", "C++");
        hashMap.put("wdbyte.com", "WDBYTE.COM");
        hashMap.put(null, "OTHER");

        for (Entry<String, String> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
        }
    }
}
