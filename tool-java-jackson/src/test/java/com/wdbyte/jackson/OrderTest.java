package com.wdbyte.jackson;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/17
 */
class OrderTest {

    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    //ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testPojoToJson() throws JsonProcessingException {
        Order order = new Order(1, new Date(), LocalDateTime.now());
        String json = objectMapper.writeValueAsString(order);
        System.out.println(json);

        order = objectMapper.readValue(json, Order.class);
        System.out.println(order.toString());

        Assertions.assertEquals(order.getId(), 1);
    }

    @Test
    void testPojoToJson0() throws JsonProcessingException {
        Order order = new Order(1, new Date(), null);
        String json = objectMapper.writeValueAsString(order);
        System.out.println(json);

        order = objectMapper.readValue(json, Order.class);
        System.out.println( order.toString());

        Assertions.assertEquals(order.getId(), 1);
    }

}