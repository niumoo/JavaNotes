---
title: IO通信模型（一）同步阻塞模式BIO（Blocking IO）
date: 2018-10-23 17:44:42
url: io/io1-bio
tags:
 - BIO
 - IO
 - 通信模型
 - Blocking IO
 - 同步阻塞IO
categories:
 - IO 通信
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/fe0a011dcd86762f98eecdedfad9eb6d.jpg)

### 几个概念

**阻塞IO** 和**非阻塞IO** 这两个概念是程序级别的。主要描述的是程序请求操作系统IO操作后，如果IO资源没有准备好，那么程序该如何处理的问题：前者等待；后者继续执行（但是使用线程一直轮询，直到有IO资源准备好了）。

**同步IO** 和 **异步IO**，这两个概念是操作系统级别的。主要描述的是操作系统在收到程序请求IO操作后，如果IO资源没有准备好，该如何响应程序的问题：前者不响应，直到IO资源准备好以后；后者返回一个标记（好让程序和自己知道以后的数据往哪里通知），当IO资源准备好以后，再用事件机制返回给程序。
<!-- more -->

### 同步阻塞模式（Blocking IO）

同步阻塞IO模型是最简单的IO模型，用户线程在内核进行IO操作时如果数据没有准备号会被阻塞。

![BIO](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/63d5d1de854fe7ba06ab60f7a765ab01.png)
<a align="center" href="http://www.masterraghu.com/subjects/np/introduction/unix_network_programming_v1.3/ch06lev1sec2.html" >图片来源:www.masterraghu.com</a>

伪代码表示如下：

```java
{
    // 阻塞，直到有数据
	read(socket, buffer);
	process(buffer);
}
```

BIO通信方式的`特点`：

1. 一个线程负责连接，多线程则为每一个接入开启一个线程。
2. 一个请求一个应答。
3. 请求之后应答之前客户端会一直等待（阻塞）。

BIO通信方式在单线程服务器下一次只能处理一个请求，在处理完毕之前一直阻塞。因此不适用于高并发的情况。不过可以使用多线程`稍微`改进。

![BIO通信模型-来源于慕课网](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/3c3aa5599f42817372ce73658387d32a.png)

### Java同步阻塞模式

Java中的阻塞模式BIO，就是在`java.net`包中的Socket套接字的实现，Socket套接字是TCP/UDP等传输层协议的实现。

### Java同步阻塞模式编码
#### 多线程客户端
为了测试服务端程序，可以先编写一个多线程客户端用于请求测试。
```java

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * BIO测试
 * 模拟20个客户端并发请求，服务端则使用单线程。
 *
 * @Author niujinpeng
 * @Date 2018/10/15 10:50
 */
public class SocketClient {
    public static void main(String[] args) throws InterruptedException {
        Integer clientNumber = 20;
        CountDownLatch countDownLatch = new CountDownLatch(clientNumber);

        // 分别启动20个客户端
        for (int index = 0; index < clientNumber; index++, countDownLatch.countDown()) {
            SocketClientRequestThread client = new SocketClientRequestThread(countDownLatch, index);
            new Thread(client).start();
        }

        synchronized (SocketClient.class) {
            SocketClient.class.wait();
        }
    }
}

/**
 * <p>
 * 客户端，用于模拟请求
 *
 * @Author niujinpeng
 * @Date 2018/10/15 10:53
 */
class SocketClientRequestThread implements Runnable {

    private CountDownLatch countDownLatch;

    /**
     * 线程的编号
     */
    private Integer clientIndex;


    public SocketClientRequestThread(CountDownLatch countDownLatch, Integer clientIndex) {
        this.countDownLatch = countDownLatch;
        this.clientIndex = clientIndex;
    }

    @Override
    public void run() {
        Socket socket = null;
        OutputStream clientRequest = null;
        InputStream clientResponse = null;
        try {
            socket = new Socket("localhost", 83);
            clientRequest = socket.getOutputStream();
            clientResponse = socket.getInputStream();

            //等待，直到SocketClientDaemon完成所有线程的启动，然后所有线程一起发送请求
            this.countDownLatch.await();

            // 发送请求信息
            clientRequest.write(("这是第" + this.clientIndex + "个客户端的请求").getBytes());
            clientRequest.flush();

            // 等待服务器返回消息
            System.out.println("第" + this.clientIndex + "个客户端请求发送完成，等待服务器响应");
            int maxLen = 1024;
            byte[] contentBytes = new byte[maxLen];
            int realLen;
            String message = "";

            // 等待服务端返回，in和out不能cloese
            while ((realLen = clientResponse.read(contentBytes, 0, maxLen)) != -1) {
                message += new String(contentBytes, 0, realLen);
            }
            System.out.println("第" + this.clientIndex + "个客户端接受到来自服务器的消息:" + message);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientRequest != null) {
                    clientRequest.close();
                }
                if (clientRequest != null) {
                    clientResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```
