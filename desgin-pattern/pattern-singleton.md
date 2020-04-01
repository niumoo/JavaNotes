---
title: 设计模式 -创建型模式之单例模式的五种实现
date: 2018-03-08 16:03:28
url: dp/pattern-singleton
updated: 2018-03-08 16:03:28
categories:
- 设计模式
tags:
- 设计模式
- 单例模式
---
# 单例模式（Singleton）

单例模式是在 `GOF`的23种设计模式里较为简单的一种，下面引用百度百科介绍：
>单例模式，是一种常用的软件设计模式。在它的核心结构中只包含一个被称为单例的特殊类。通过单例模式可以保证系统中，应用该模式的类一个类只有一个实例。即一个类只有一个对象实例

<!-- more -->

许多时候整个系统只需要拥有一个的全局对象，这样有利于我们协调系统整体的行为。比如在某个服务器程序中，该服务器的配置信息存放在一个文件中，这些配置数据由一个单例对象统一读取，然后服务进程中的其他对象再通过这个单例对象获取这些配置信息。这种方式简化了在复杂环境下的配置管理。  

在Java中，确保一个类只有一个对象实例可以通过权限的修饰来实现。
# 单例模式 - 饿汉模式
单例模式的饿汉模式指全局的单例实例在第一次被使用时构建。
具体实现：
```
  // 单例模式的饿汉模式实现
  public class Singleton {
    private final static Singleton SINGLETON= new Singleton();
    // Private constructor suppresses   
    private Singleton() {}
 
    // default public constructor
    public static Singleton getInstance() {
        return SINGLETON;
    }
  }
```
在饿汉模式实现方式中，程序的主要特点是：
1. 私有构造方法
2. 私有静态属性，维护自身实例
3. 静态服务方法，获取实例
4. 初始化时候创建，消耗初始化系统资源

# 单例模式 - 懒汉模式 - 普通

懒汉模式，也是最常用的形式，饿汉模式让程序在初始化时候进行加载，有时为了节约资源，我们需要在需要的时候进行加载，这时候我们可以使用懒汉模式。
具体实现：
```
public class SingletonLayload {	
	// 私有化自身类对象
	private static SingletonLayload SINGLETON;
	// 私有化构造方法
	private SingletonLayload() {}
	
	// 静态方法获取实例
	public static SingletonLayload getInstance() {
		if(SINGLETON== null ) {
			SINGLETON= new SingletonLayload();
		}
		return SINGLETON;
	}
}
```
# 单例模式 - 懒汉模式 - 同步锁
在多线程的环境中，简单的单例模式将会出现问题，试想在上面的懒汉模式中，如果多线程并发执行`getInstance()`，当线程A执行到：
>`INSTANCE = new SingletonLayload();`

却还没有执行完毕时，线程B执行到`if(INSTANCE == null )`，此时就无法保证单例特性。
因此在多线程环境中，单例模式需要使用同步锁确保实现真正的单例。
具体实现：
```
public class SingletonLayloadSyn {
	// 私有化自身类对象
	private static SingletonLayloadSyn SINGLETON;
	// 私有化构造方法
	private SingletonLayloadSyn() {}
	// 静态方法获取实例
	public static synchronized SingletonLayloadSyn getInstance() {
		if(SINGLETON == null ) {
			SINGLETON = new SingletonLayloadSyn();
		}
		return SINGLETON;
	}

}
```
 通过在`getInstance()`方法上添加 `synchronized` 关键字可以解决多线程带来的问题。

# 单例模式 - 懒汉模式 - 双重校验锁
使用上面的（ 多线程下 - 懒汉模式 - 同步锁）方式在解决多线程问题时虽然可以达到确保线程安全的目的，但是使用了`synchronized `关键字之后在需要多次调用时，会让代码的执行效率大大降低。那么有没有在确保线程安全的同时又可以兼顾效率的方法呢？
具体实现：

