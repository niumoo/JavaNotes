//package com.wdbyte.httpclient.get;
//
//import org.apache.hc.client5.http.auth.AuthScope;
//import org.apache.hc.client5.http.auth.CredentialsProvider;
//import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
//import org.apache.hc.client5.http.classic.methods.HttpGet;
//import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
//import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
//import org.apache.hc.core5.http.HttpEntity;
//import org.apache.hc.core5.http.io.entity.EntityUtils;
//
///**
//* @author https://www.wdbyte.com
// * @date 2022/06/25
// */
//public class HttpClient5GetWithBasicAuth {
//
//    public static void main(String[] args) {
//
//    }
//
//    public static String get(String url, String username, String password) {
//        HttpGet request = new HttpGet(url);
//        BasicCredentialsProvider provider = new BasicCredentialsProvider();
//       provider.setCredentials(AuthScope.);
//
//        provider.setCredentials(
//            AuthScope.ANY,
//            new UsernamePasswordCredentials("user", "password")
//        );
//
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
//            .setDefaultCredentialsProvider(provider)
//            .build();
//             CloseableHttpResponse response = httpClient.execute(request)) {
//
//            // 401 if wrong user/password
//            System.out.println(response.getStatusLine().getStatusCode());
//
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                // return it as a String
//                String result = EntityUtils.toString(entity);
//                System.out.println(result);
//            }
//
//        }
//    }
//}
