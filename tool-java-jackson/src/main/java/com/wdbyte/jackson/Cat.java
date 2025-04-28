package com.wdbyte.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/17
 */
public class Cat {

    @JsonSetter(value = "catName")
    private String name;

    private Integer age;

    @JsonGetter(value = "catName")
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

    public Cat() {
    }

    public Cat(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
