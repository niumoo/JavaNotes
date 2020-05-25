---
title: 原来热加载如此简单，手动写一个 Java 热加载吧
date: 2019-10-28 08:35:00
url: jvm/java-hotput
tags:
 - JVM
 - 热加载
 - 热部署
categories:
 - Java 虚拟机
---

 ![热加载](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/640.webp) 

# 1. 什么是热加载

**热加载**是指可以在不重启服务的情况下让更改的代码生效，**热加载**可以显著的提升开发以及调试的效率，它是基于 Java 的类加载器实现的，但是由于热加载的不安全性，一般不会用于正式的生产环境。
<!-- more -->
# 2. 热加载与热部署的区别

首先，不管是**热加载**还是热部署，都可以在不重启服务的情况下编译/部署项目，都是基于 Java 的类加载器实现的。

那么两者到底有什么区别呢？

在部署方式上：

- 热部署是在服务器运行时**重新部署**项目。
- 热加载是在运行时**重新加载 class**。

在实现原理上：

- 热部署是直接重新**加载整个应用**，耗时相对较高。
- 热加载是在运行时**重新加载 class**，后台会启动一个线程不断检测你的类是否改变。

在使用场景上：

- 热部署更多的是在**生产环境**使用。
- 热加载则更多的是在**开发环境**上使用。线上由于安全性问题不会使用，难以监控。

# 3. 类加载五个阶段

![类的生命周期](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572188635986.png)

可能你已经发现了，图中一共是7个阶段，而不是5个。是因为图是类的完整生命周期，如果要说只是类加载阶段的话，图里最后的使用（Using）和卸载（Unloading）并不算在内。

简单描述一下类加载的五个阶段：

1. 加载阶段：找到类的静态存储结构，加载到虚拟机，定义数据结构。用户可以自定义类加载器。

2. 验证阶段：确保字节码是安全的，确保不会对虚拟机的安全造成危害。

3. 准备阶段：确定内存布局，确定内存遍历，赋**初始值**（注意：是初始值，也有特殊情况）。

4. 解析阶段： 将符号变成直接引用。

5. 初始化阶段：调用程序自定义的代码。规定有且仅有5种情况必须进行初始化。
   1. new（实例化对象）、getstatic（获取类变量的值，被final修饰的除外，他的值在编译器时放到了常量池）、putstatic（给类变量赋值）、invokestatic（调用静态方法） 时会初始化
   2. 调用子类的时候，发现父类还没有初始化，则父类需要立即初始化。
   3. 虚拟机启动，用户要执行的主类，主类需要立即初始化，如 main 方法。
   4. 使用 java.lang.reflect包的方法对类进行反射调用方法 是会初始化。
   5.  当使用JDK 1.7的动态语言支持时， 如果一个java.lang.invoke.MethodHandle实例最后
      的解析结果REF_getStatic、 REF_putStatic、 REF_invokeStatic的方法句柄， 并且这个方法句柄
      所对应的类没有进行过初始化， 则需要先触发其初始化。  

要说明的是，类加载的 5 个阶段中，只有加载阶段是用户可以自定义处理的，而验证阶段、准备阶段、解析阶段、初始化阶段都是用 JVM 来处理的。

# 4. 实现类的热加载

## 4.1 实现思路

我们怎么才能手动写一个类的热加载呢？根据上面的分析，Java 程序在运行的时候，首先会把 class 类文件加载到 JVM 中，而类的加载过程又有五个阶段，五个阶段中只有**加载阶段**用户可以进行自定义处理，所以我们如果能在程序代码更改且重新编译后，让运行的进程可以实时获取到新编译后的 class 文件，然后重新进行加载的话，那么理论上就可以实现一个简单的 **Java 热加载**。

所以我们可以得出实现思路：

1. 实现自己的类加载器。
2. 从自己的类加载器中加载要热加载的类。
3. 不断轮训要热加载的类 class 文件是否有更新。
4. 如果有更新，重新加载。

## 4.2 自定义类加载器

