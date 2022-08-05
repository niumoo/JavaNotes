package com.wdbyte.httpclient;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

/**
 * @author https://www.wdbyte.com
 */
public class HttpClient5PostWithJson {

    /**
     * {
     * "args": {},
     * "data": "{    \"password\": \"secret\",    \"username\": \"wdbyte.com\"}",
     * "files": {},
     * "form": {},
     * "headers": {
     * "Accept-Encoding": "gzip, x-gzip, deflate",
     * "Content-Length": "55",
     * "Content-Type": "text/plain; charset=ISO-8859-1",
     * "Host": "httpbin.org",
     * "User-Agent": "Apache-HttpClient/5.1.3 (Java/17)",
     * "X-Amzn-Trace-Id": "Root=1-62b6ab18-2aec3a5731e325620f1f5717"
     * },
     * "json": {
     * "password": "secret",
     * "username": "wdbyte.com"
     * },
     * "origin": "42.120.74.8",
     * "url": "http://httpbin.org/post"
     * }
     *
     * @param args
     */
    public static void main(String[] args) {
        String json = "{"
            + "    \"password\": \"secret\","
            + "    \"username\": \"wdbyte.com\""
            + "}";
        String result = post("http://httpbin.org/post", json);
        System.out.println(result);
    }

    public static String post(String url, String jsonBody) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                // 获取响应信息
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}
