---
title: Springboot 系列（十四）迅速启用 HTTPS 加密你的网站
toc_number: false
date: 2019-08-07 00:10:22
url: springboot/springboot-14-https
tags:
 - Springboot
 - Https
categories:
 - Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![HTTPS](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572308662878.png)

## 1. 获取 HTTPS 证书

正常情况下 HTTPS 证书需要从证书授权中心获得，这样获得的证书才具有公信力，也会被各种浏览器客户端所认可。常见的证书品牌如 Symantec，GeoTrustm，TrustAsia，Symantec 等。不过在 Springboot 的 HTTPS 实验中就没有必要去申请了，我们可以使用 Java 自带的 **keytool** 生成 HTTPS 证书。

<!-- more -->

查看 keytool 工具使用说明。

```shell
D:\>keytool
密钥和证书管理工具
命令:
 -certreq            生成证书请求
 -changealias        更改条目的别名
 -delete             删除条目
 -exportcert         导出证书
 -genkeypair         生成密钥对
 -genseckey          生成密钥
 -gencert            根据证书请求生成证书
 -importcert         导入证书或证书链
 -importpass         导入口令
 -importkeystore     从其他密钥库导入一个或所有条目
 -keypasswd          更改条目的密钥口令
 -list               列出密钥库中的条目
 -printcert          打印证书内容
 -printcertreq       打印证书请求的内容
 -printcrl           打印 CRL 文件的内容
 -storepasswd        更改密钥库的存储口令

使用 "keytool -command_name -help" 获取 command_name 的用法

D:\>keytool -genkeypair --help
keytool -genkeypair [OPTION]...
生成密钥对
选项:
 -alias <alias>                  要处理的条目的别名
 -keyalg <keyalg>                密钥算法名称
 -keysize <keysize>              密钥位大小
 -sigalg <sigalg>                签名算法名称
 -destalias <destalias>          目标别名
 -dname <dname>                  唯一判别名
 -startdate <startdate>          证书有效期开始日期/时间
 -ext <value>                    X.509 扩展
 -validity <valDays>             有效天数
 -keypass <arg>                  密钥口令
 -keystore <keystore>            密钥库名称
 -storepass <arg>                密钥库口令
 -storetype <storetype>          密钥库类型
 -providername <providername>    提供方名称
 -providerclass <providerclass>  提供方类名
 -providerarg <arg>              提供方参数
 -providerpath <pathlist>        提供方类路径
 -v                              详细输出
 -protected                      通过受保护的机制的口令
```

通过上面的 keytool ，我们生成自己的自签名证书。

```shell
D:\>keytool -genkeypair -alias tomcat_https -keypass 123456 -keyalg RSA -keysize 1024 -validity 365 -keystore d:/tomcat_https.keystore -storepass 123456
您的名字与姓氏是什么?
  [Unknown]:  darcy
您的组织单位名称是什么?
  [Unknown]:  codingme
您的组织名称是什么?
  [Unknown]:  codingme
您所在的城市或区域名称是什么?
  [Unknown]:  ShangHai
您所在的省/市/自治区名称是什么?
  [Unknown]:  ShangHai
该单位的双字母国家/地区代码是什么?
  [Unknown]:  ZN
CN=darcy, OU=codingme, O=codingme, L=ShangHai, ST=ShangHai, C=ZN是否正确?
  [否]:  y
D:\>
```

这时候已经在我们指定的位置下生成了证书文件，如果需要查看证书信息，可以使用 keytool 的 list 命令，可以看到密钥库类型是 JKS，在后面的配置里会用到。

```shell
D:\>keytool -list -keystore tomcat_https.keystore
输入密钥库口令:

密钥库类型: JKS
密钥库提供方: SUN

您的密钥库包含 1 个条目

tomcat_https, 2019-4-21, PrivateKeyEntry,
证书指纹 (SHA1): 1E:5F:15:9C:45:BD:D3:2A:7E:7F:1F:83:56:B8:74:E0:8B:CA:FD:F6

D:\>
```

自己生成的 HTTPS 证书只能用来自己测试，真正用于网络上时，浏览器会显示证书无法信息。因此如果想要得到一个真实有效的证书，请看文章末尾。

## 2. 配置 HTTPS 证书

创建一个 Springboot 项目这里不提，拷贝上一步骤中生成的 tomcat_https.keystore 证书文件到**src/main/resource** 文件夹下，先看下总体的项目结构。

![项目结构如下](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/3f4682d8b6eaa6ac7bf29eae9f9d5109.png)


然后在 application.yml 文件中配置 HTTPS 相关信息。直接配置了端口号为 **443**，443是 HTTPS 的默认端口，这样在使用 HTTPS 就行访问的时候就不需要写额外的端口号了。

```yml
# 配置 HTTPS 相关信息
server:
  port: 443
  http-port: 80 # 为了后面的配置使用，暂时无用
  ssl:
    enabled: true
    key-store: classpath:tomcat_https.keystore # 证书文件
    key-password: 123456  # 密码
    key-store-type: JKS # 密钥库类型
    key-alias: tomcat_https
```

这时，已经可以通过 HTTPS 进行页面访问了。

## 3. 测试 HTTPS 证书

直接编写一个 接口用于测试。

```java
/**
 * <p>
 * Https 接口控制类
 *
 * @Author niujinpeng
 * @Date 2019/4/20 22:59
 */
@RestController
public class HttpsController {

    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello HTTPS";
    }

}
```

启动之后可以通过 [https://localhost/hello](https://localhost/hello) 进行访问了。

![HTTPS 访问测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/29a658a7761aa462cb80e1e34e5b0017.png)


当然，由于是自己生成的证书，会提示不安全，继续访问即可，如果是正常申请或者购买的证书就不会有这个问题的。

## 4. HTTP 跳转 HTTPS

在上面的测试里，HTTPS 已经可以访问了，但是 HTTP 却不能访问，大多数情况下在启用了 HTTPS 之后，都会希望 HTTP 的请求会自动跳转到 HTTPS，这个在 Springboot 里自然也是可以实现的。我们只需要写一个配置类把 HTTP 请求直接转发到 HTTPS 即可。

```java
/**
 * <p>
 * HTTP 强制跳转 HTTPS
 *
 * @Author niujinpeng
 * @Date 2019/4/21 17:47
 */
@Configuration
public class Http2Https {

    @Value("${server.port}")
    private int sslPort;
    @Value("${server.http-port}")
    private int httpPort;

    @Bean
    public TomcatServletWebServerFactory servletContainerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setRedirectPort(sslPort);
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }
}
```

再次启动之后，使用 [http://localhost/hello](http://localhost/hello) 访问会自动跳转到 [https://localhost/hello](https://localhost/hello).



## 附录

如果需要申请免费证书，可以在腾讯云上免费申请，请参考：

- [免费版 DV SSL 证书申请](https://cloud.tencent.com/document/product/400/6813#.E8.8E.B7.E5.8F.96.E8.AF.81.E4.B9.A6)。

如果想要自己安装证书，请参考：

- [Apache 服务器证书安装](https://cloud.tencent.com/document/product/400/35243)
- [Nginx 服务器证书安装](https://cloud.tencent.com/document/product/400/35244)
- [Tomcat 服务器证书安装](https://cloud.tencent.com/document/product/400/35224)
- [Windows IIS 服务器证书安装](https://cloud.tencent.com/document/product/400/35225)

🐟 文章相关代码已经上传 Github [Spring Boot https](https://github.com/niumoo/springboot/tree/master/springboot-web-https)， 欢迎⭐Star️，欢迎 Fork !

**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)