设计 Java 虚拟机的团队把类的加载阶段放到的 JVM 的外部实现（  通过一个类的全限定名来获取描述此类的二进制字节流  ）。这样就可以让程序自己决定如果获取到类信息。而实现这个加载动作的代码模块，我们就称之为 “类加载器”。

在 Java 中，类加载器也就是  `java.lang.ClassLoader`. 所以如果我们想要自己实现一个类加载器，就需要继承 `ClassLoader` 然后重写里面 `findClass`的方法，同时因为类加载器是 `双亲委派模型`实现（也就说。除了一个最顶层的类加载器之外，每个类加载器都要有父加载器，而加载时，会先询问父加载器能否加载，如果父加载器不能加载，则会自己尝试加载）所以我们还需要指定父加载器。

最后根据传入的类路径，加载类的代码看下面。

```java
package net.codingme.box.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * <p>
 * 自定义 Java类加载器来实现Java 类的热加载
 *
 * @Author niujinpeng
 * @Date 2019/10/24 23:22
 */
public class MyClasslLoader extends ClassLoader {

    /** 要加载的 Java 类的 classpath 路径 */
    private String classpath;

    public MyClasslLoader(String classpath) {
        // 指定父加载器
        super(ClassLoader.getSystemClassLoader());
        this.classpath = classpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name, data, 0, data.length);
    }

    /**
     * 加载 class 文件中的内容
     *
     * @param name
     * @return
     */
    private byte[] loadClassData(String name) {
        try {
            // 传进来是带包名的
            name = name.replace(".", "//");
            FileInputStream inputStream = new FileInputStream(new File(classpath + name + ".class"));
            // 定义字节数组输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = inputStream.read()) != -1) {
                baos.write(b);
            }
            inputStream.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

## 4.3 定义要热加载的类

我们假设某个接口（BaseManager.java）下的某个方法（logic）要进行热加载处理。

首先定义接口信息。

```java
package net.codingme.box.classloader;

/**
 * <p>
 * 实现这个接口的子类，需要动态更新。也就是热加载
 *
 * @Author niujinpeng
 * @Date 2019/10/24 23:29
 */
public interface BaseManager {

    public void logic();
}
```

写一个这个接口的实现类。

```java
package net.codingme.box.classloader;

import java.time.LocalTime;

/**
 * <p>
 * BaseManager 这个接口的子类要实现类的热加载功能。
 *
 * @Author niujinpeng
 * @Date 2019/10/24 23:30
 */
public class MyManager implements BaseManager {

    @Override
    public void logic() {
        System.out.println(LocalTime.now() + ": Java类的热加载");
    }
}
```

后面我们要做的就是让这个类可以通过我们的 MyClassLoader 进行自定义加载。类的**热加载**应当只有在类的信息被更改然后重新编译之后进行重新加载。所以为了不意义的重复加载，我们需要判断 class 是否进行了更新，所以我们需要记录 class 类的修改时间，以及对应的类信息。

所以编译一个类用来记录某个类对应的某个类加载器以及上次加载的 class 的修改时间。

```java
package net.codingme.box.classloader;

/**
 * <p>
 * 封装加载类的信息
 *
 * @Author niujinpeng
 * @Date 2019/10/24 23:32
 */
public class LoadInfo {

    /** 自定义的类加载器 */
    private MyClasslLoader myClasslLoader;

    /** 记录要加载的类的时间戳-->加载的时间 */
    private long loadTime;

    /** 需要被热加载的类 */
    private BaseManager manager;

    public LoadInfo(MyClasslLoader myClasslLoader, long loadTime) {
        this.myClasslLoader = myClasslLoader;
        this.loadTime = loadTime;
    }

    public MyClasslLoader getMyClasslLoader() {
        return myClasslLoader;
    }

    public void setMyClasslLoader(MyClasslLoader myClasslLoader) {
        this.myClasslLoader = myClasslLoader;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }

    public BaseManager getManager() {
        return manager;
    }

