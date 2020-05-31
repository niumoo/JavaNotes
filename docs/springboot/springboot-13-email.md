---
title: Springboot 系列（十三）使用邮件服务
toc_number: true
date: 2019-03-12 00:20:22
url: springboot/springboot-13-email
tags:
 - Springboot
 - 邮件
 - E-mail
categories:
 - Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![桌面生活（来自网络）](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/00a2b4768bf601da67118c0acb347876.jpg)我们这个时代，邮件服务不管是对于工作上的交流，还是平时的各种邮件通知，都是一个十分重要的存在。Java 从很早时候就可以通过 Java mail 支持邮件服务。Spring 更是对 Java mail 进行了进一步的封装，抽象出了 `JavaMailSender`. 后来随着 Springboot 的出现，理所当然的出现了 `spring-boot-starter-mail`. 不管怎么说，每次的封装都让使用变得越来越简单。
<!-- more -->
## Springboot mail 依赖

创建 Springboot 项目不提，先看一下总体目录结构。
![项目结构](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/87989c22d455e3d428811562649c840d.jpg)

直接引入 Springboot 邮件服务所需的依赖。
```xml
   <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- 邮件服务 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <!-- Thymeleaf 模版，用于发送模版邮件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Springboot mail 配置
使用邮件服务需要配置自己可以使用的邮箱信息，一般需要配置发送协议 SMTP、邮箱帐号（本次以126邮箱为例）、邮箱密码以及编码格式。
```properties
spring.mail.host=smtp.126.com
spring.mail.port=25
# 你的邮箱地址
spring.mail.username=niumoo@126.com 
# 你的授权码（126 和 163 以及 qq 邮箱 都需要授权码登录，没有授权码的直接登录网页版邮箱设置里设置）
spring.mail.password=password
spring.mail.default-encoding=UTF-8
```
## Springboot mail 文本邮件
文本邮件是最简单也是最基础的一种邮件，使用 Spring 封装的 `JavaMailSender` 直接发送就可以了。

创建 `MailService` 类，注入 `JavaMailSender` 用于发送邮件，使用 `@Value("${spring.mail.username}")` 绑定配置文件中的参数用于设置邮件发送的来邮箱。使用 `@Service` 注解把 `MailService` 注入到 Spring 容器，使用 `Lombok` 的 `@Slf4j` 引入日志。
```java
/**
 * <p>
 * 邮件服务
 *
 * @Author niujinpeng
 * @Date 2019/3/10 21:45
 */
@Service
@Slf4j
public class MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送简单文本邮件
     * 
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleTextMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(from);
        mailSender.send(message);
        log.info("【文本邮件】成功发送！to={}", to);
    }
}
```
创建 Springboot 的单元测试类测试文本邮件，实验中的收信人为了方便，都设置成了自己的邮箱。
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendSimpleTextMailTest() {
        String to = "niumoo@126.com";
        String subject = "Springboot 发送简单文本邮件";
        String content = "<p>第一封 Springboot 简单文本邮件</p>";
        mailService.sendSimpleTextMail(to, subject, content);
    }
}
```
运行单元测试，测试文本邮件的发送。

PS：如果运行报出异常 `AuthenticationFailedException: 535 Error`. 一般都是用户名和密码有误。
```log
Caused by: javax.mail.AuthenticationFailedException: 535 Error: authentication failed

	at com.sun.mail.smtp.SMTPTransport$Authenticator.authenticate(SMTPTransport.java:965)
	at com.sun.mail.smtp.SMTPTransport.authenticate(SMTPTransport.java:876)
	at com.sun.mail.smtp.SMTPTransport.protocolConnect(SMTPTransport.java:780)
	at javax.mail.Service.connect(Service.java:366)
	at org.springframework.mail.javamail.JavaMailSenderImpl.connectTransport(JavaMailSenderImpl.java:517)
	at org.springframework.mail.javamail.JavaMailSenderImpl.doSend(JavaMailSenderImpl.java:436)
	... 34 more
```
正常运行输出成功发送的日志。
```log
2019-03-11 23:35:14.743  INFO 13608 --- [           main] n.codingme.boot.service.MailServiceTest  : Started MailServiceTest in 3.964 seconds (JVM running for 5.749)
2019-03-11 23:35:24.718  INFO 13608 --- [           main] net.codingme.boot.service.MailService    : 【文本邮件】成功发送！to=niumoo@126.com
```
查看邮箱中的收信。

