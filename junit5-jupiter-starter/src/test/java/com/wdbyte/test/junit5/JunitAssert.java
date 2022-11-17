package com.wdbyte.test.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author niulang
 * @date 2022/11/17
 */
public class JunitAssert {

    @DisplayName("是否是狗")
    @Test
    public void testIsDog() {
        String name = "dog";
        Assertions.assertNotNull(name);
        Assertions.assertEquals(name, "dog");
        Assertions.assertNotEquals(name, "cat");
        Assertions.assertTrue("dog".equals(name));
        Assertions.assertFalse("cat".equals(name));
    }

    @DisplayName("是否是猫")
    @Test
    public void testIsCat() {
        String name = "cat";
        Assertions.assertNull(name, "name is not null");
    }

}
