package com.wdbyte.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author niulang
 */
public class MapTest2 {
    public static void main(String[] args) {
        Map<String, String> hashMap = new HashMap<>();
        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        // 添加元素
        hashMap.put("site", "www.wdbyte.com");
        hashMap.put("author", "程序猿阿朗");
        hashMap.put("github", "github.com/niumoo");
        linkedHashMap.put("site", "www.wdbyte.com");
        linkedHashMap.put("author", "程序猿阿朗");
        linkedHashMap.put("github", "github.com/niumoo");

        // 输出全部元素
        System.out.println(hashMap);
        System.out.println(linkedHashMap);
    }
}
