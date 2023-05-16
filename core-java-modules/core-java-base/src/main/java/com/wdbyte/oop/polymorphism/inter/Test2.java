package com.wdbyte.oop.polymorphism.inter;


/**
 * @author https://www.wdbyte.com
 * @date 2023/04/18
 */
public class Test2 {
    public static void main(String[] args) {
        Animal cat = new Cat();
        Animal dog = new Dog();
        makeSound(cat); // 喵喵喵
        makeSound(dog); // 汪汪汪
    }

    public static void makeSound(Animal animal) {
        animal.makeSound();
    }
}

