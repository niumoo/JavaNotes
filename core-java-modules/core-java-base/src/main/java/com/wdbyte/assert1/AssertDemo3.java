package com.wdbyte.assert1;

import java.util.Arrays;
import java.util.List;

/**
 * @author www.wdbyte.com
 * @date 2024/04/22
 */
public class AssertDemo3 {
    static final boolean asserts = false; // 设置为 false 来消除断言

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2");
        boolean result = list.remove("x");
        if (asserts) {
            assert result : "移除失败";
        }
    }
}
