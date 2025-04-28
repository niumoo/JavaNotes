package com.wdbyte.assert1;

import java.util.Arrays;
import java.util.List;

/**
 * @author www.wdbyte.com
 * @date 2024/04/22
 */
public class AssertDemo1 {
    public static void main(String[] args) {

        List<String> list = Arrays.asList("1", "2");
        boolean result = list.remove("x");
        //assert result;
        assert result : "移除失败";
        System.out.println(calc(100, 10));

        // 手动开启断言
        //ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        //System.out.println(calc(100, 0));
    }

    public static int calc(int a, int b) {
        assert b != 0 : "除数不能为0";
        return a / b;

    }
}
