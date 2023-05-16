package com.wdbyte.oop;

/**
 * @author https://www.wdbyte.com
 * @date 2023/03/31
 */
public class JavaExtends {
    public static void main(String[] args) {
        Student student = new Student();
        printSleep(student);
    }
    public static void printSleep(Person person){
        person.sleep();
    }
}

class Person {
    public void eat() {
        System.out.println("吃饭");
    }

    public void sleep() {
        System.out.println("睡觉");
    }
}

class Student extends Person {

    public Student() {
        super();
    }

    public void study() {
        System.out.println("学习");
    }

    @Override
    public void eat() {
        System.out.println("吃大米");
    }

    @Override
    public void sleep() {
        System.out.println("上课时不能睡觉");
    }
}

interface One {
    public void print_geek();
}

interface Two {
    public void print_for();
}

interface Three extends One, Two {
    public void print_geek();
}

class Teach implements Three{

    @Override
    public void print_for() {

    }

    @Override
    public void print_geek() {

    }
}