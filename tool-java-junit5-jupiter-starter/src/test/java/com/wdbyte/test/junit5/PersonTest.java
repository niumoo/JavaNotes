package com.wdbyte.test.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author https://www.wdbyte.com
 * @date 2022/11/17
 */
@DisplayName("测试 Presion")
class PersonTest {

    @DisplayName("测试幸运数字")
    @Test
    void getLuckyNumber() {
        Person person = new Person();
        Assertions.assertEquals(8, person.getLuckyNumber());
    }
}