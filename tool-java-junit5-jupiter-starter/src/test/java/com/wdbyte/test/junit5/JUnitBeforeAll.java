package com.wdbyte.test.junit5;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author https://www.wdbyte.com
 * @date 2022/11/15
 */
class JUnitBeforeAll {

    @BeforeAll
    public static void init() {
        System.out.println("初始化，准备测试信息");
    }

    @BeforeEach
    public void start() {
        System.out.println("开始测试...");
    }

    @DisplayName("是否是狗")
    @Disabled("由于xx原因，关闭 testIsDog 测试")
    @Test
    public void testIsDog() {
        String name = "dog";
        Assertions.assertEquals(name, "dog");
        System.out.println("is dog");
    }

    @DisplayName("是否是猫")
    @Test
    public void testIsCat() {
        String name = "cat";
        Assertions.assertEquals(name, "cat");
        System.out.println("is cat");
    }

    @AfterEach
    public void end() {
        System.out.println("测试完毕...");
    }

    @AfterAll
    public static void close() {
        System.out.println("结束，准备退出测试");
    }
}
