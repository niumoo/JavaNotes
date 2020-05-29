---
title: 使用Apache Ant 进行Java web项目打包并部署至TOMCAT
date: 2017-11-01 18:58:23
updated: 2017-11-01 18:58:23
url: develop/tool-apache-ant
categories: 
 - 生产工具
tags: 
- Tomcat
- Ant
- 开发工具
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

### Apache Ant介绍
是一个将软件编译、测试、部署等步骤联系在一起加以自动化的一个工具，大多用于Java环境中的软件开发。由Apache软件基金会所提供。
<!-- more -->
**优点**：Ant是Apache软件基金会JAKARTA目录中的一个子项目，它有以下的优点。跨平台性。Ant是纯Java语言编写的，所以具有很好的跨平台性。操作简单。Ant是由一个内置任务和可选任务组成的。Ant运行时需要一个XML文件(构建文件)。 Ant通过调用target树，就可以执行各种task。每个task实现了特定接口对象。由于Ant构建文件 是XML格式的文件，所以很容易维护和书写，而且结构很清晰。由于Ant的跨平台性和操作简单的特点，它很容易集成到一些开发环 境中去。


 **那么我们如何使用呢？这里用一个例子进行演示如何用ANT来编译一个Java web项目。**
### 下载Apache Ant

 直接到[官方网站](http://ant.apache.org/)下载，
或者直接：
>[Windows版本](https://mirrors.tuna.tsinghua.edu.cn/apache//ant/binaries/apache-ant-1.9.9-bin.zip)
>
>[Linux版本](https://mirrors.tuna.tsinghua.edu.cn/apache//ant/binaries/apache-ant-1.9.9-bin.tar.gz)

### 安装Apache Ant
直接解压下载的压缩包，可以看到Ant的目录结构。

Bin目录：Ant命令  
Lib目录：Ant所需要的jar包  
manual：用户参考文档  

### 配置Apache Ant

1：首先需要安装了JDK,并且配置环境变量。  
2：配置ANT_HOME/bin目录即可。（自行配置）  
3：测试是否配置成功。
运行命令ant -version查看版本号，配置成功可以看到：
```shell
C:\>ant -version
Apache Ant(TM) version 1.9.9 compiled on February 2 2017
C:\>
```

### Apache Ant编译准备
这里用一个Java web项目为例。
看下JavaWeb项目整体目录结构。

```shell
├─.settings
├─src
│  └─net
│      └─codingme
│          ├─controller
│          ├─dao
│          ├─mapping
│          ├─po
│          ├─service
│          │  └─impl
│          └─util
└─WebContent
    ├─attached
    ├─css
    ├─fonts
    ├─js
    │  └─google-code-prettify
    ├─META-INF
    └─WEB-INF
        ├─lib
        └─view
            └─post
```
#### 编写用于编译的build.xml
 ![enter image description here](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/6b6e8cf5d117335e278227d6b6d0de0d.jpg)

build.xml具体内容。 这里已经把注释已经写的非常清楚，具体内容如下。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="BlogV2"  default="run" basedir=".">
	<!-- java源代码目录 -->
	<property name="src.dir" location="src" />
	<!-- 构建存放目录 -->
	<property name="build.dir" location="build" />
	<!-- class文件存放目录 -->
	<property name="build.classes" location="${build.dir}/classes" />
	<!-- 打包目录 -->
	<property name="build.war" location="${build.dir}/war" />

	<!-- tomcat根目录 -->
	<property name="tomcat.home" location="D:\CodeProgram\tomcat7" />
	<!-- tomcat项目存放-->
	<property name="tomcat.WebContent" location="${tomcat.home}/webContent" />

	<!-- war包名字 -->
	<property name="war.name" value="BlogV2" />
	<!-- web 根目录 -->
	<property name="web.root" location="WebContent" />
	<!-- web jar包所在 -->
	<property name="web.lib" location="WebContent/WEB-INF/lib" />

	<!-- 加载环境变量 -->
	<property environment="env" />

	<!-- 定义编译时的jar -->
	<path id="compile.path">
		<fileset dir="${web.lib}" includes="*.jar">
		</fileset>
		<fileset dir="${tomcat.home}/lib">
			<include name="**/*.jar" />
		</fileset>
	</path>

	<!-- 初始化需要的文件夹 -->
	<target name="init" description="初始化">
		<mkdir dir="${build.dir}" />
		<mkdir dir="${build.classes}" />
		<mkdir dir="${build.war}" />
		<echo>初始化工作结束！</echo>
	</target>
	
	<!-- 编译java文件为class -->
	<target name="compile" depends="init" description="编译">
		<javac destdir="build/classes" srcdir="src" includeantruntime="false" fork="true">
			<compilerarg line="-encoding UTF-8 " />
			<classpath refid="compile.path" />
		</javac>
		<echo>初始化工作结束！</echo>
		<echo message="编译完成！" />
	</target>
	<!-- 把项目打包成war包存放在指定位置 -->
	<target name="war" depends="compile" description="打包war文件">
		<war destfile="${build.war}/${war.name}.war">
			<fileset dir="${web.root}" includes="**/*.*" />
			<lib dir="${web.lib}" />
			<classes dir="${build.classes}" />
		</war>
		<echo>打包完成！</echo>
	</target>
	
	<!-- 拷贝war包到tomcat发布目录 -->
	<target name="deploy" depends="war" description="发布">
		<copy todir="${tomcat.WebContent}">
			<fileset dir="${build.war}" includes="*.war" />
		</copy>
		<echo>已发布到Tomcat！</echo>
	</target>
	
	<target name="clean" description="清理">
		<delete dir="${build.dir}" />
		<delete dir="${tomcat.WebContent}/${web.name}"/>
		<delete file="${tomcat.WebContent}/${web.name}.war"/>
		<echo>清理完成！</echo>
	</target>
	
</project>
```
#### Build.xml文件的一些解释

```xml
<?xml version="1.0" encoding="UTF-8" ?>   
```

build.xml中的第一句话，没有实际的意义。

```xml
<project name="BlogV2" default="run" basedir=".">  
</project>  
```

包含Ant的所有内容，name即名字，basedir即工作根目录，default即默认操作。

```xml
<property name="src.dir" location="src" />    
```

类似于声明变量：即src.dir代表localtion里配置的目录。

```xml
<target name="war" depends="compile" description="打包war文件">
	<war destfile="${build.war}/${war.name}.war">
		<fileset dir="${web.root}" includes="**/*.*" />
		<lib dir="${web.lib}" />
		<classes dir="${build.classes}" />
	</war>
	<echo>打包完成！</echo>
</target>
```


Ant每一件要做的事情我们都需要写 成**target**的形式，并给出一个名字name，depends="compile"表示在做这个target之前需要先执行compile，description即描述。
中间则是打包成war包的格式需要。

参考上面的build.xml文件，我相信可以解决很多简单的编译问题。  
但是Ant的功能远远不止于此，其他参数可以参考[官方文档](http://ant.apache.org/manual-1.9.x/index.html)

### Apache Ant编译开始
在做了上面的准备工作之后，我们离成功只差一步之遥了。
命令切换到build.xml文件所在目录。
查看当前目录里build.xml里编写的功能
用命令 ant -p（在执行ant命令时默认会使用名字为build.xml的文件）
```shell
C:\Users\83981\Desktop\Apache Ant\BlogV2>ant -p
Buildfile: C:\Users\83981\Desktop\Apache Ant\BlogV2\build.xml
Main targets:
 clean    清理
 compile  编译
 deploy   发布
 init     初始化
 war      打包war文件
Default target: run
	
C:\Users\83981\Desktop\Apache Ant\BlogV2>
```
Ant的常用操作：
ant  target_name  执行相应的操作。  
ant clean  清理文件夹。  
ant deploy  进行发布。

```shell
C:\Users\83981\Desktop\Apache Ant\BlogV2>ant clean
Buildfile: C:\Users\83981\Desktop\Apache Ant\BlogV2\build.xml

clean:
     [echo] 清理完成！

BUILD SUCCESSFUL
Total time: 0 seconds

C:\Users\83981\Desktop\Apache Ant\BlogV2>ant deploy
Buildfile: C:\Users\83981\Desktop\Apache Ant\BlogV2\build.xml

init:
    [mkdir] Created dir: C:\Users\83981\Desktop\Apache Ant\BlogV2\build
    [mkdir] Created dir: C:\Users\83981\Desktop\Apache Ant\BlogV2\build\classes
    [mkdir] Created dir: C:\Users\83981\Desktop\Apache Ant\BlogV2\build\war
     [echo] 初始化工作结束！

compile:
    [javac] Compiling 30 source files to C:\Users\83981\Desktop\Apache Ant\BlogV2\build\classes
     [echo] 初始化工作结束！
     [echo] 编译完成！

war:
      [war] Building war: C:\Users\83981\Desktop\Apache Ant\BlogV2\build\war\BlogV2.war
     [echo] 打包完成！

deploy:
     [copy] Copying 1 file to D:\CodeProgram\tomcat7\webContent
     [echo] 已发布到Tomcat！

BUILD SUCCESSFUL
Total time: 6 seconds

C:\Users\83981\Desktop\Apache Ant\BlogV2>
```

项目已经编译并打包成war格式拷贝至指定的tomcat目录
操作结束，关于Ant的其他命令可以执行ant -help进行查看。

### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)