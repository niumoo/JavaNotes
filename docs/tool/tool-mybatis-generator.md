---
title: 使用MyBatis Generator自动生成Model、Dao、Mapper相关代码
date: 2017-11-01 06:58:23
updated: 2017-11-01 06:58:23
url: develop/tool-mybatis-generator
categories:
 - 生产工具
tags:
 - Mybatis
 - Mybatis Generator
 - 开发工具
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

### MyBatis Generator？

MyBatis是一款非常流行的开源框架，在简单的进行框架配置后可以快速的进入开发阶段，主要的工作是编写Mapper文件和相关Dao接口以及实体类，久之，麻烦，发现这个工具，可以自动化的生成Model、Dao、Mapper相关代码。可谓是是开发MyBatis神器。

> Mybatis generator的功能介绍：[MyBatis发电机简介](http://www.mybatis.org/generator/ "MyBatis发电机简介")


<!-- more -->

### 工具准备
- mysql-connector-java-5.1.6.jar 用于连接数据库
	 [mybatis-generator-core-1.3.5.jar](http://central.maven.org/maven2/org/mybatis/generator/mybatis-generator-core/1.3.5/mybatis-generator-core-1.3.5.jar "mybatis-generator-core-1.3.5.jar")		用于生成相关代码(点击直接下载)
- generator.xml 配置生成逻辑

把工具放在统一目录下
我在此处放置在了D:/generator之下：
```shell
D:\generator>dir

2017/07/12  10:11             3,242 generator.xml
2017/07/11  20:35           555,960 mybatis-generator-core-1.3.5.jar
2017/06/28  21:44           703,265 mysql-connector-java-5.1.6.jar
               3 个文件      1,262,467 字节

D:\generator>
```
###配置generator.xml
注意修改相关信息，数据库用户名和密码，添加要生成的表名称。
**由于配置了javaModelGenerator，需要在同目录下新建文件夹src**
```xml
 <!-- 生成模型的包名和位置 -->  
        <javaModelGenerator targetPackage="net.codingme.po" targetProject="D:\generator\src">  
```

generator.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN" "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">  
<generatorConfiguration>  
    <!-- 数据库驱动包位置 -->  
    <classPathEntry location="D:\generator\mysql-connector-java-5.1.6.jar" />   
    <!-- <classPathEntry location="C:\oracle\product\10.2.0\db_1\jdbc\lib\ojdbc14.jar" />-->  
    <context id="DB2Tables" targetRuntime="MyBatis3">  
        <commentGenerator>  
            <property name="suppressAllComments" value="true" />  
        </commentGenerator>  
        <!-- 数据库链接URL、用户名、密码 -->  
         <jdbcConnection driverClass="com.mysql.jdbc.Driver" connectionURL="jdbc:mysql://localhost:3306/maven?characterEncoding=utf8" userId="root" password="123">   
        </jdbcConnection>  
        <javaTypeResolver>  
            <property name="forceBigDecimals" value="false" />  
        </javaTypeResolver>  
        <!-- 生成模型的包名和位置 -->  
        <javaModelGenerator targetPackage="net.codingme.po" targetProject="D:\generator\src">  
            <property name="enableSubPackages" value="true" />  
            <property name="trimStrings" value="true" />  
        </javaModelGenerator>  
        <!-- 生成的映射文件包名和位置 -->  
        <sqlMapGenerator targetPackage="net.codingme.mapper" targetProject="D:\generator\src">  
            <property name="enableSubPackages" value="true" />  
        </sqlMapGenerator>  
        <!-- 生成DAO的包名和位置 -->  
        <javaClientGenerator type="XMLMAPPER" targetPackage="net.codingme.dao" targetProject="D:\generator\src">  
            <property name="enableSubPackages" value="true" />  
        </javaClientGenerator>  
        <!-- 要生成那些表(更改tableName和domainObjectName就可以) -->  
        <table tableName="POST" domainObjectName="Post" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false" />  
        <table tableName="TAG" domainObjectName="Tag" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false" />
    </context>  
</generatorConfiguration>  
```
### 在当前目录下运行命令进行生成
> java -jar mybatis-generator-core-1.3.5.jar  -configfile generator.xml -overwrite 

```shell
D:\generator>java -jar mybatis-generator-core-1.3.5.jar  -configfile generator.xml -overwrite
MyBatis Generator finished successfully.

D:\generator>
```

### 查看生成的文件
```shell

D:\generator\src>tree /f
卷 software 的文件夹 PATH 列表
卷序列号为 00000042 0000:FF78
D:.
└─net
    └─codingme
        ├─dao
        │      PostMapper.java
        │      TagMapper.java
        │
        ├─mapper
        │      PostMapper.xml
        │      TagMapper.xml
        │
        └─po
                Post.java
                PostWithBLOBs.java
                Tag.java


D:\generator\src>
```
代码已经自动生成完成，拷贝粘贴修修改改即可。

**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)