package com.wdbyte.httpclient;

import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * 这个例子演示了使用本地HTTP上下文填充, 自定义属性
 */
public class HttpClient5WithCookie {

    public static void main(final String[] args) throws Exception {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // 创建一个本地的 Cookie 存储
            final CookieStore cookieStore = new BasicCookieStore();
            //BasicClientCookie clientCookie = new BasicClientCookie("name", "www.wdbyte.com");
            //clientCookie.setDomain("http://httpbin.org/cookies");
            // 过期时间
            //clientCookie.setExpiryDate(new Date());
            // 添加到本地 Cookie
            //cookieStore.addCookie(clientCookie);

            // 创建本地 HTTP 请求上下文 HttpClientContext
            final HttpClientContext localContext = HttpClientContext.create();
            // 绑定 cookieStore 到 localContext
            localContext.setCookieStore(cookieStore);

            final HttpGet httpget = new HttpGet("http://httpbin.org/cookies/set/cookieName/www.wdbyte.com");
            System.out.println("执行请求 " + httpget.getMethod() + " " + httpget.getUri());

            // 获取 Coolie 信息
            try (final CloseableHttpResponse response = httpclient.execute(httpget, localContext)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                final List<Cookie> cookies = cookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("Local cookie: " + cookies.get(i));
                }
                EntityUtils.consume(response.getEntity());
            }
        }
    }

}