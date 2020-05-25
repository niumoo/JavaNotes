---
title: Linux配置Tomcat的单机多实例
date: 2018-08-10 01:14:35
url: develop/install-tomcat-many-instance
tags:
 - Tomcat
categories:
 - Linux
---

![多示例](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1571963198487.png)

有时候需要在一个服务器上部署多个Tomcat，通过不同的端口进行区分，比如，反向代理。但是不想简单的通过复制Tomcat来实现，这样既不方便以后的升级也不方便管理，那么这时候就需要配置Tomcat的单机多实例了。
以下操作运行于Linux下。

### Tomcat 下载
Tomcat的下载可以直接到Tomcat官方网站下载自己需要的版本，博主这里下载的Tomcat8.5.*32*

```shell
// 解压
tar -zxvf apache-tomcat-8.5.32.tar.gz
```
<!-- more -->
可以看到解压后目录如下：

```shell
niu@ubuntu:~/develop/test$ cd apache-tomcat-8.5.32/
niu@ubuntu:~/develop/test/apache-tomcat-8.5.32$ ll
total 120
drwxr-x--- 2 niu niu  4096 8月  10 01:35 bin/
drwx------ 2 niu niu  4096 6月  20 12:53 conf/
drwxr-x--- 2 niu niu  4096 8月  10 01:35 lib/
-rw-r----- 1 niu niu 57092 6月  20 12:53 LICENSE
drwxr-x--- 2 niu niu  4096 6月  20 12:50 logs/
-rw-r----- 1 niu niu  1723 6月  20 12:53 NOTICE
-rw-r----- 1 niu niu  7138 6月  20 12:53 RELEASE-NOTES
-rw-r----- 1 niu niu 16246 6月  20 12:53 RUNNING.txt
drwxr-x--- 2 niu niu  4096 8月  10 01:35 temp/
drwxr-x--- 7 niu niu  4096 6月  20 12:51 webapps/
drwxr-x--- 2 niu niu  4096 6月  20 12:50 work/
```

### 配置多实例模版
要实现单Tomcat的多实例启动，首先我们要修改一下当前的Tomcat目录结构具体操作如下。为了方便，我们会先配置一个模版实例，然后在模版实例中编写一个启动停止shell脚本。以后扩展实例只需要拷贝一份修改端口号。
```shell
// 删除无用文件
rm LICENSE 
rm NOTICE 
rm RELEASE-NOTES
rm RUNNING.txt
// 创建WEB实例模版文件夹，以后部署新实例只需要拷贝一份
mkdir web-template
// 移动实例文件到实例模版文件夹
mv conf/ ./web-template/
mv logs/ ./web-template/
mv tem/ ./web-template/
mv temp/ ./web-template/
mv webapps/ ./web-template/
mv work/ ./web-template/
```
在模版文件夹下编写启动停止Tomcat的shell脚本。
```shell
// 新建sehll脚本
vim tomcat.sh
```
输入如下内容：
```shell
RETVAL=$?
# tomcat实例目录
export CATALINA_BASE="$PWD"
# tomcat安装目录，改成自己的
export CATALINA_HOME="/home/niu/develop/test/apache-tomcat-8.5.32"
# 可选
export JVM_OPTIONS="-Xms128m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=512m"
case "$1" in
start)
if [ -f $CATALINA_HOME/bin/startup.sh ];then
echo $"Start Tomcat"
$CATALINA_HOME/bin/startup.sh
fi
;;
stop)
if [ -f $CATALINA_HOME/bin/shutdown.sh ];then
echo $"Stop Tomcat"
$CATALINA_HOME/bin/shutdown.sh
fi
;;
*)
echo $"Usage:$0 {start|stop}"
exit 1
;;
esac
exit $RETVAL
```
保存退出，赋予执行权限。
```shell
chmod +x tomcat.sh
```
经过上面的操作，现在的Tomcat目录结构如下：
```shell
apache-tomcat-8.5.32
├── bin
├── lib
└── web-template
    ├── conf
    ├── logs
    ├── temp
    ├── webapps
    └── work
```
### 测试实例模版
实例模版中包含config文件夹，也就是此实例的配置文件，可以修改端口号等信息。我们没有进行修改过，默认也就是`8080`。webapps文件夹中的ROOT目录也就是Tomcat的默认发布目录，我们没有进行修改，里面存放的是Tomcat默认首页信息。
```shell
// 启动模版实例进行测试,可以看到正常启动的日志
tomcat.sh start
// 停止则使用stop
tomcat.sh stop
```
成功启动后，访问IP+8080进行测试。
![Tomcat首页](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/119d56104ea62415e369bae13f6269b6.png)
看到属性的页面，大功告成。距离多实例只有一步之遥。

### 增加一个实例
增加一个实例，只拷贝一份模版实例。然后修改端口号即可。不然会因为端口占用而无法启动。
```shell
# 拷贝一份实例
cp -r web-template/ web-9090
# 修改端口号为9090
vim conf/server.xml
# 修改HTTP端口号从8080变为9090，第69行左右
<Connector port="9090" protocol="HTTP/1.1"
  connectionTimeout="20000"
  redirectPort="8443" />
# 修改SHUTDOWN端口号从8005变为9005，第22行左右
ver port="9005" shutdown="SHUTDOWN">
# 保存，退出，启动
tomcat.sh start
```

此时可以访问IP+端口9090进行访问测试。
![9090实例访问测试](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/e73313a3dd0634c258ae09e34e8be295.png)

到这里，多实例已经部署完成，关闭各个Tomcat。退出终端。

增加实例只需要拷贝模版实例然后修改端口号。每个实例都有自己单独的配置，可以独立管理启动。

