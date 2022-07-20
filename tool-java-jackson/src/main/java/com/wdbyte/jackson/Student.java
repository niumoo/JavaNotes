package com.wdbyte.jackson;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/17
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Integer age;

    @Getter
    @Setter
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

}
