package com.wdbyte.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

/**
* @author https://www.wdbyte.com
 */
public class HttpClient5Post {

    public static void main(String[] args) {
        String result = post("http://httpbin.org/post");
        System.out.println(result);
    }

    /**
     * {
     * "args": {},
     * "data": "",
     * "files": {},
     * "form": {
     * "password": "secret",
     * "username": "wdbyte.com"
     * },
     * "headers": {
     * "Accept-Encoding": "gzip, x-gzip, deflate",
     * "Content-Length": "31",
     * "Content-Type": "application/x-www-form-urlencoded; charset=ISO-8859-1",
     * "Host": "httpbin.org",
     * "User-Agent": "Apache-HttpClient/5.1.3 (Java/17)",
     * "X-Amzn-Trace-Id": "Root=1-62ab2708-4425003d3641a0a75cfeda74"
     * },
     * "json": null,
     * "origin": "47.251.4.198",
     * "url": "http://httpbin.org/post"
     * }
     */
    public static String post(String url) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        // 表单参数
        List<NameValuePair> nvps = new ArrayList<>();
        // POST 请求参数
        nvps.add(new BasicNameValuePair("username", "wdbyte.com"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                System.out.println(response.getVersion()); // HTTP/1.1
                System.out.println(response.getCode()); // 200
                System.out.println(response.getReasonPhrase()); // OK

                HttpEntity entity = response.getEntity();
                // 获取响应信息
                result = EntityUtils.toString(entity);
                // 确保流被完全消费
                EntityUtils.consume(entity);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}
