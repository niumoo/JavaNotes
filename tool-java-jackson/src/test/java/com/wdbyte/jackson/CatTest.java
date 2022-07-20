package com.wdbyte.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/17
 */
class CatTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testPojoToJson() throws JsonProcessingException {
        Cat cat = new Cat();
        cat.setName("Tom");
        cat.setAge(2);
        String json = objectMapper.writeValueAsString(cat);
        System.out.println(json);

        Assertions.assertEquals(json, "{\"name\":\"Tom\"}");

        cat = objectMapper.readValue(json, Cat.class);
        Assertions.assertEquals(cat.getName(), "Tom");
        Assertions.assertEquals(cat.getAge(), null);
    }

    @Test
    void testJsonToPojo() throws JsonProcessingException {
        String json = "{\"name\":\"tom\"}";
        Cat cat = objectMapper.readValue(json, Cat.class);
        Assertions.assertEquals(cat.getName(), "Tom");
        Assertions.assertEquals(cat.getAge(), null);
    }

    @Test
    void testPojoToJson2() throws JsonProcessingException {
        String json = "{\"age\":2,\"catName\":\"Tom\"}";
        Cat cat = objectMapper.readValue(json, Cat.class);
        System.out.println(cat.toString());
        Assertions.assertEquals(cat.getName(), "Tom");
    }

}