#### 单线程服务端

因为Java中的Socket就是BIO的模式，因此我们可以很简单的编写一个BIO单线程服务端。

SocketServer.java

```java

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO服务端
 * <p>
 * 单线程阻塞的服务器端
 *
 * @Author niujinpeng
 * @Date 2018/10/15 11:17
 */
public class SocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(83);
        try {
            while (true) {
                // 阻塞，直到有数据准备完毕
                Socket socket = serverSocket.accept();

                // 开始收取信息
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                Integer sourcePort = socket.getPort();
                int maxLen = 1024 * 2;
                byte[] contextBytes = new byte[maxLen];

                // 阻塞，直到有数据准备完毕
                int realLen = input.read(contextBytes, 0, maxLen);
                // 读取信息
                String message = new String(contextBytes, 0, realLen);

                // 输出接收信息
                System.out.println("服务器收到来自端口【" + sourcePort + "】的信息：" + message);
                // 响应信息
                output.write("Done!".getBytes());

                // 关闭
                output.close();
                input.close();
                socket.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}

```

#### 多线程服务端
单线程服务器，在处理请求时只能同时处理一条，也就是说如果在请求到来时发现有请求尚未处理完毕，只能等待处理，因此使用`多线程改进`服务端。    

SocketServerThread.java

```java

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO服务端
 * <p>
 * 多线程的阻塞的服务端
 * <p>
 * 当然，接收到客户端的socket后，业务的处理过程可以交给一个线程来做。
 * 但还是改变不了socket被一个一个的做accept()的情况。
 *
 * @Author niujinpeng
 * @Date 2018/10/15 11:17
 */
public class SocketServerThread implements Runnable {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketServerThread.class);

    private Socket socket;

    public SocketServerThread(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(83);
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                //当然业务处理过程可以交给一个线程（这里可以使用线程池）,并且线程的创建是很耗资源的。
                //最终改变不了.accept()只能一个一个接受socket的情况,并且被阻塞的情况
                SocketServerThread socketServerThread = new SocketServerThread(socket);
                new Thread(socketServerThread).start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }


    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            //下面我们收取信息
            in = socket.getInputStream();
            out = socket.getOutputStream();
            Integer sourcePort = socket.getPort();
            int maxLen = 1024;
            byte[] contextBytes = new byte[maxLen];
            //使用线程，同样无法解决read方法的阻塞问题，
            //也就是说read方法处同样会被阻塞，直到操作系统有数据准备好
            int realLen = in.read(contextBytes, 0, maxLen);
            //读取信息
            String message = new String(contextBytes, 0, realLen);

            //下面打印信息
            logger.info("服务器收到来自于端口：" + sourcePort + "的信息：" + message);

            //下面开始发送信息
            out.write("回发响应信息！".getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            //试图关闭
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
```
看起来多线程增加了服务能力，但是很明显多线程改进之后仍有以下`局限性`：

- 接收和通知处理结果的过程依旧是单线程的。
- 系统可以创建的线程数量有限。`cat /proc/sys/kernel/threads-max`可以查看可以创建的线程数量。
- 如果线程较多，CPU需要更多的时间切换，处理真正业务的时间就会变少。
- 创建线程会消耗较多资源，JVM创建一个线程都会默认分配128KB空间。
- 多线程也无法解决因为`调用底层系统`的`同步IO`而决定的同步IO机制。


### 同步阻塞模式总结
BIO模式因为进程的阻塞挂起，不会消耗过多的CPU资源，而且开发难度低，比较适合并发量小的网络应用开发。同时很容易发现因为请求IO会阻塞进程，所以不时候并发量大的应用。如果为每一个请求分配一个线程，系统开销就会过大。

同时在Java中，使用了多线程来处理阻塞模式，也无法解决程序在`accept()`和`read()`时候的阻塞问题。因为`accept()`和`read()`的IO模式支持是基于操作系统的，如果操作系统发现没有套接字从指定的端口传送过来，那么`操作系统就会等待`。这样`accept()`和`read()`方法就会一直等待。

----

GitHub   源码：[https://github.com/niumoo/java-toolbox](https://github.com/niumoo/java-toolbox/tree/master/src/main/java/net/codingme/box/io/bio)

此文参考文章：[5种IO模型、阻塞IO和非阻塞IO、同步IO和异步IO](https://blog.csdn.net/tjiyu/article/details/52959418)
此文参考文章：[架构设计：系统间通信（3）——IO通信模型和JAVA实践 上篇](https://blog.csdn.net/yinwenjie/article/details/48472237)


**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)