![文本邮件](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/e391dbce4b614779ae9a024908ef0275.jpg)

文本邮件正常收到，同时可见文本邮件中的 HTML 标签也不会被解析。

## Springboot mail HTML 邮件
在上面的 `MailService` 类里新加一个方法 `sendHtmlMail`，用于测试 HTML 邮件。
```java
    /**
     * 发送 HTML 邮件
     * 
     * @param to
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        // true 为 HTML 邮件
        messageHelper.setText(content, true);
        mailSender.send(message);
        log.info("【HTML 邮件】成功发送！to={}", to);
    }
```
在测试方法中增加 HTML 邮件测试方法。
```java
    @Test
    public void sendHtmlMailTest() throws MessagingException {
        String to = "niumoo@126.com";
        String subject = "Springboot 发送 HTML 邮件";
        String content = "<h2>Hi~</h2><p>第一封 Springboot HTML 邮件</p>";
        mailService.sendHtmlMail(to, subject, content);
    }
```
运行单元测试，查看收信情况。

![HTML 邮件](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/147b7f68d12e0080b408a581414fb92a.jpg)

HTML 邮件正常收到，HTML 标签也被解析成对应的样式。

## Springboot mail 附件邮件
在上面的 `MailService` 类里新加一个方法 `sendAttachmentMail`，用于测试 附件邮件。
```java
    /**
     * 发送带附件的邮件
     * 
     * @param to
     * @param subject
     * @param content
     * @param fileArr
     */
    public void sendAttachmentMail(String to, String subject, String content, String... fileArr)
        throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);

        // 添加附件
        for (String filePath : fileArr) {
            FileSystemResource fileResource = new FileSystemResource(new File(filePath));
            if (fileResource.exists()) {
                String filename = fileResource.getFilename();
                messageHelper.addAttachment(filename, fileResource);
            }
        }
        mailSender.send(mimeMessage);
        log.info("【附件邮件】成功发送！to={}", to);
    }
```
在测试方法中增加附件邮件测试方法。
```java
    @Test
    public void sendAttachmentTest() throws MessagingException {
        String to = "niumoo@126.com";
        String subject = "Springboot 发送 HTML 附件邮件";
        String content = "<h2>Hi~</h2><p>第一封 Springboot HTML 附件邮件</p>";
        String filePath = "pom.xml";
        mailService.sendAttachmentMail(to, subject, content, filePath, filePath);
    }
```
运行单元测试，查看收信情况。

![附件邮件](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/0ab42e65ae35c7970d7954bf5a985a79.jpg)

带附件的邮件正常收到，多个附件的实现方式同理。

## Springboot mail 图片邮件
图片邮件和其他的邮件方式略有不同，图片邮件需要先在内容中定义好图片的位置并出给一个记录 ID ，然后在把图片加到邮件中的对于的 ID 位置。

