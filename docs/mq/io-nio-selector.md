---
title: IO通信模型（三）多路复用IO
date: 2018-10-27 08:08:08
url: io/io3-nio
tags:
 - IO
 - NIO
 - 通信模型
 - 多路复用IO
categories:
 - IO 通信
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![mac背包](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/59a331b8fdebaf1dcecab9fe31bb8b80.png)
## 多路复用IO
从`非阻塞同步IO`的介绍中可以发现，为每一个接入创建一个线程在请求很多的情况下不那么适用了，因为这会渐渐耗尽服务器的资源，人们也都意识到了这个 问题，因此终于有人发明了`IO多路复用`。最大的特点就是`不需要开那么多的线程和进程`。
`多路复用IO`是指使用一个线程来检查多个文件描述符（Socket）的就绪状态，比如调用select和poll函数，传入多个文件描述符，如果有一个文件描述符就绪，则返回，否则阻塞直到超时。得到就绪状态后进行真正的操作可以在同一个线程里执行，也可以启动线程执行（比如使用线程池）。
<!-- more -->
![NIO 通信模型](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/083f9772318bc178ec518d6aaadca09e.png)

如图，这样在处理多个连接时，可以只需要一个线程监控就绪状态，对就绪的每个连接开一个线程处理就可以了，这样需要的线程数大大减少，减少了内存开销和上下文切换的CPU开销。


 多路复用IO有几个比较重要的概念，下面一一讲解。

## 缓冲区Buffer
`Buffer`本质是可以写入可以读取的内存，这块内存被包装成了NIO的Buffer对象，然后为它提供一组用于访问的方法。Java则为`java.nio.Buffer`实现了基本数据类型的`Buffer`。
![Buffer](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1f344908223446333ce9db0f67bcc38f.png)
所有的Buffer缓冲区都有4个属性，具体解释可以看表格。

| 属性     | 描述                                                         |
| -------- | ------------------------------------------------------------ |
| Capacity | 容量，可以容纳的最大数据量，不可变                           |
| Limit    | 上届，缓冲区当前数据量，Capacity=>Limit                      |
| Position | 位置，下一个要被读取或者写入的元素的位置，Capacity>=Position |
| Mark     | 标记，调用mark()来设置mark=position，再调用reset()设置position=mark |

这4个属性遵循大小关系： `mark <= position <= limit <= capacity`

### Buffer的基本用法

使用Buffer读写数据一般遵循以下四个步骤：
1. 写入数据到`Buffer`。
2. 调用`flip()`方法。
3. 从Buffer中读取数据。
4. 调用`clear()`方法或者`compact()`方法。
### Buffer的测试代码
下面是对于Java中`ByteBuffer`的测试代码：
```java
        // 申请一个大小为1024bytes的缓冲buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("申请到的Buffer："+byteBuffer);

        // 写入helloworld到buffer
        byteBuffer.put("HelloWorld".getBytes());
        System.out.println("写入HelloWorld到Buffer："+byteBuffer);

        // 切换为读模式
        byteBuffer.flip();
        // 当前Buffer已存放的大小
        int length = byteBuffer.remaining();
        byte[] bytes = new byte[length];

        // 读取bytes长度的数据
        byteBuffer.get(bytes);
        System.out.println("从buffer读取到数据："+new String(bytes,"UTF-8"));

        // 切换为compact 清空已读取的数据
        byteBuffer.compact();
        System.out.println("读取后的Buffer："+byteBuffer);
```
得到如下输出：
```java
申请到的Buffer：java.nio.HeapByteBuffer[pos=0 lim=1024 cap=1024]
写入HelloWorld到Buffer：java.nio.HeapByteBuffer[pos=10 lim=1024 cap=1024]
从buffer读取到数据：HelloWorld
读取后的Buffer：java.nio.HeapByteBuffer[pos=0 lim=1024 cap=1024]
```
需要说明的是`flip()`方法将`Buffer`从写模式切换到读模式，`clear()`方法会清空整个缓冲区。`compact()`方法只会清除已经读过的数据。

### Buffer的读写模式
注意读写模式切换时候几个标记位的变化。
![IO读写模式](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/ff580cf428b9f8e3b5f381df31d083f6.png)

## 通道Channel
通道Channel和流类似，不同的是通道的工作模式可以是全双工。也就是说既可以读取，也可以写入。同时也可以异步的进行读写。`Channel`连接着底层数据与缓冲区`Buffer`。
同样的，Java中针对不同的情况实现了不同的Channel操作类。常用的有
1. FileChannel 从文件中读写数据。
1. DatagramChannel 能通过UDP读写网络中的数据。
1. SocketChannel 能通过TCP读写网络中的数据。
1. ServerSocketChannel可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。

