package com.wdbyte.jackson;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/17
 */
class StudentTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJsonToPojo() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "aLang");
        map.put("age", 18);
        map.put("skill", "java");

        String json = objectMapper.writeValueAsString(map);
        System.out.println(json);

        Student student = objectMapper.readValue(json, Student.class);
        System.out.println(student);

        Assertions.assertEquals(student.getDiyMap().get("skill"), "java");
    }

    @Test
    void testPojoToJsonTest() throws JsonProcessingException {
        Student student = new Student();
        student.setName("aLang");
        student.setAge(20);
        String json = objectMapper.writeValueAsString(student);
        System.out.println(json);

        Assertions.assertEquals(json,"{\"name\":\"aLang\",\"age\":20,\"diyMap\":{},\"a\":111,\"b\":222,\"c\":333}");
    }

}