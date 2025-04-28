package com.wdbyte.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/17
 */
public class Student {
    private String name;

    private Integer age;

    private Map<String, Object> diyMap = new HashMap<>();

    @JsonAnyGetter
    private Map<String, Object> initMap = new HashMap() {{
        put("a", 111);
        put("b", 222);
        put("c", 333);
    }};

    @JsonAnySetter
    public void otherField(String key, String value) {
        this.diyMap.put(key, value);
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

    public Map<String, Object> getDiyMap() {
        return diyMap;
    }

    public void setDiyMap(Map<String, Object> diyMap) {
        this.diyMap = diyMap;
    }

    public Map<String, Object> getInitMap() {
        return initMap;
    }

    public void setInitMap(Map<String, Object> initMap) {
        this.initMap = initMap;
    }

    public Student() {
    }

    public Student(String name, Integer age, Map<String, Object> diyMap, Map<String, Object> initMap) {
        this.name = name;
        this.age = age;
        this.diyMap = diyMap;
        this.initMap = initMap;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", diyMap=" + diyMap +
                ", initMap=" + initMap +
                '}';
    }
}
