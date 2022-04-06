package com.wdbyte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Lambda 的语法主要是下面几种。
 * 1. (params) -> expression
 * 2. (params) -> statement
 * 3. (params) -> {statements;}
 * Lambda 的语法特性。
 * <p>
 * 1. 使用 `->` 分割 Lambda 参数和处理语句。
 * 2. 类型可选，可以不指定参数类型，编译器可以自动判断。
 * 3. 圆括号可选，如果只有一个参数，可以不需要圆括号，多个参数必须要圆括号。
 * 4. 花括号可选，一个语句可以不用花括号，多个参数则花括号必须。
 * 5. 返回值可选，如果只有一个表达式，可以自动返回不需要 return 语句，花括号中需要 return 语法。
 *
 * @Author niujinpeng
 * @Date 2019/2/17 14:48
 */
public class Jdk8Lambda {

    /**
     * Lambda 的使用，使用 Runnable 例子
     *
     * @throws InterruptedException
     */
    @Test
    public void createLambda() throws InterruptedException {
        // 使用 Lambda 之前
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("JDK8 之前的线程创建");
            }
        };
        new Thread(runnable).start();

        // 使用 Lambda 之后
        Runnable runnable1Jdk8 = () -> System.out.println("JDK8 之后的线程创建");
        new Thread(runnable1Jdk8).start();

        // 更加紧凑的方式
        new Thread(() -> System.out.println("JDK8 之后的线程创建")).start();
    }

    /**
     * 定义函数接口
     */
    @FunctionalInterface
    public interface FunctionInterfaceDemo {
        void say(String name, int age);
    }

    /**
     * 函数接口，Lambda 测试
     */
    @Test
    public void functionLambdaTest() {
        FunctionInterfaceDemo demo = (name, age) -> System.out.println("我叫" + name + "，我今年" + age + "岁");
        demo.say("金庸", 99);
    }


    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    static class User {
        private String name;
        private Integer age;
    }

    public static List<User> userList = new ArrayList<User>();

    static {
        userList.add(new User("A", 26));
        userList.add(new User("B", 18));
        userList.add(new User("C", 23));
        userList.add(new User("D", 19));
    }

    /**
     * 测试方法引用
     */
    @Test
    public void methodRef() {
        User[] userArr = new User[userList.size()];
        userList.toArray(userArr);
        // User::getAge 调用 getAge 方法
        Arrays.sort(userArr, Comparator.comparing(User::getAge));
        for (User user : userArr) {
            System.out.println(user);
        }
    }

    /**
     * 排序输出
     */
    @Test
    public void testSortBeforeJdk8() {
        // 使用 Lambda 之前
        userList.sort(new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u1.getAge() - u2.getAge();
            }
        });
        for (User user : userList) {
            System.out.println(user);
        }
        System.out.println("------------------------");

        // 使用 Lambda 之后
        // 方式1
        userList.sort((User u1, User u2) -> u1.getAge() - u2.getAge());
        userList.forEach(System.out::println);
        System.out.println("------------------------");
        // 方式2
        userList.sort((u1, u2) -> u1.getAge().compareTo(u2.getAge()));
        userList.forEach(System.out::println);
        System.out.println("------------------------");
        // 方式3
        userList.sort(Comparator.comparing(User::getAge));
        userList.forEach(System.out::println);
        System.out.println("------------------------");
        // 方式4
        userList.stream().sorted(Comparator.comparing(User::getAge)).forEach(System.out::println);
    }

    /**
     * 新的遍历方式
     */
    @Test
    public void foreachTest() {
        List<String> skills = Arrays.asList("java", "golang", "c++", "c", "python");
        // 使用 Lambda 之前
        for (String skill : skills) {
            System.out.print(skill + ",");
        }
        System.out.println();

        // 使用 Lambda 之后
        // 方式1,forEach+lambda
        skills.forEach((skill) -> System.out.print(skill + ","));
        System.out.println();
        // 方式2，forEach+方法引用
        skills.forEach(System.out::print);
    }

    /**
     * Lambda 对于流的操作
     */
    @Test
    public void streamTest() {
        List<String> skills = Arrays.asList("java", "golang", "c++", "c", "python", "java");
        // Jdk8 之前
        for (String skill : skills) {
            System.out.print(skill + ",");
        }
        System.out.println();

        // Jdk8 之后-去重遍历
        skills.stream().distinct().forEach(skill -> System.out.print(skill + ","));
        System.out.println();
        // Jdk8 之后-去重遍历
        skills.stream().distinct().forEach(System.out::print);
        System.out.println();
        // Jdk8 之后-去重，过滤掉 ptyhon 再遍历
        skills.stream().distinct().filter(skill -> skill != "python").forEach(skill -> System.out.print(skill + ","));
        System.out.println();

        // Jdk8 之后转字符串
        String skillString = String.join(",", skills);
        System.out.println(skillString);
    }

    /**
     * 数据转换
     */
    @Test
    public void mapTest() {
        List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5);
        // 数据转换
        numList.stream().map(num -> num * num).forEach(num -> System.out.print(num + ","));

        System.out.println();

        // 数据收集
        Set<Integer> numSet = numList.stream().map(num -> num * num).collect(Collectors.toSet());
        numSet.forEach(num -> System.out.print(num + ","));
    }

    /**
     * 数学计算测试
     */
    @Test
    public void mapMathTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        IntSummaryStatistics stats = list.stream().mapToInt(x -> x).summaryStatistics();
        System.out.println("最小值：" + stats.getMin());
        System.out.println("最大值：" + stats.getMax());
        System.out.println("个数：" + stats.getCount());
        System.out.println("和：" + stats.getSum());
        System.out.println("平均数：" + stats.getAverage());

        // 求和的另一种方式
        Integer integer = list.stream().reduce((sum, cost) -> sum + cost).get();
        System.out.println(integer);
    }
}