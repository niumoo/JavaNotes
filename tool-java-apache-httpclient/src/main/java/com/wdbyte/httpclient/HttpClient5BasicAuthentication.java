package com.wdbyte.httpclient;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * 一个简单的示例,它使用HttpClient执行HTTP请求;
 * 一个需要进行用户身份验证的目标站点。
 * Basic Authorization
 *
 * GET /basic-auth/user/passwd HTTP/1.1
 * Accept-Encoding: gzip, x-gzip, deflate
 * Host: httpbin.org
 * Connection: keep-alive
 * User-Agent: Apache-HttpClient/5.1.3 (Java/1.8.0_151)
 *
 * HTTP/1.1 401 UNAUTHORIZED
 * Date: Sat, 06 Aug 2022 08:25:33 GMT
 * Content-Length: 0
 * Connection: keep-alive
 * Server: gunicorn/19.9.0
 * WWW-Authenticate: Basic realm="Fake Realm"
 * Access-Control-Allow-Origin: *
 * Access-Control-Allow-Credentials: true
 *
 * GET /basic-auth/user/passwd HTTP/1.1
 * Host: httpbin.org
 * Connection: keep-alive
 * User-Agent: Apache-HttpClient/5.1.3 (Java/1.8.0_151)
 * Authorization: Basic dXNlcjpwYXNzd2Q=
 *
 * HTTP/1.1 200 OK
 * Date: Sat, 06 Aug 2022 08:25:33 GMT
 * Content-Type: application/json
 * Content-Length: 47
 * Connection: keep-alive
 * Server: gunicorn/19.9.0
 * Access-Control-Allow-Origin: *
 * Access-Control-Allow-Credentials: true
 *
 * {
 *   "authenticated": true,
 *   "user": "user"
 * }
 */
public class HttpClient5BasicAuthentication {

    public static void main(final String[] args) throws Exception {
        final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("httpbin.org", 80),
                new UsernamePasswordCredentials("admin", "123456".toCharArray()));
        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build()) {
            final HttpGet httpget = new HttpGet("http://httpbin.org/basic-auth/admin/123456");

            System.out.println("执行请求" + httpget.getMethod() + " " + httpget.getUri());
            try (final CloseableHttpResponse response = httpclient.execute(httpget)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }
}