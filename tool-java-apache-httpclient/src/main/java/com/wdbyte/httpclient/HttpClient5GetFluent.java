package com.wdbyte.httpclient;

import java.io.IOException;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;

/**
* @author https://www.wdbyte.com
 */
public class HttpClient5GetFluent {

    public static void main(String[] args) {
        System.out.println(get("http://httpbin.org/get"));
    }

    public static String get(String url) {
        String result = null;
        try {
            Response response = Request.get(url).execute();
            result = response.returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
