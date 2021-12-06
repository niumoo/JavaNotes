package com.wdbyte;

import java.util.Random;
import java.util.function.IntSupplier;

/**
 * @author niulang
 * @date 2021/07/23
 */
public class Java8SupplierInt {

    public static void main(String[] args) {
        IntSupplier intSupplier = () -> new Random().nextInt(10);
        System.out.println(intSupplier.getAsInt());
        System.out.println(intSupplier.getAsInt());
    }
}