```
public class SingletonLayLoadSynDCL {
	// 私有化自身类对象
	private static SingletonLayLoadSynDCL SINGLETON;
	// 私有化构造方法
	private SingletonLayLoadSynDCL() {
	}

	public static  SingletonLayLoadSynDCL getInstance() {
		if (SINGLETON == null) {
			synchronized(SingletonLayLoadSynDCL.class) {
				SINGLETON = new SingletonLayLoadSynDCL();
			}
		}
		return SINGLETON;
	}
}
```
使用 `synchronized` 确保线程安全,在SINGLETON 为 `null` 时才进行创建实例，但是仍然不能 保证在实例未创建完成时候有新的线程执行到 `if (SINGLETON == null)`；因此，仍然不够安全。
修改 `getInstance（）`方法。
具体实现：

```
public class SingletonLayLoadSynDCL {
	// 私有化自身类对象
	private static SingletonLayLoadSynDCL SINGLETON;
	// 私有化构造方法
	private SingletonLayLoadSynDCL() {
	}
	
	// 使用双重校验锁确保线程安全的同时兼顾执行效率
	public static SingletonLayLoadSynDCL getInstance() {
		if (SINGLETON == null) { // 第一重检查
			synchronized (SingletonLayLoadSynDCL.class) {
				if (SINGLETON == null) { //第二重检查
					SINGLETON = new SingletonLayLoadSynDCL();
				}
			}
		}
		return SINGLETON;

	}
}
```
看似完美的双检查模式，在理论上是没有问题的。但是在实际的情况里，有可能发生在没有构造完毕的情况下SINGLETON 引用已经不是 NULL 的情况，这时候如果有其他线程执行到`if (SINGLETON == null) { // 第一重检查`则会获取到一个不正确的 SINGLETON 引用。这是由于`JVM` 的无序写入引起的。  

幸好，在 `JDK1.5` 之后，提供了`volatile`关键字，用于确保被修饰的变量的读写不允许被控制。因此修改上面具体实现为：
```
/**
 * <p>
 * 使用双重校验锁以及volatile关键字确保线程安全的同时兼顾执行效率
 * @author  niujinpeng
 */
public class SingletonLayLoadSynDCL {
	// 私有化自身类对象
	//	private static SingletonLayLoadSynDCL SINGLETON;
	private volatile static SingletonLayLoadSynDCL SINGLETON;
	// 私有化构造方法
	private SingletonLayLoadSynDCL() {}

	// 使用双重校验锁确保线程安全的同时兼顾执行效率
	public static SingletonLayLoadSynDCL getInstance() {
		if (SINGLETON == null) {
			synchronized (SingletonLayLoadSynDCL.class) {
				if (SINGLETON == null) {
					SINGLETON = new SingletonLayLoadSynDCL();
				}
			}
		}
		return SINGLETON;

	}
}
```

# 单例模式 - 懒汉模式 - 内部类 
除了使用上面的懒汉模式实现方式之外，在解决多线程问题中，《Effective Java》的作者给出了另外一种保证线程安全且兼顾效率的方式，利用了静态内部类以及类加载特性实现。静态内部类只有在调用时才会加载，而静态属性随着类的加载而加载，类的加载初始化只会有一次。因此保证了获取实例的唯一性。
具体实现：
```
package cn.snowflow.pattern.singleton;
/**
 * <p>
 * 利用静态内部类实现线程安全且兼顾效率的单例模式
 * @author  niujinpeng
 */
public class SingletonLayloadSynSafe {
	//静态内部类
	public static class SingletonHolder{
		static final SingletonLayloadSynSafe INSTANCE = 
			new SingletonLayloadSynSafe();
	}
	// 私有化构造方法
	private SingletonLayloadSynSafe() {}
	
	// 公有方法获取实例
	public static SingletonLayloadSynSafe getInstance() {
		return SingletonHolder.INSTANCE;
	}

}

```

如果使用单例模式-饿汉模式，推荐`【单例模式 - 饿汉模式】`
如果使用单例模式-懒汉模式，推荐`【单例模式 - 懒汉模式 - 内部类 】`