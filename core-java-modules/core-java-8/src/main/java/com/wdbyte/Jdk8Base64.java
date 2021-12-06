package com.wdbyte;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <p>
 * JDK8 对 base64 编码的支持
 *
 * @Author niujinpeng
 * @Date 2019/6/12 9:47
 */
public class Jdk8Base64 {

    public void base64Test() {
        String text = "1234567";
        // 编码
        String encoded = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        System.out.println(encoded);

        // 解码
        String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        System.out.println(decoded);

        // url 编码
        String url = "https://www.baidu.com/s?wd=jdk8";
        String urlEncoded = Base64.getUrlEncoder().encodeToString(url.getBytes(StandardCharsets.UTF_8));
        System.out.println(urlEncoded);
        String urldecoded = new String(Base64.getUrlDecoder().decode(urlEncoded), StandardCharsets.UTF_8);
        System.out.println(urldecoded);

        // Result
        // MTIzNDU2Nw==
        // 1234567
    }
}
