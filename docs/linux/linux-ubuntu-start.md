---
title: Ubuntu18 的超详细常用软件安装
date: 2018-11-16 09:51:11
url: linux/start-ubuntu
tags:
 - Ubuntu
 - Ubuntu软件安装
categories:
 - Linux
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![Ubuntu-desktop](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/877fecc7f272598c29084c2a621df2c1.png)

心血来潮，在笔记本安装了Ubuntu 18 用于日常学习，于是有了下面的安装记录。
## Gnome-Tweak-Tool

gnome-tweak-tool可以打开隐藏的设置，可以详细的对系统进行配置，以及安装主题和扩展等功能。

```shell
// 安装
sudo apt install gnome-tweak-tool
// 安装扩展
sudo apt install gnome-shell-extensions
alt+f2 r 回车
// 安装浏览器扩展工具
sudo apt install chrome-gnome-shell
```

<!-- more -->

## N卡驱动异常
### 开机关机异常
因为使用了N卡`开源`驱动，`N卡驱动`和ubuntu系统的兼容性存在问题，有时会导致无法开机，开机循环登录，关机注销卡死等一系列问题，如果你也碰到这样的问题，可以继续向下看。笔者在使用过程中需要同样问题，升级驱动无果之后决定直接禁用掉N卡驱动。

开机先进入登录页面，`CTRL+ALT+F2`进入命令行模式。

```shell
// purge（彻底删除软件和配置）
sudo apt-get purge nvidia-*
// 进入 /配置文件/自动载入模块(类似windows系统下的服务)配置文件
cd /etc/modprobe.d/
// 使用vim 编辑（不存在会新建）一个叫blacklist-nouveau.conf的文件
sudo vim blacklist-nouveau.conf
// 在编辑模式下，按i(insert)进入编辑模式，输入
blacklist nouveau
options nouveau modeset=0
// 按一次esc退出编辑模式，再按一次“冒号”，输入wq（保存并退出）
// 重置内核引导
sudo update-initramfs -u
// 重启ubuntu
sudo reboot
```
重启之后就可以正常登录进桌面了，但是笔者发现当连接多个显示器的时候，不能进行扩展显示，应该是没有N卡驱动影响到的，如果没有多个显示，那么可以就此停止折腾了。

### 安装N卡驱动

无奈有两个显示器，不用起来还是有点不舒服的，因此有了下面的操作，安装nvidia官方驱动。

把 nouveau 驱动加入黑名单

