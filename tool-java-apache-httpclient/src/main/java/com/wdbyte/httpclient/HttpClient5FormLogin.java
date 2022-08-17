package com.wdbyte.httpclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

/**
 * 演示基于表单的登录
 *
 * @author https://www.wdbyte.com
 */
public class HttpClient5FormLogin {

    public static void main(final String[] args) throws Exception {
        final BasicCookieStore cookieStore = new BasicCookieStore();
        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build()) {

            // 本应该使用 POST 请求发送表单参数，但是在 httpbin.org 中没有对应的接口用于测试，所以这里换成了 GET 请求
            // HttpPost httpPost = new HttpPost("http://httpbin.org/cookies/set/username/wdbyte.com");
            HttpGet httpPost = new HttpGet("http://httpbin.org/cookies/set/username/wdbyte.com");
            // POST 表单请求参数
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("username", "wdbyte.com"));
            nvps.add(new BasicNameValuePair("password", "secret"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try (final CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
                final HttpEntity entity = response2.getEntity();

                System.out.println("Login form get: " + response2.getCode() + " " + response2.getReasonPhrase());
                System.out.println("当前响应信息 "+EntityUtils.toString(entity));;

                System.out.println("Post 登录 Cookie:");
                final List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i));
                    }
                }
            }
        }
    }
}