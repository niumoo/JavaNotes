package com.wdbyte;

/**
 * <p>
 * 接口的静态方法和默认方法
 *
 *
*  @Author https://www.wdbyte.com
 * @Date 2019/2/18 22:52
 */
public class Jdk8Interface {
    public static void main(String[] args) {
        // 接口静态方法
        Person.say();
        // 接口默认方法
        Person southerner = new Southerner();
        southerner.eat();
        // 接口重写方法
        Northerners northerners = new Northerners();
        northerners.eat();

        /**
         * result<br/>
         * 你好啊<br/>
         * 吃米饭<br/>
         * 吃馒头<br/>
         */
    }
}

/**
 * 南方人
 */
class Southerner implements Person {

}

/**
 * 北方人
 */
class Northerners implements Person {
    @Override
    public void eat() {
        System.out.println("吃馒头");
    }
}

/**
 * 多个接口有相同的默认方法必须重写方法
 */
class PersonImpl implements Person, Person2 {

    @Override
    public void eat() {
        System.out.println("吃米饭吃粥");
    }
}

interface Person {
    /**
     * 接口静态方法
     */
    static void say() {
        System.out.println("你好啊");
    }

    /**
     * 接口默认方法
     */
    default void eat() {
        System.out.println("吃米饭");
    }
}

/**
 *
 */
interface Person2 {
    default void eat() {
        System.out.println("吃粥");
    }
}