package com.wdbyte.assert1;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Verify.*;

/**
 * @author www.wdbyte.com
 * @date 2024/04/22
 */
public class AssertDemo2 {
    public static void main(String[] args) {
        int x = 100;
        verifyNotNull(x != 0);
        System.out.println(calc(100, 10));
        System.out.println(calc(100, 0));
    }

    public static int calc(int a, int b) {
        verify(b != 0, "除数不能为0");
        return a / b;
    }
}
