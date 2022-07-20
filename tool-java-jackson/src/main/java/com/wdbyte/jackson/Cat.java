package com.wdbyte.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/17
 */
@Data
public class Cat {

    @JsonSetter(value = "catName")
    private String name;

    private Integer age;

    @JsonGetter(value = "catName")
    public String getName() {
        return name;
    }
}
