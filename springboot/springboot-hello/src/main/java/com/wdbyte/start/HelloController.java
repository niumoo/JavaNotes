package com.wdbyte.start;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author https://www.wdbyte.com
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(String username) {
        if (username == null) {
            return "Hello,Who are you?";
        }
        return "Hello," + username;
    }

}
