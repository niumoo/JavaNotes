package com.wdbyte.httpclient;

import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * 抢先式身份认证
 * 注意安全性问题
 */
public class HttpClient5PreemptiveBasicAuthentication {

    public static void main(final String[] args) throws Exception {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {

            // 生成一个认证信息
            final BasicScheme basicAuth = new BasicScheme();
            basicAuth.initPreemptive(new UsernamePasswordCredentials("admin", "123456".toCharArray()));
            // 认证信息的生效域名信息
            final HttpHost target = new HttpHost("http", "httpbin.org", 80);
            // 添加认证信息到 HttpClient 请求上下文中
            final HttpClientContext localContext = HttpClientContext.create();
            localContext.resetAuthExchange(target, basicAuth);

            //AuthScope authScope = new AuthScope("httpbin.org", 80);
            //BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
            //credsProvider.setCredentials(authScope,
            //    new UsernamePasswordCredentials("admin", "1234556".toCharArray()));

            AuthCache authCache = new BasicAuthCache();
            authCache.put(target, new BasicScheme());
            //localContext.setCredentialsProvider(credsProvider);
            localContext.setAuthCache(authCache);

            final HttpGet httpget = new HttpGet("http://httpbin.org/basic-auth/admin/123456");

            System.out.println("执行请求 " + httpget.getMethod() + " " + httpget.getUri());
            for (int i = 0; i < 3; i++) {
                try (final CloseableHttpResponse response = httpclient.execute(httpget, localContext)) {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getCode() + " " + response.getReasonPhrase());
                    System.out.println(EntityUtils.toString(response.getEntity()));
                }
            }
        }
    }

}