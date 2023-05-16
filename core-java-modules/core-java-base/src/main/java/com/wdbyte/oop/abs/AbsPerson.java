package com.wdbyte.oop.abs;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/17
 */
public abstract class AbsPerson {
    public int age = 22;

    public void eat() {
        System.out.println("吃饭");
    }

    public abstract void sleep();
}
