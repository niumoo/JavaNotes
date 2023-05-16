package com.wdbyte;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


/**
 * <p>
 * 方法引用，通过方法的名字指向一个方法
 * 
 * - Supplier<T>: 数据提供器，可以提供 T 类型对象；无参的构造器，提供了 get 方法<br/>
 * - Function<T,R>: 数据转换器，接收一个 T 类型的对象，返回一个 R类型的对象； 单参数单返回值的行为接口；提供了 apply, compose, andThen, identity 方法<br/>
 * - Consumer<T>: 数据消费器， 接收一个 T类型的对象，无返回值，通常用于设置T对象的值； 单参数无返回值的行为接口；提供了 accept, andThen 方法<br/>
 * - Predicate<T>: 条件测试器，接收一个 T 类型的对象，返回布尔值，通常用于传递条件函数； 单参数布尔值的条件性接口。提供了 test (条件测试) , and-or- negate(与或非) 方法。
 *
*  @Author https://www.wdbyte.com
 * @Date 2019/2/18 22:08
 */
public class Jdk8Function {

    public void testFunction() {
        // 构造器引用
        final Car bmwCar = Car.create(Car::new);
        bmwCar.setName("BMW");
        final Car audiCar = Car.create(Car::new);
        audiCar.setName("AUDI");
        List<Car> cars = Arrays.asList(bmwCar, audiCar);
        // 静态方法引用
        cars.forEach(Car::collide);
        // 特定类的任意对象的方法引用
        cars.forEach(Car::repair);
        // 特定对象的方法引用
        cars.forEach(bmwCar::follow);
    }

    /**
     * 函数接口
     */
    public void functionInterfaceTest() {
        List<String> skills = Arrays.asList("java", "golang", "c++", "c", "python");
        Predicate<String> length4 = (str) -> str.length() > 4;
        filterByForeach(skills, length4);
        System.out.println("----------------------");
        filterByFilter(skills, length4);
    }

    /**
     * 接收函数参数
     *
     * @param list
     * @param condition
     */
    public void filterByForeach(List list, Predicate condition) {
        list.forEach(a -> {
            if (condition.test(a)) {
                System.out.print(a + " ");
            }
        });
    }

    /**
     * 接收函数参数
     *
     * @param list
     * @param condition
     */
    public void filterByFilter(List list, Predicate condition) {
        list.stream().filter(a -> condition.test(a)).forEach(a -> System.out.print(a + (" ")));
    }

    /**
     * predicate 的复杂使用
     */
    public void predicateTest() {
        Predicate<String> startsWith = (str) -> str.startsWith("g");
        Predicate<String> length = (str) -> str.length() > 4;

        List<String> skills = Arrays.asList("java", "golang", "c++", "c", "python");
        skills.stream().filter(startsWith.and(length)).forEach((skill) -> {
            System.out.print(skill + " ");
        });
    }

}

class Car {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Car create(final Supplier<Car> supplier) {
        return supplier.get();
    }

    public static void collide(final Car car) {
        System.out.println("Collied:" + car.getName());
    }

    public void follow(final Car another) {
        System.out.println("Following the:" + another.getName());
    }

    public void repair() {
        System.out.println("Repaired:" + this.getName());
    }

}

@FunctionalInterface
interface Supplier<T> {
    /**
     * 获取元素
     *
     * @return
     */
    T get();
}