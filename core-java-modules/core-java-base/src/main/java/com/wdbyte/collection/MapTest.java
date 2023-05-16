package com.wdbyte.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author https://www.wdbyte.com
 */
public class MapTest {
    public static void main(String[] args) {
        Map<String, String> hashMap = new HashMap<>();
        // 添加元素
        hashMap.put("site","www.wdbyte.com");
        hashMap.put("author","程序猿阿朗");
        hashMap.put("github","github.com/niumoo");
        // 获取元素
        System.out.println(hashMap.get("github"));
        // 输出全部元素
        System.out.println(hashMap);
    }
}