下面是对于Java中Channel和Buffer的简单演示：
```java
    // 申请一个大小为1024bytes的缓冲buffer
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
     // 初始化Channel数据
    FileInputStream fis = new FileInputStream("f:/test.txt");
    FileChannel channel = fis.getChannel();
    System.out.println("Init Channel size：" + channel.size());
     // 从channel中读取数据
    int read = channel.read(byteBuffer);
    System.out.println("Read Size :" + read);
    System.out.println("byteBuffer:"+byteBuffer);
     // 切换到读取模式
    byteBuffer.flip();
     // 输出byteBuffer内容
    System.out.print("print byteBuffer：");
    while (byteBuffer.hasRemaining()){
        System.out.print((char) byteBuffer.get());
    }
     byteBuffer.clear();
    System.out.println(byteBuffer);
    fis.close();

```
输出信息如下：
```java
Init Channel size：10
Read Size :10
byteBuffer:java.nio.HeapByteBuffer[pos=10 lim=1024 cap=1024]
print byteBuffer：helloworld
```
需要注意的是，在读取之前一定要调用`flip()`切换到读取模式。
## 选择器Selector	
Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。我们也可以称`Selector`为轮询代理器，事件订阅器或者channel容器管理器。
应用程序将向Selector对象注册需要它关注的Channel，以及具体的某一个Channel会对哪些IO事件感兴趣。Selector中也会维护一个“已经注册的Channel”的容器。

关于IO事件，我们可以在`SelectionKey`类中找到几个常用事件：

1. OP_READ 可以读取
2. OP_WRITE 可以写入
3. OP_CONNECT 已经连接
4. OP_ACCEPT 可以接受

值得注意的是，在程序中都是通过不断的轮训已经注册的Channel，根据检查注册时的感兴趣事件是否已经就绪来决定是否可以进行后续操作。同时`Selector`也有几个经常使用的方法。

1. select() 阻塞到至少有一个通道在你注册的事件上就绪了。

2. select(long timeout) 最长会阻塞timeout毫秒

3. selectNow() 会阻塞，不管什么通道就绪都立刻返回

4. selectedKeys() 返回就绪的通道

