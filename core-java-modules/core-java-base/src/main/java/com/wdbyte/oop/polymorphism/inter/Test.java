package com.wdbyte.oop.polymorphism.inter;

import java.util.Arrays;


/**
 * @author https://www.wdbyte.com
 * @date 2023/04/18
 */
public class Test {
    public static void main(String[] args) {
        Animal cat = new Cat();
        Animal dog = new Dog();
        for (Animal animal : Arrays.asList(cat, dog)) {
            animal.makeSound();
        }
    }

    public static void makeSound(Animal animal){
        animal.makeSound();
    }
}

