package com.wdbyte.assert1;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.Assertions;

/**
 * @author niulang
 * @date 2024/04/22
 */
public class AssertDemo5 {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2");
        boolean result = list.remove("x");
        Assertions.assertTrue(result);
        Preconditions.checkNotNull("","msg");
        Validate.isTrue(list.isEmpty(),"msg");
        Verify.verify(list.isEmpty(),"msg");
    }
}