下面是一个对Java中Selector编写服务端的简单使用测试（客户端不在此编写了，如有需要，可以查看[IO通信模型（一）同步阻塞模式BIO（Blocking IO）](https://www.wdbyte.com/2018/10/io/io1-bio/)中的客户端代码）：

```java

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 * NIO-Selector
 * 选择器的使用测试
 * Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读
 * 写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接
 * 。我们也可以称Selector为轮询代理器，事件订阅器或者channel容器管理器。
 * 应用程序将向Selector对象注册需要它关注的Channel，以及具体的某一个Channel会对哪些
 * IO事件感兴趣。Selector中也会维护一个“已经注册的Channel”的容器。
 *
 * @Author niujinpeng
 * @Date 2018/10/26 15:31
 */
public class NioSelector {

    public static void main(String[] args) throws IOException {
        // 获取channel
        ServerSocketChannel channel = ServerSocketChannel.open();
        // channel是否阻塞
        channel.configureBlocking(false);
        // 监听88端口
        ServerSocket socket = channel.socket();
        socket.bind(new InetSocketAddress(83));


        // 创建选择器Selector
        Selector selector = Selector.open();
        // 像选择器中注册channel
        channel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 阻塞到有一个就绪
            int readyChannel = selector.select();
            if (readyChannel == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                // 是否可以接受
                if (selectionKey.isAcceptable()) {
                    System.out.println("准备就绪");
                    SelectableChannel selectableChannel = selectionKey.channel();
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectableChannel;
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 注册感兴趣事件-读取
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2048));
                } else if (selectionKey.isConnectable()) {
                    System.out.println("已连接");

                } else if (selectionKey.isReadable()) {
                    System.out.println("可以读取");

                } else if (selectionKey.isWritable()) {
                    System.out.println("可以写入");

                }
            }
        }
    }
}

```

## Java NIO编程

到这里，已经对`多路复用IO`有了一个基本的认识了，可以结合上面的三个概念就行多路复用IO编程了，下面演示使用Java语言编写一个`多路复用IO`服务端。
NioSocketServer.java

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * <p>
 * 使用Java NIO框架，实现一个支持多路复用IO的服务器端
 *
 * @Author niujinpeng
 * @Date 2018/10/16 0:53
 */
public class NioSocketServer {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NioSocketServer.class);

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 是否阻塞
        serverChannel.configureBlocking(false);
        ServerSocket serverSocket = serverChannel.socket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(83));

        Selector selector = Selector.open();
        // 服务器通道只能注册SelectionKey.OP_ACCEPT事件
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // java程序对多路复用IO的支持也包括了阻塞模式 和非阻塞模式两种。
            if (selector.select(100) == 0) {
                //LOGGER.info("本次询问selector没有获取到任何准备好的事件");
                continue;
            }

            // 询问系统，所有获取到的事件类型
            Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
            while (selectionKeys.hasNext()) {
                SelectionKey readKey = selectionKeys.next();
                // 上面获取到的readKey要移除，不然会一直存在selector.selectedKeys()的集合之中
                selectionKeys.remove();

                SelectableChannel selectableChannel = readKey.channel();
                if (readKey.isValid() && readKey.isAcceptable()) {
                    LOGGER.info("--------------channel通道已经准备完毕-------------");
                    /*
                     * 当server socket channel通道已经准备好，就可以从server socket channel中获取socketchannel了
                     * 拿到socket channel后，要做的事情就是马上到selector注册这个socket channel感兴趣的事情。
                     * 否则无法监听到这个socket channel到达的数据
                     * */
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectableChannel;
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    registerSocketChannel(socketChannel, selector);
                } else if (readKey.isValid() && readKey.isConnectable()) {
                    LOGGER.info("--------------socket channel 建立连接-------------");
                } else if (readKey.isValid() && readKey.isReadable()) {
                    LOGGER.info("--------------socket channel 数据准备完成，可以开始读取-------------");
                    try {
                        readSocketChannel(readKey);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    }
                }
            }
        }

    }

    /**
     * 在server socket channel接收到/准备好 一个新的 TCP连接后。
     * 就会向程序返回一个新的socketChannel。<br>
     * 但是这个新的socket channel并没有在selector“选择器/代理器”中注册，
     * 所以程序还没法通过selector通知这个socket channel的事件。
     * 于是我们拿到新的socket channel后，要做的第一个事情就是到selector“选择器/代理器”中注册这个
     * socket channel感兴趣的事件
     *
     * @param socketChannel
     * @param selector
     * @throws Exception
     */
    private static void registerSocketChannel(SocketChannel socketChannel, Selector selector) {
        // 是否阻塞
        try {
            socketChannel.configureBlocking(false);
            // 读模式只能读，写模式可以同时读
            // socket通道可以且只可以注册三种事件SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT
            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2048));
        } catch (IOException e) {
            LOGGER.info(e.toString(), e);
        }

    }

    private static void readSocketChannel(SelectionKey readKey) throws Exception {
        SocketChannel clientSocketChannel = (SocketChannel) readKey.channel();
        //获取客户端使用的端口
        InetSocketAddress sourceSocketAddress = (InetSocketAddress) clientSocketChannel.getRemoteAddress();
        int sourcePort = sourceSocketAddress.getPort();

        // 拿到这个socket channel使用的缓存区，准备读取数据
        // 解缓存区的用法概念，实际上重要的就是三个元素capacity,position和limit。
        ByteBuffer contextBytes = (ByteBuffer) readKey.attachment();
        // 通道的数据写入到【缓存区】
        // 由于之前设置了ByteBuffer的大小为2048 byte，所以可以存在写入不完的情况，需要调整
        int realLen = -1;
        try {
            realLen = clientSocketChannel.read(contextBytes);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            clientSocketChannel.close();
            return;
        }

        // 如果缓存中没有数据
        if (realLen == -1) {
            LOGGER.warn("--------------缓存中没有数据-------------");
            return;
        }

        // 将缓存区读写状态模式进行切换
        contextBytes.flip();
        // 处理编码问题
        byte[] messageBytes = contextBytes.array();
        String messageEncode = new String(messageBytes, "UTF-8");
        String message = URLDecoder.decode(messageEncode, "UTF-8");

        // 接受到了"over"则清空buffer,并响应,否则不清空缓存，并还原Buffer写状态
        if (message.indexOf("over") != -1) {
            //清空已经读取的缓存，并从新切换为写状态(这里要注意clear()和capacity()两个方法的区别)
            contextBytes.clear();
            LOGGER.info("端口【" + sourcePort + "】客户端发来的信息：" + message);
            LOGGER.info("端口【" + sourcePort + "】客户端消息发送完毕");
            // 响应
            ByteBuffer sendBuffer = ByteBuffer.wrap(URLEncoder.encode("Done!", "UTF-8").getBytes());
            clientSocketChannel.write(sendBuffer);
            clientSocketChannel.close();
        } else {
            LOGGER.info("端口【" + sourcePort + "】客户端发来的信息还未完毕，继续接收");
            // limit和capacity的值一致，position的位置是realLen的位置
            contextBytes.position(realLen);
            contextBytes.limit(contextBytes.capacity());
        }
    }
}
```

## 多路复用IO优缺点

-  不需要使用多线程进行IO处理了
- 同一个端口可以处理多种协议
- 多路复用IO具有操作系统级别的优化
- 其实底层还都是同步IO

文章代码已经上传GitHub：[https://github.com/niumoo/java-toolbox/](https://github.com/niumoo/java-toolbox/tree/master/src/main/java/net/codingme/box/io/jdknio)

<完>


### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变的优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)