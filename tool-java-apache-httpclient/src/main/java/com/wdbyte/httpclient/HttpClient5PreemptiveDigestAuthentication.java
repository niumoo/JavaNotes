package com.wdbyte.httpclient;

import org.apache.hc.client5.http.auth.AuthExchange;
import org.apache.hc.client5.http.auth.AuthScheme;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.DigestScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * GET /digest-auth/auth/user/1234567 HTTP/1.1
 * Accept-Encoding: gzip, x-gzip, deflate
 * Host: httpbin.org
 * Connection: keep-alive
 * User-Agent: Apache-HttpClient/5.1.3 (Java/17)
 *
 * HTTP/1.1 401 UNAUTHORIZED
 * Date: Mon, 27 Jun 2022 01:14:33 GMT
 * Content-Type: text/html; charset=utf-8
 * Content-Length: 0
 * Connection: keep-alive
 * Server: gunicorn/19.9.0
 * WWW-Authenticate: Digest realm="me@kennethreitz.com", nonce="5555b4af2e521ae090278b68bdc0a514", qop="auth",
 * opaque="ba9c52e5ba8efb290bbf44404c9df42c", algorithm=MD5, stale=FALSE
 * Set-Cookie: stale_after=never; Path=/
 * Set-Cookie: fake=fake_value; Path=/
 * Access-Control-Allow-Origin: *
 * Access-Control-Allow-Credentials: true
 *
 * GET /digest-auth/auth/user/1234567 HTTP/1.1
 * Host: httpbin.org
 * Connection: keep-alive
 * User-Agent: Apache-HttpClient/5.1.3 (Java/17)
 * Cookie: fake=fake_value; stale_after=never
 * Authorization: Digest username="user", realm="me@kennethreitz.com", nonce="5555b4af2e521ae090278b68bdc0a514",
 * uri="/digest-auth/auth/user/1234567", response="ceef33b582fcbe9b15157d29a75f4781", qop=auth, nc=00000001,
 * cnonce="085c5561765f5b9a", algorithm=MD5, opaque="ba9c52e5ba8efb290bbf44404c9df42c"
 *
 * HTTP/1.1 200 OK
 * Date: Mon, 27 Jun 2022 01:14:33 GMT
 * Content-Type: application/json
 * Content-Length: 47
 * Connection: keep-alive
 * Server: gunicorn/19.9.0
 * Set-Cookie: fake=fake_value; Path=/
 * Set-Cookie: stale_after=never; Path=/
 * Access-Control-Allow-Origin: *
 * Access-Control-Allow-Credentials: true
 *
 * {
 *   "authenticated": true,
 *   "user": "user"
 * }
 *
 * HttpClient如何验证多个请求的示例
 * 使用相同的摘要方案。在初始请求/响应交换之后
 * 共享相同执行上下文的所有后续请求都可以重用
 * 要向服务器进行身份验证的最后一个摘要nonce值。
 */
public class HttpClient5PreemptiveDigestAuthentication {

    public static void main(final String[] args) throws Exception {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {

            final HttpHost target = new HttpHost("http", "httpbin.org", 80);

            final HttpClientContext localContext = HttpClientContext.create();
            final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new AuthScope(target),
                    new UsernamePasswordCredentials("admin", "123456".toCharArray()));
            localContext.setCredentialsProvider(credentialsProvider);

            final HttpGet httpget = new HttpGet("http://httpbin.org/digest-auth/auth/admin/123456");

            System.out.println("执行请求 " + httpget.getMethod() + " " + httpget.getUri());
            for (int i = 0; i < 2; i++) {
                try (final CloseableHttpResponse response = httpclient.execute(target, httpget, localContext)) {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getCode() + " " + response.getReasonPhrase());
                    EntityUtils.consume(response.getEntity());

                    final AuthExchange authExchange = localContext.getAuthExchange(target);
                    if (authExchange != null) {
                        final AuthScheme authScheme = authExchange.getAuthScheme();
                        if (authScheme instanceof DigestScheme) {
                            final DigestScheme digestScheme = (DigestScheme) authScheme;
                            System.out.println("Nonce: " + digestScheme.getNonce() +
                                    "; count: " + digestScheme.getNounceCount());
                        }
                    }
                }
            }
        }
    }

}