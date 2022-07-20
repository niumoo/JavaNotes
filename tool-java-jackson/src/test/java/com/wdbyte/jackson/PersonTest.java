package com.wdbyte.jackson;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/16
 */
class PersonTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void pojoToJsonString() throws JsonProcessingException {
        Person person = new Person();
        person.setName("aLng");
        person.setAge(27);
        person.setSkillList(Arrays.asList("java", "c++"));

        String json = objectMapper.writeValueAsString(person);
        System.out.println(json);
        String expectedJson = "{\"name\":\"aLng\",\"age\":27,\"skillList\":[\"java\",\"c++\"]}";
        Assertions.assertEquals(json, expectedJson);
    }

    @Test
    void jsonStringToPojo() throws JsonProcessingException {
        String expectedJson = "{\"name\":\"aLang\",\"age\":27,\"skillList\":[\"java\",\"c++\"]}";
        Person person = objectMapper.readValue(expectedJson, Person.class);
        System.out.println(person);
        Assertions.assertEquals(person.getName(), "aLang");
        Assertions.assertEquals(person.getSkillList().toString(), "[java, c++]");
    }

    @Test
    void testJsonFilePojo() throws IOException {
        File file = new File("src/Person.json");
        Person person = objectMapper.readValue(file, Person.class);
        System.out.println(person);
        Assertions.assertEquals(person.getName(), "aLang");
        Assertions.assertEquals(person.getSkillList().toString(), "[java, c++]");
    }

    @Test
    void jsonBytesToPojo() throws IOException {
        String expectedJson = "{\"name\":\"aLang\",\"age\":27,\"skillList\":[\"java\",\"c++\"]}";
        Person person = objectMapper.readValue(expectedJson.getBytes(), Person.class);
        System.out.println(person);
        Assertions.assertEquals(person.getName(), "aLang");
        Assertions.assertEquals(person.getSkillList().toString(), "[java, c++]");
    }

    @Test
    void fileToPojoList() throws IOException {
        File file = new File("src/EmployeeList.json");
        List<Person> personList = objectMapper.readValue(file, new TypeReference<List<Person>>() {});
        for (Person person : personList) {
            System.out.println(person);
        }
        Assertions.assertEquals(personList.size(), 2);
        Assertions.assertEquals(personList.get(0).getName(), "aLang");
        Assertions.assertEquals(personList.get(1).getName(), "darcy");
    }

    @Test
    void jsonStringToMap() throws IOException {
        String expectedJson = "{\"name\":\"aLang\",\"age\":27,\"skillList\":[\"java\",\"c++\"]}";
        Map<String, Object> employeeMap = objectMapper.readValue(expectedJson, new TypeReference<Map>() {});
        System.out.println(employeeMap.getClass());
        for (Entry<String, Object> entry : employeeMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        Assertions.assertEquals(employeeMap.get("name"), "aLang");
    }


    @Test
    void jsonStringToPojoIgnoreProperties() throws IOException {
        // UnrecognizedPropertyException
        String json = "{\"yyy\":\"xxx\",\"name\":\"aLang\",\"age\":27,\"skillList\":[\"java\",\"c++\"]}";
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Person person = objectMapper.readValue(json, Person.class);
        System.out.printf(person.toString());
        Assertions.assertEquals(person.getName(), "aLang");
        Assertions.assertEquals(person.getSkillList().toString(), "[java, c++]");
    }

}