---
title: Linux定时任务crontab的使用
date: 2018-05-23 22:14:25
url: linux/linux-crontab
tags:
 - Crontab
 - 定时任务
categories:
 - Linux
thumbnail: /static/blog/images/logo/linux-crontab.png
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。  
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![crontab logo](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/d82883e1a2b8c5b9e949e80047091e74.jpg)



### crontab介绍

crontab经常被用于设置周期性任务，Linux系统本身就存在的许多计划性任务是由cron服务来控制，因此默认情况下，这个cron服务也是默认启动的。crond常常在后台运行，每一分钟检查是否有预定的作业需要执行。Linux 系统为使用者控制计划任务提供的命令：`crontab 命令`。
<!-- more -->

### crontab文件
`Linux的系统任务调度文件`，可以在/etc/crontab文件中查看系统计划任务信息。在CentOS7中文件内容如下：
```shell
[root@VM_105_191_centos ~]# cat /etc/crontab 
SHELL=/bin/bash #使用哪个shell
PATH=/sbin:/bin:/usr/sbin:/usr/bin #系统命令的路径
MAILTO=root  #任务执行结果通知给root用户，不写则不通知

# For details see man 4 crontabs
# 任务命令格式说明
# Example of job definition:
# .---------------- minute (0 - 59)
# |  .------------- hour (0 - 23)
# |  |  .---------- day of month (1 - 31)
# |  |  |  .------- month (1 - 12) OR jan,feb,mar,apr ...
# |  |  |  |  .---- day of week (0 - 6) (Sunday=0 or 7) OR sun,mon,tue,wed,thu,fri,sat
# |  |  |  |  |
# *  *  *  *  * user-name  command to be executed
```
`Linux的用户任务调度文件`，系统会把用户使用crontab工具定制的周期性计划任务保存在：`/var/spool/cron/`目录中，里面的文件名和用户名相同，对应每个用户，可以直接查看。

### 计划任务时间设置
上面的Crontab文件没有任何计划任务，但是有一份详细的crontab文件格式说明，我们按照文件中的内容可以很好的理解五个*号区域所代表的意思。
```shell
# 指令格式说明：
# .---------------- 分 (0 - 59)
# |  .------------- 时 (0 - 23)
# |  |  .---------- 日 (1 - 31)
# |  |  |  .------- 月 (1 - 12)
# |  |  |  |  .---- 星期 (0 - 6) (星期日=0 或 7) 
# |  |  |  |  |
# *  *  *  *  * 操作指令
```
这里需要注意的是，如果日期和星期同时被设定，那么其中的一个条件被满足时，指令便会被执行。
在上面的每个区域中，

| 说明 |
| :-------- : |
|星号（'*'）代表任何可能的值。例如，在“小时域”里的星号等于是“每一个小时”，等等    |   
|逗号（','）分开的值，例如：“1,3,4,7,8”    | 
|连词符（'-'）指定值的范围，例如：“1-6”，意思等同于“1,2,3,4,5,6”    | 
|某些cron程序的扩展版本也支持斜线（'/'）操作符，例如，“*/3”在小时域中等于“0,3,6,9,12,15,18,21”等被3整除的数；    | 

实例：每1分钟输出一次hello
命令：\* \* \* \* \*  command

实例：每小时的第3和第15分钟执行
命令：3,15 \* \* \* \* command

实例：在上午8点到11点的第3和第15分钟执行
命令：3,15 8-11 \* \* \* command

### crontab命令
命令中的`File`是命令文件的名字，在这个文件中编写了符合上述规则的计划任务，命令则表示将这个File做为crontab的任务载入到crontab。如果在命令行中没有指定这个文件，crontab命令将接受标准输入（键盘）上键入的命令，并将它们载入crontab。载入crontab之后可以在`/var/spool/cron/[user]`文件查看计划任务信息。
```shell
用法:
 crontab [参数] 文件 
 crontab [参数]
 
常用参数:
 -u <user>  配置用户计划任务，默认当前用户
 -e         编辑用户计划任务，默认当前用户
 -l         列出用户计划任务，默认当前用户更
 -r         清空用户计划任务，默认当前用户
 -i         删除用户计划任务，有确认提示
```

所以，如果要把一个编写好的crontab文件添加到当前用户的计划任务，可以使用
$ crontab crontab_file

### 一些示例
实例1：每1分钟执行一次myCommand
\* \* \* \* \* myCommand

实例2：每小时的第3和第15分钟执行
3,15 \* \* \* \* myCommand

实例3：在上午8点到11点的第3和第15分钟执行
3,15 8-11 \* \* \* myCommand

实例4：每隔两天的上午8点到11点的第3和第15分钟执行
3,15 8-11 \*/2  \*  \* myCommand

实例5：每周一上午8点到11点的第3和第15分钟执行
3,15 8-11 \* \* 1 myCommand

实例6：每晚的21:30重启smb
30 21 \* \* \* /etc/init.d/smb restart

实例7：每月1、10、22日的4 : 45重启smb
45 4 1,10,22 \* \* /etc/init.d/smb restart

实例8：每周六、周日的1 : 10重启smb
10 1 \* \* 6,0 /etc/init.d/smb restart

实例9：每天18 : 00至23 : 00之间每隔30分钟重启smb
0,30 18-23 \* \* \* /etc/init.d/smb restart

实例10：每星期六的晚上11 : 00 pm重启smb
0 23 \* \* 6 /etc/init.d/smb restart

实例11：每一小时重启smb
\* \*/1 \* \* \* /etc/init.d/smb restart

实例12：晚上11点到早上7点之间，每隔一小时重启smb
0 23-7 \* \* \* /etc/init.d/smb restart


**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变得优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)