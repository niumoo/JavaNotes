package com.wdbyte.oop.interfac;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/13
 */
public class JavaInterface {

    public static void main(String[] args) {
        MyInterface.staticMethod();
        System.out.println(MyInterface.param);
    }
}

interface MyInterface {
    String param = null;
    void method1();
    void method2();
    // 默认方法
    default void defaultMethod() {
        System.out.println("default method");
    }
    // 私有方法
    private void privateMethod() {
        System.out.println("private method");
    }
    // 静态方法
    static void staticMethod() {
        System.out.println("static method");
    }
    // 私有静态方法
    private static void privateStaticMethod() {
        System.out.println("private static method");
    }
}
interface IMobilePhone{
    void mehtod1();
}
interface MyInterface2{
    void mehtod2();
}
class IPhone14 implements IMobilePhone{
    @Override
    public void mehtod1() {
    }

}
class Xiaomi14 implements IMobilePhone{
    @Override
    public void mehtod1() {
    }

}
