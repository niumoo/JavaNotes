package com.wdbyte;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author https://www.wdbyte.com
 * @date 2021/07/21
 */
public class Dog {

    private String name;
    private Integer age;

    public Dog() {
    }

    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(
            Paths.get("/Users/darcy/git/java-core/java-8/target/classes/com/wdbyte/JEP371Test.class"));
        String toString = Base64.getEncoder().encodeToString(bytes);
        System.out.println(toString);
    }

    public Dog(String name) {
        this.name = name;
    }

    public Dog(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Dog{" +
            ", name='" + name + '\'' +
            ", age=" + age +
            '}';
    }
}
