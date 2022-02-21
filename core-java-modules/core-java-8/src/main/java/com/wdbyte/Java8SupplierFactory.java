package com.wdbyte;

import java.util.function.Supplier;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/21
 */
public class Java8SupplierFactory {

    public static void main(String[] args) {
        Dog dog1 = dogFactory(() -> new Dog("哈士奇"));
        Dog dog2 = dogFactory(() -> new Dog("牧羊犬"));
        System.out.println(dog1);
        System.out.println(dog2);
    }

    public static Dog dogFactory(Supplier<? extends Dog> supplier) {
        Dog dog = supplier.get();
        dog.setAge(1);
        return dog;
    }

}