    public void setManager(BaseManager manager) {
        this.manager = manager;
    }
}
```

## 4.4 热加载获取类信息

在实现思路里，我们知道轮训检查 class 文件是不是被更新过，所以每次调用要热加载的类时，我们都要进行检查类是否被更新然后决定要不要重新加载。为了方便这步的获取操作，可以使用一个简单的工厂模式进行封装。

要注意是加载 class 文件需要指定完整的路径，所以类中定义了 CLASS_PATH 常量。

```java
package net.codingme.box.classloader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 加载 manager 的工厂
 *
 * @Author niujinpeng
 * @Date 2019/10/24 23:38
 */
public class ManagerFactory {

    /** 记录热加载类的加载信息 */
    private static final Map<String, LoadInfo> loadTimeMap = new HashMap<>();

    /** 要加载的类的 classpath */
    public static final String CLASS_PATH = "D:\\IdeaProjectMy\\lab-notes\\target\\classes\\";

    /** 实现热加载的类的全名称(包名+类名 ) */
    public static final String MY_MANAGER = "net.codingme.box.classloader.MyManager";

    public static BaseManager getManager(String className) {
        File loadFile = new File(CLASS_PATH + className.replaceAll("\\.", "/") + ".class");
        // 获取最后一次修改时间
        long lastModified = loadFile.lastModified();
        System.out.println("当前的类时间：" + lastModified);
        // loadTimeMap 不包含 ClassName 为 key 的信息，证明这个类没有被加载，要加载到 JVM
        if (loadTimeMap.get(className) == null) {
            load(className, lastModified);
        } // 加载类的时间戳变化了，我们同样要重新加载这个类到 JVM。
        else if (loadTimeMap.get(className).getLoadTime() != lastModified) {
            load(className, lastModified);
        }
        return loadTimeMap.get(className).getManager();
    }

    /**
     * 加载 class ，缓存到 loadTimeMap
     * 
     * @param className
     * @param lastModified
     */
    private static void load(String className, long lastModified) {
        MyClasslLoader myClasslLoader = new MyClasslLoader(className);
        Class loadClass = null;
        // 加载
        try {
            loadClass = myClasslLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        BaseManager manager = newInstance(loadClass);
        LoadInfo loadInfo = new LoadInfo(myClasslLoader, lastModified);
        loadInfo.setManager(manager);
        loadTimeMap.put(className, loadInfo);
    }

    /**
     * 以反射的方式创建 BaseManager 的子类对象
     * 
     * @param loadClass
     * @return
     */
    private static BaseManager newInstance(Class loadClass) {
        try {
            return (BaseManager)loadClass.getConstructor(new Class[] {}).newInstance(new Object[] {});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

## 4.5  热加载测试

直接写一个线程不断的检测要热加载的类是不是已经更改需要重新加载，然后运行测试即可。

```java
package net.codingme.box.classloader;

/**
 * <p>
 *
 * 后台启动一条线程，不断检测是否要刷新重新加载，实现了热加载的类
 * 
 * @Author niujinpeng
 * @Date 2019/10/24 23:53
 */
public class MsgHandle implements Runnable {
    @Override
    public void run() {
        while (true) {
            BaseManager manager = ManagerFactory.getManager(ManagerFactory.MY_MANAGER);
            manager.logic();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

主线程：

```java
package net.codingme.box.classloader;

public class ClassLoadTest {
    public static void main(String[] args) {
        new Thread(new MsgHandle()).start();
    }
}
```

代码已经全部准备好了，最后一步，可以启动测试了。如果你是用的是 Eclipse ，直接启动就行了；如果是 IDEA ，那么你需要 DEBUG 模式启动（IDEA 对热加载有一定的限制）。

启动后看到控制台不断的输出：

```shell
00:08:13.018: Java类的热加载
00:08:15.018: Java类的热加载
```

这时候我们随便更改下 MyManager 类的 logic 方法的输出内容然后保存。

```java
@Override
public void logic() {
     System.out.println(LocalTime.now() + ": Java类的热加载 Oh~~~~");
}
```

可以看到控制台的输出已经自动更改了（IDEA 在更改后需要按 CTRL + F9）。

![类的热加载](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1572192565262.png)

代码已经放到Github: https://github.com/niumoo/lab-notes/