package com.wdbyte.jackson;

import java.util.List;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/16
 */
public class Person {
    private String name;
    private Integer age;
    private List<String> skillList;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", skillList=" + skillList +
                '}';
    }

    public Person(String name, Integer age, List<String> skillList) {
        this.name = name;
        this.age = age;
        this.skillList = skillList;
    }

    public Person() {
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

    public List<String> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<String> skillList) {
        this.skillList = skillList;
    }
}

