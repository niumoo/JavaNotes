package com.wdbyte.springsqlite.controller;

import java.time.LocalDateTime;

import com.wdbyte.springsqlite.model.WebsiteUser;
import com.wdbyte.springsqlite.repository.WebsiteUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author https://www.wdbyte.com
 */
@Slf4j
@RestController
public class SqliteController {

    @Autowired
    private WebsiteUserRepository userRepository;

    @GetMapping("/sqlite/init")
    public String init() {
        for (int i = 0; i < 10; i++) {
            WebsiteUser websiteUser = new WebsiteUser();
            // 随机4个字母
            websiteUser.setUsername(RandomStringUtils.randomAlphabetic(4));
            // 随机16个字符用于密码加盐加密
            websiteUser.setSalt(RandomStringUtils.randomAlphanumeric(16));
            String password = "123456";
            // 密码存储 = md5(密码+盐)
            password = password + websiteUser.getSalt();
            websiteUser.setPassword(DigestUtils.md5Hex(password));
            websiteUser.setCreatedAt(LocalDateTime.now());
            websiteUser.setUpdatedAt(LocalDateTime.now());
            websiteUser.setStatus("active");
            WebsiteUser saved = userRepository.save(websiteUser);
            log.info("init user {}", saved.getUsername());
        }
        return "init success";
    }

    @GetMapping("/sqlite/find")
    public String findByUsername(String username) {
        WebsiteUser websiteUser = userRepository.findByUsername(username);
        if (websiteUser == null) {
            return null;
        }
        return websiteUser.toString();
    }

    @GetMapping("/sqlite/login")
    public String findByUsername(String username, String password) {
        WebsiteUser websiteUser = userRepository.findByUsername(username);
        if (websiteUser == null) {
            return "login failed";
        }
        password = password + websiteUser.getSalt();
        if (StringUtils.equals(DigestUtils.md5Hex(password), websiteUser.getPassword())) {
            return "login succeeded";
        } else {
            return "login failed";
        }
    }
}
