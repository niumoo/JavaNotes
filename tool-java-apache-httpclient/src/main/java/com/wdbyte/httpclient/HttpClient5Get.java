package com.wdbyte.httpclient;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
* @author https://www.wdbyte.com
 * @date 2022/04/01
 */
public class HttpClient5Get {

    public static void main(String[] args) {
        String result = get("http://httpbin.org/get");
        System.out.println(result);
    }

    public static String get(String url) {
        String resultContent = null;
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                // 获取状态码
                System.out.println(response.getVersion()); // HTTP/1.1
                System.out.println(response.getCode()); // 200
                System.out.println(response.getReasonPhrase()); // OK
                HttpEntity entity = response.getEntity();
                // 获取响应信息
                resultContent = EntityUtils.toString(entity);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return resultContent;
    }

}
