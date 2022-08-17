package com.wdbyte.httpclient;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * 如何通过代理发送请求。
 *
* @author https://www.wdbyte.com
 * @date 2022/06/26
 */
public class HttpClient5ExecuteProxy {

    public static void main(String[] args) {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpHost target = new HttpHost("https", "httpbin.org", 443);
            final HttpHost proxy = new HttpHost("http", "127.0.0.1", 8080);

            final RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
            final HttpGet request = new HttpGet("/get");
            request.setConfig(config);

            System.out.println("执行请求 " + request.getMethod() + " " + request.getUri() + " via " + proxy);

            try (final CloseableHttpResponse response = httpclient.execute(target, request)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException | ParseException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

