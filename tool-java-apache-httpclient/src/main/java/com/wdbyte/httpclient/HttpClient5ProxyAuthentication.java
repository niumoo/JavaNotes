package com.wdbyte.httpclient;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * A simple example that uses HttpClient to execute an HTTP request
 * over a secure connection tunneled through an authenticating proxy.
 */
public class HttpClient5ProxyAuthentication {

    public static void main(final String[] args) throws Exception {
        final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials( // 代理和认证信息
            new AuthScope("localhost", 8888),
            new UsernamePasswordCredentials("squid", "squid".toCharArray()));
        credsProvider.setCredentials( // 目标和认证信息
            new AuthScope("httpbin.org", 80),
            new UsernamePasswordCredentials("user", "passwd".toCharArray()));
        try (final CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultCredentialsProvider(credsProvider).build()) {
            final HttpHost target = new HttpHost("http", "httpbin.org", 80);
            final HttpHost proxy = new HttpHost("localhost", 8888);

            final RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
            final HttpGet httpget = new HttpGet("/basic-auth/user/passwd");
            httpget.setConfig(config);

            System.out.println("执行请求 " + httpget.getMethod() + " " + httpget.getUri() + " via " + proxy);

            try (final CloseableHttpResponse response = httpclient.execute(target, httpget)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }
}