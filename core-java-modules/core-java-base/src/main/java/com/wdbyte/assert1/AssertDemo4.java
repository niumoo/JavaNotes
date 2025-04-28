package com.wdbyte.assert1;

import java.util.Arrays;
import java.util.List;

/**
 * @author www.wdbyte.com
 * @date 2024/04/22
 */
public class AssertDemo4 {

    static {
        boolean assertsEnabled = false;
        assert assertsEnabled = true; // 故意产生副作用！！！
        if (!assertsEnabled) {
            throw new RuntimeException("必须启用断言!!!");
        }
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2");
        boolean result = list.remove("x");
        assert result : "移除失败";
    }
}
