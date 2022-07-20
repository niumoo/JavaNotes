package com.wdbyte.jackson;

import java.util.List;

import lombok.Data;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/16
 */
@Data
public class Person {
    private String name;
    private Integer age;
    private List<String> skillList;
}

