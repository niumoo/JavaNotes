package com.wdbyte.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author niulang
 * @date 2022/03/16
 */
@DisplayName("计算器")
class CalculatorTest {

    private final Calculator calculator = new Calculator();

    //@Tag("fast")
    @Test
    //@RepeatedTest(2)
    @DisplayName("相加")
    //@ParameterizedTest()
    //@ValueSource(ints = { -1, -4 })
    void add() {
        assertEquals(3, calculator.add(1, 1));
    }
}