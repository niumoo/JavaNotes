---
title: IO通信模型（二）同步非阻塞模式NIO（NonBlocking IO）
date: 2018-10-25 08:08:08
url: io/io2-nio
tags:
 - NIO
 - 通信模型
 - 非阻塞IO
 - NonBlocking IO
categories:
 - IO 通信
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/7da0bf510061ea2cd6b104262bd340f7.jpg)

### 同步非阻塞模式（NonBlocking IO）

在非阻塞模式中，发出Socket的`accept()`和`read()`操作时，如果内核中的数据还没有准备好，那么它并不会阻塞用户进程，而是立刻返回一个信息。也就是说进程发起一个read操作后，并不需要一直阻塞等待，而是马上就得到了一个结果。
<!-- more -->
如果结果发现数据准备完毕就可以读取数据，然后拷贝到用户内存。如果结果发现数据没有就绪也会返回，进程继续不断的`主动询问`数据的准备情况是非阻塞模式的一个特点。
![多路复用IO](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/c6cd2412ccdfc0aec38a87379c8800c6.png)
伪代码表示如下：

```java
{
	while(read(socket, buffer) != SUCCESS){    
	}
	process(buffer);
}
```
### Java同步非阻塞模式

如上所述，Java的Socket是阻塞模式的典型应用。在发起`accpet()`和`read()`请求之后会持续阻塞，但是Java中提供了`setSoTimeout()`方法设置超时时间，在固定时间内没有得到结果，就会结束本次阻塞，等待进行下一次的阻塞轮训。这是，也就实现了应用层面的非阻塞。

Java中Socket中的`setSoTimeout()`方法：
```java
public synchronized void setSoTimeout(int timeout) throws SocketException {
    if (isClosed())
        throw new SocketException("Socket is closed");
    if (timeout < 0)
        throw new IllegalArgumentException("timeout can't be negative");
    getImpl().setOption(SocketOptions.SO_TIMEOUT, new Integer(timeout));
}
```
### Java同步非阻塞模式编码
通过设置`setSoTimeout()`使阻塞模式的服务端`accpet()`和`read()`优化为非阻塞模式。
SocketServerNioListenAndRead.java

