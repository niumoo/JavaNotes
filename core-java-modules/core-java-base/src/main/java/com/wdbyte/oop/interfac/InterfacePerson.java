package com.wdbyte.oop.interfac;

public interface InterfacePerson {
    default void sayHello(){
        System.out.println("say hello");
    }

    void sleep();
}
