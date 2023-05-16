package com.wdbyte.oop.abs;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/17
 */
public class PersonTest {
    public static void main(String[] args) {
        AbsPerson absPerson = new Student();
        System.out.println(absPerson.age);
        absPerson.eat();
        absPerson.sleep();

        Student student = new Student();
        Teacher teacher = new Teacher();
        sleep(student);
        sleep(teacher);
    }

    public static void sleep(AbsPerson person) {
        person.sleep();
    }
}
