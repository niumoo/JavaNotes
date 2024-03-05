package com.wdbyte.collection;

import java.util.EnumMap;

/**
 * @author niulang
 * @date 2023/10/20
 */
public class EnumMapTest {
    public static void main(String[] args) {
        EnumMap<Week, String> enumMap = new EnumMap(Week.class);
        enumMap.put(Week.a,null);
        System.out.println(enumMap.get(Week.a));
    }
}

enum Week {
    a,
    b,
    c,
    d,
    e,
    f,
    g
}
