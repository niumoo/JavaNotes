package com.wdbyte.oop.polymorphism;

import java.util.Arrays;

public class Animal {
    public void makeSound() {
        System.out.println("动物发出叫声");
    }

    public static void main(String[] args) {
        Animal cat = new Cat();
        Animal dog = new Dog();
        for (Animal animal : Arrays.asList(cat, dog)) {
            animal.makeSound();
        }
    }
}
