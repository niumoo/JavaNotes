package com.wdbyte.test.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author niulang
 * @date 2022/11/17
 */
@TestMethodOrder(OrderAnnotation.class)
public class JUnitOther {

    //@Test
    //@EnabledOnJre(JAVA_19)
    @DisplayName("测试是否是狗")
    @Order(2)
    @RepeatedTest(10)
    public void testIsDog() {
        String name = "dog";
        Assertions.assertEquals(name, "dog");
        System.out.println("is dog");
    }

    @DisplayName("是否是猫")
    @Test
    @Order(1)
    public void testIsCat() {
        String name = "cat";
        Assertions.assertEquals(name, "cat");
        System.out.println("is cat");
    }
}
