package com.wdbyte.oop.abs;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/17
 */
public class Teacher extends AbsPerson {

    @Override
    public void sleep() {
        System.out.println("老师教课时不能睡觉");
    }
}