```shell
$sudo nano /etc/modprobe.d/blacklist-nouveau.conf
```
在文件 blacklist-nouveau.conf 中加入如下内容：
```shell
blacklist nouveau
blacklist lbm-nouveau
options nouveau modeset=0
alias nouveau off
alias lbm-nouveau off
```
禁用 nouveau 内核模块
```shell
$echo options nouveau modeset=0 | sudo tee -a /etc/modprobe.d/nouveau-kms.conf
$sudo update-initramfs -u
```
可以用lsmod看看禁止成功没有
```shell
lsmod | grep nouveau
```
然后开始安装Nvidia驱动
```shell
sudo add-apt-repository ppa:graphics-drivers/ppa
sudo apt update
sudo ubuntu-drivers autoinstall
```
重启
```shell
sudo apt install nvidia-cuda-toolkit gcc-6
nvcc --version
```
用lsmod看看驱动安装成功没有
```shell
lsmod | grep nvidia
```
安装cuda-toolkit，介绍可以参考 [https://developer.nvidia.com/cuda-toolkit](https://developer.nvidia.com/cuda-toolkit)
```shell
sudo apt install nvidia-cuda-toolkit gcc-6
nvcc --version
```

## 纸飞机Shadowsocks
```shell
sudo apt-get update
sudo apt install shadowsocks
// 自行编写配置文件 /etc/shadowsocks.json
// 启动
sslocal -c /etc/shadowsocks.json
```

##  JDK环境变量

JDK下载解压此处不说。环境变量配置如下。

```shell
export JAVA_HOME=/home/niu/develop/program/jdk1.8.0_191
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$PATH

export JAVA_HOME=/home/niu/develop/program/jdk1.8.0_191
export JRE_HOME=/home/niu/develop/program/jdk1.8.0_191/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
```

##  IDEA图标
IDEA下载解压此处不说。

/usr/share/applications目录下，如果我们要创建桌面快捷方式，需要在该目录下创建一个名为“idea.desktop”的文件。

```shell
[Desktop Entry]
Name=IdeaIU
Comment=IdeaIU
Exec=env JAVA_HOME=/home/niu/develop/program/jdk1.8.0_191 /home/niu/develop/program/idea-IU-182.4892.20/bin/idea.sh
Icon=/home/niu/develop/program/idea-IU-182.4892.20/bin/idea.png
Terminal=false
Type=Application
Categories=Application;Development;
```

##  Sublime Text 3
```shell
wget -qO - https://download.sublimetext.com/sublimehq-pub.gpg | sudo apt-key add -
echo "deb https://download.sublimetext.com/ apt/stable/" | sudo tee /etc/apt/sources.list.d/sublime-text.list

sudo apt-get update
sudo apt-get install sublime-text
```
##  字体YaHeiConsolas
```shell
wget http://www.mycode.net.cn/wp-content/uploads/2015/07/YaHeiConsolas.tar.gz
tar -zxvf YaHeiConsolas.tar.gz
sudo mkdir -p /usr/share/fonts/YaHeiConsolas
sudo cp YaHeiConsolas.ttf /usr/share/fonts/YaHeiConsolas
cd /usr/share/fonts/YaHeiConsolas
sudo chmod 644 YaHeiConsolas.ttf
sudo mkfontscale
sudo mkfontdir
sudo fc-cache -fv  
```
## SecureCRT

直接到官网注册下载。下载完毕之后可以试用30天。

下面是注册信息的生成，可能不适用于最新版本。

```shell
➜  software sudo perl securecrt_linux_crack.pl /usr/bin/SecureCRT
crack successful

License:

	Name:		xiaobo_l
	Company:	www.boll.me
	Serial Number:	03-94-294583
	License Key:	ABJ11G 85V1F9 NENFBK RBWB5W ABH23Q 8XBZAC 324TJJ KXRE5D
	Issue Date:	04-20-2017
```

## mysql5.7
### 安装Mysql5.7
```shell
# 安装mysql服务
sudo apt-get install mysql-server
# 安装客户端
sudo apt install mysql-client
# 安装依赖
sudo apt install libmysqlclient-dev
# 检查状态
sudo netstat -tap | grep mysql
```
> mysql5.7安装完成后普通用户不能进mysql，原因：root的plugin被修改成了auth_socket，用密码登陆的plugin应该是mysql_native_password，直接用root权限登录就不用密码,修改root密码和登录验证方式。

```shell
# root权限进入mysql
sudo mysql
mysql> select user, plugin from mysql.user;
+------------------+-----------------------+
| user             | plugin                |
+------------------+-----------------------+
| root             | auth_socket           |
| mysql.session    | mysql_native_password |
| mysql.sys        | mysql_native_password |
| debian-sys-maint | mysql_native_password |
+------------------+-----------------------+
4 rows in set (0.00 sec)

mysql> update mysql.user set authentication_string=PASSWORD('123456'), plugin='mysql_native_password' where user='root';
Query OK, 1 row affected, 1 warning (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 1

mysql> flush privileges;
Query OK, 0 rows affected (0.01 sec)

mysql> exit
Bye
# 重启mysql
niu@ubuntu:~$ sudo /etc/init.d/mysql restart
[ ok ] Restarting mysql (via systemctl): mysql.service.
```
### 远程登录mysql
```shell
# 修改配置文件，注释掉bind-address = 127.0.0.1
niu@ubuntu:~$ sudo vim /etc/mysql/mysql.conf.d/mysqld.cnf 
niu@ubuntu:~$ mysql -uroot -p
Enter password: 

mysql> grant all on *.* to root@'%' identified by '123456' with grant option;
Query OK, 0 rows affected, 1 warning (0.00 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.00 sec)

mysql>  exit
Bye
# 重启mysql
niu@ubuntu:~$ sudo /etc/init.d/mysql restart
```

## 安装typora
```shell
// or run:
// sudo apt-key adv --keyserver keyserver.ubuntu.com--recv-keys BA300B7755AFCFAE
wget -qO - https://typora.io/linux/public-key.asc | sudo apt-key add -
// add Typora's repository
sudo add-apt-repository 'deb https://typora.io/linux ./'
sudo apt-get update
// install typora
sudo apt-get install typora
```
##  邮件客户端
```shell
wget https://github.com/nylas/nylas-mail/releases/download/2.0.14/NylasMail-2.0.14.deb
sudo dpkg -i NylasMail-2.0.14.deb
sudo apt-get -f install
```
安装之后发现要连接服务器，但是服务器报错，且了解到需要收费，因此放弃。

改用mailspring，界面好评，使用一天之后发现在邮件很多的时候会卡顿，还会出现服务器连接不上的情况，且没有设置pop3的地方，只有imap设置。因此放弃。

最后改用大名鼎鼎TｈunderBird。

```shell
sudo apt-get install thunderbird-locale-uk thunderbird-locale-vi thunderbird-locale-zh-cn
```



## 安装搜狗拼音输入法

```shell
// 卸载自带的中文输入法
sudo apt remove 'ibus*'
// 安装fcitx输入法配置框架
sudo apt install fcitx-bin fcitx-table
// 在设置语言中，选择语言输入框架为fcitx，应用到整个系统。
// 下载搜狗拼音linux版本
https://pinyin.sogou.com/linux/
// 搜狗拼音的官方安装教程,可以参考，也就是说先安装fcitx框架，然后安装输入法
//https://pinyin.sogou.com/linux/help.php
// 双击安装
```
卸载搜狗拼音。

```shell
   sudo apt-get  remove  sogoupinyin
   sudo apt-get  purge  sogoupinyin
   sudo apt-get autoremove
```

## VLC播放器
```shell
安装解码器
sudo apt-get install ubuntu-restricted-extras 
安装VLC
sudo apt-get install vlc browser-plugin-vlc
```

## 点击任务栏图表最小化
```shell
gsettings set org.gnome.shell.extensions.dash-to-dock click-action 'minimize'
```

## QQ TIM 迅雷
Linux下QQ，TIM 一直体验不好，庆幸发现了目前体验最好的deepin 移植版。
直接看[链接](https://github.com/wszqkzqk/deepin-wine-ubuntu)

## wine程序图标放到顶部

使用这个功能需要先安装gnome-tweak-tool以及gnome-shell-extensions

https://extensions.gnome.org/extension/1031/topicons/

```shell
TopIcons Plus
Applications Menu
```

## 登录页面背景

18.04登录背景相关的配置是用css的：/etc/alternatives/gdm3.css。如果你熟悉CSS规则， 可以很方便的编写出自己喜欢的登录页面样式。


```css
//找到默认的这个部分
lockDialogGroup {
  background: #2c001e url(resource:///org/gnome/shell/theme/noise-texture.png);
  background-repeat: repeat; 
}
//改为
lockDialogGroup {
  background: #2c001e url(file:///usr/share/backgrounds/mypicture.jpg); 
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center; 
}
```

![Ubuntu-desktop](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/7bcdeb5f8902fd0be6662bbb6397de32.png)

**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)