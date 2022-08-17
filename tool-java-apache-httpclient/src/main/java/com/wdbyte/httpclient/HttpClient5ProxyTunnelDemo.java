package com.wdbyte.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.classic.ProxyClient;
import org.apache.hc.core5.http.HttpHost;

/**
 * Example code for using {@link ProxyClient} in order to establish a tunnel through an HTTP proxy.
 */
public class HttpClient5ProxyTunnelDemo {

    public final static void main(final String[] args) throws Exception {

        final ProxyClient proxyClient = new ProxyClient();
        final HttpHost target = new HttpHost("www.yahoo.com", 80);
        final HttpHost proxy = new HttpHost("127.0.0.1", 9090);
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("user", "pwd".toCharArray());
        try (final Socket socket = proxyClient.tunnel(proxy, target, credentials)) {
            final Writer out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1);
            out.write("GET / HTTP/1.1\r\n");
            out.write("Host: " + target.toHostString() + "\r\n");
            out.write("Agent: whatever\r\n");
            out.write("Connection: close\r\n");
            out.write("\r\n");
            out.flush();
            final BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

}