在上面的 `MailService` 类里新加一个方法 `sendImgMail`，用于测试 附件邮件。
```java
   /**
     * 发送带图片的邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param imgMap
     */
    public void sendImgMail(String to, String subject, String content, Map<String, String> imgMap)
        throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        // 添加图片
        for (Map.Entry<String, String> entry : imgMap.entrySet()) {
            FileSystemResource fileResource = new FileSystemResource(new File(entry.getValue()));
            if (fileResource.exists()) {
                String filename = fileResource.getFilename();
                messageHelper.addInline(entry.getKey(), fileResource);
            }
        }
        mailSender.send(mimeMessage);
        log.info("【图片邮件】成功发送！to={}", to);
    }
```
在测试方法中增加图片邮件测试方法，测试方法中使用的 apple.png 是项目里的一个图片。可以看上面的项目结构。
```java
    @Test
    public void sendImgTest() throws MessagingException {
        String to = "niumoo@126.com";
        String subject = "Springboot 发送 HTML 图片邮件";
        String content =
            "<h2>Hi~</h2><p>第一封 Springboot HTML 图片邮件</p><br/><img src=\"cid:img01\" /><img src=\"cid:img02\" />";
        String imgPath = "apple.png";
        Map<String, String> imgMap = new HashMap<>();
        imgMap.put("img01", imgPath);
        imgMap.put("img02", imgPath);
        mailService.sendImgMail(to, subject, content, imgMap);
    }
```
运行单元测试，查看收信情况。

![图片邮件](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/a676fadf6b40195c7de746adff0700bc.jpg)

两个图片正常显示在邮件里。

## Springboot mail 模版邮件
模版邮件的用处很广泛，像经常收到的注册成功邮件或者是操作通知邮件等都是模版邮件，模版邮件往往只需要更改其中的几个变量。Springboot 中的模版邮件首选需要选择一款模版引擎，在引入依赖的时候已经增加了模版引擎 `Thymeleaf`.

模版邮件首先需要一个邮件模版，我们在 `Templates` 下新建一个 `HTML` 文件 `RegisterSuccess.html`. 其中的 username 是给我们自定义的。
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>注册成功通知</title>
</head>
<body>
<p>[[${username}]]，您好!
</p>
<p>
    新的公钥已添加到你的账户:<br/>
    标题: HP-WIN10 <br/>
    如果公钥无法使用，你可以在这里重新添加： SSH Keys
</p>
</body>
</html>
```
在邮件服务 `MailService` 中注入模版引擎,然后编写邮件模版发送代码。
```java
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送模版邮件
     * 
     * @param to
     * @param subject
     * @param paramMap
     * @param template
     * @throws MessagingException
     */
    public void sendTemplateMail(String to, String subject, Map<String, Object> paramMap, String template)
        throws MessagingException {
        Context context = new Context();
        // 设置变量的值
        context.setVariables(paramMap);
        String emailContent = templateEngine.process(template, context);
        sendHtmlMail(to, subject, emailContent);
        log.info("【模版邮件】成功发送！paramsMap={}，template={}", paramMap, template);
    }
```
在单元单元测试中增加模版邮件测试方法，然后发送邮件测试。
```java
    @Test
    public void sendTemplateMailTest() throws MessagingException {
        String to = "niumoo@126.com";
        String subject = "Springboot 发送 模版邮件";
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("username", "Darcy");
        mailService.sendTemplateMail(to, subject, paramMap, "RegisterSuccess");
    }
```
查看收信情况。

![模版邮件](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/8244d50efe797cfa3ceb14aab7b7b461.jpg)

可以发现模版邮件已经正常发送了。

## Springboot mail 补充
上面的例子中，是 Springboot 邮件服务的基本用法，代码也有很多重复，和实际的使用情况相比还有很多不足，比如缺少`异常处理机制`，在发送失败时的`重试机制`也没有，实际情况中邮件服务往往对实时性不高，多说情况下会用于`异步请求`。

文章相关代码已经上传 Github [Spring Boot 相关整合 - 邮件服务](https://github.com/niumoo/springboot/tree/master/springboot-mail)。

<完>
本文原发于个人博客：[https://www.wdbyte.com](https://www.wdbyte.com) 转载请注明出处。

**最后的话**

>文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，欢迎Star和指教。更有一线大厂面试点，Java程序员需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点个「**赞**」或「**分享**」，都是支持，我都喜欢！  
文章每周持续更新，要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号或者[我的博客](https://www.wdbyte.com/)。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)