```java

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * <p>
 * 非阻塞IO - 监听非阻塞 - 读取非阻塞
 *
 * @Author niujinpeng
 * @Date 2018/10/15 14:53
 */
public class SocketServerNioListenAndRead {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketServerNioListenAndRead.class);
    private static Object xWait = new Object();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(83);
            serverSocket.setSoTimeout(100);
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    synchronized (SocketServerNioListenAndRead.xWait) {
                        logger.info("没有从底层接收到任务数据报文，等待10ms，，模拟事件X的处理时间");
                        SocketServerNioListenAndRead.xWait.wait(10);
                    }
                    continue;
                }

                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                Integer sourcePort = socket.getPort();
                int maxLen = 2048;
                byte[] contentBytes = new byte[maxLen];
                int realLen;
                StringBuffer message = new StringBuffer();

                // 接收消息非阻塞实现
                socket.setSoTimeout(10);

                BIORead:
                while (true) {
                    try {
                        // 读取的时候，程序会阻塞，知道系统把网络传过来的数据准备完毕
                        while ((realLen = input.read(contentBytes, 0, maxLen)) != -1) {
                            message.append(new String(contentBytes, 0, realLen));
                            /**
                             * 如果收到over，表示传送完毕
                             */
                            if (message.toString().endsWith("over")) {
                                break BIORead;
                            }
                        }
                    } catch (SocketTimeoutException e) {
                        //===========================================================
                        //      执行到这里，说明本次read没有接收到任何数据流
                        //      主线程在这里又可以做一些事情，记为Y
                        //===========================================================
                        logger.info("这次没有从底层接收到任务数据报文，等待10毫秒，模拟事件Y的处理时间");
                        continue;
                    }

                }

                // 输出信息
                logger.info("服务器收到来自端口" + sourcePort + "的消息：" + message.toString());
                // 响应
                output.write("Done!".getBytes());

                output.close();
                input.close();
                socket.close();
            }
        } catch (SocketException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

}


```
上面的代码可以实现监听和读取数据的非阻塞，但是还是只能一个一个的处理，可以使用多线程`稍微改进`。
SocketServerNioListenThread.java
```java

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * <p>
 * 非阻塞IO - 监听非阻塞 - 读取非阻塞
 * 通过加入线程的概念，让socket server能够在应用层面
 * 通过非阻塞的方式同时处理多个socket套接字
 * <p>
 * 此时可以实现非阻塞的IO，但是因为调用了系统底层的阻塞同步IO，
 * 因此仍然没有从根本上解决问题
 *
 * @Author niujinpeng
 * @Date 2018/10/15 15:23
 */
public class SocketServerNioListenThread {

    private static Object xWait = new Object();

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServerNioListenThread.class);

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(83);
        serverSocket.setSoTimeout(100);
        try {
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch (SocketTimeoutException e1) {
                    //===========================================================
                    //      执行到这里，说明本次accept没有接收到任何TCP连接
                    //      主线程在这里就可以做一些事情，记为X
                    //===========================================================
                    synchronized (SocketServerNioListenThread.xWait) {
                        LOGGER.info("这次没有从底层接收到任何TCP连接，等待10毫秒，模拟事件X的处理时间");
                        SocketServerNioListenThread.xWait.wait(10);
                    }
                    continue;
                }
                //当然业务处理过程可以交给一个线程（这里可以使用线程池）,并且线程的创建是很耗资源的。
                //最终改变不了.accept()只能一个一个接受socket连接的情况
                SocketServerThread socketServerThread = new SocketServerThread(socket);
                new Thread(socketServerThread).start();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}

/**
 * 当然，接收到客户端的socket后，业务的处理过程可以交给一个线程来做。
 * 但还是改变不了socket被一个一个的做accept()的情况。
 *
 * @author niujinpeng
 */
class SocketServerThread implements Runnable {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServerThread.class);

    private Socket socket;

    public SocketServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            Integer sourcePort = socket.getPort();
            int maxLen = 2048;
            byte[] contextBytes = new byte[maxLen];
            int realLen;
            StringBuffer message = new StringBuffer();
            //下面我们收取信息（设置成非阻塞方式，这样read信息的时候，又可以做一些其他事情）
            this.socket.setSoTimeout(10);
            BIORead:
            while (true) {
                try {
                    while ((realLen = in.read(contextBytes, 0, maxLen)) != -1) {
                        message.append(new String(contextBytes, 0, realLen));
                        /*
                         * 我们假设读取到“over”关键字，
                         * 表示客户端的所有信息在经过若干次传送后，完成
                         * */
                        if (message.indexOf("over") != -1) {
                            break BIORead;
                        }
                    }
                } catch (SocketTimeoutException e2) {
                    //===========================================================
                    //      执行到这里，说明本次read没有接收到任何数据流
                    //      主线程在这里又可以做一些事情，记为Y
                    //===========================================================
                    LOGGER.info("这次没有从底层接收到任务数据报文，等待10毫秒，模拟事件Y的处理时间");
                    continue;
                }
            }
            //下面打印信息
            Long threadId = Thread.currentThread().getId();
            LOGGER.info("服务器(线程：" + threadId + ")收到来自于端口：" + sourcePort + "的信息：" + message);

            //下面开始发送信息
            out.write("回发响应信息！".getBytes());

            //关闭
            out.close();
            in.close();
            this.socket.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}

```

### 同步非阻塞模式总结

用户需要不断地调用，尝试读取数据，直到读取成功后，才继续处理接收的数据。整个IO请求的过程中，虽然用户线程每次发起IO请求后可以立即返回，但是为了等到数据，仍需要不断地轮询、重复请求，消耗了大量的CPU的资源。一般很少直接使用这种模型，而是在其他IO模型中使用非阻塞IO这一特性。

开发难度相对于阻塞IO模式较难，适合并发小且不需要及时响应的网络应用开发。

GitHub   源码：[https://github.com/niumoo/java-toolbox/](https://github.com/niumoo/java-toolbox/tree/master/src/main/java/net/codingme/box/io/nio)
此文参考文章：[IO复用,AIO,BIO,NIO,同步，异步，阻塞和非阻塞](https://www.cnblogs.com/aspirant/p/6877350.html?utm_source=itdadao&utm_medium=referral)
此文参考文章：[6.2 I/O Models](http://www.masterraghu.com/subjects/np/introduction/unix_network_programming_v1.3/ch06lev1sec2.html)


**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)