package com.wdbyte.test.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author niulang
 * @date 2022/11/15
 */
class JUnitTestIsDog {

    @BeforeAll
    public static void init() {
        System.out.println("准备测试 Dog 信息");
    }

    @Test
    public void testIsDog() {
        String name = "cat";
        Assertions.assertEquals(name, "dog");
    }

    @Test
    public void testIsDog2() {
        String name = "dog";
        Assertions.assertEquals(name, "dog");
    }
}
