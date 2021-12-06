package com.wdbyte;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author niulang
 * @date 2021/07/21
 */
public class Java8Supplier {

    public static void main(String[] args) {
        Supplier<Integer> supplier = () -> new Random().nextInt(10);
        System.out.println(supplier.get());
        System.out.println(supplier.get());

        Supplier<LocalDateTime> supplier2 = LocalDateTime::now;
        System.out.println(supplier2.get());
        System.out.println(supplier2.get());
    }

}
