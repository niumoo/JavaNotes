---
title: Manjaro Linux 入门使用教程
date: 2020-04-24 08:08:01
url: linux/linux-manjaro
tags:
 - Manjaro
 - Manjaro 软件安装
categories:
 - Linux
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

![screenfetch](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/image-20200419220905196.png)

## Manjaro 初体验

Manjaro 是一款基于 Arch LInux 的自由开源发行版，它吸收了 Arch Linux 优秀丰富的软件管理，同时提供了稳定流畅的操作体验。优雅简单是它的追求，稳定实用是它的优势。

Manjaro 和 Arch Linux 一样采用滚动发行模式，但是它的滚动更新是在 Arch Linux 更新测试一段时间之后，这也保证了系统的稳定性。话虽如此，使用中你依旧可能面临大量的更新而不知如何选择，所以，**如果使用已经满足需求，有升级的必要吗？**

### 下载镜像

镜像下载可以去 [Manjaro](https://manjaro.org/get-manjaro/) 官方网站下载，国内速度太慢也可以到[ 清华大学开源软件镜像站](https://mirrors.tuna.tsinghua.edu.cn/osdn/storage/g/m/ma/manjaro/) 进行下载。Manjaro 提供了多种桌面环境，可以根据喜好自行下载，我一般偏向于 kde 或者 gnome 桌面。

### 制作启动盘

使用 [Rufus](http://rufus.ie/) 工具以 **DD 模式**写入镜像到 U 盘，制作 U 盘启动成功之后，开机选择 U 盘进行启动即可。Rufus 工具这里选择的是 3.4 版本，经过测试，高版本的 Rufus 可能会存在分区类型等选项不能修改的 :bug: Bug。

![Rufus 3.4](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/image-20200421213247256.png)

### 安装 Manjaro

安装 Manjaro 这里不做描述，网上有很多优秀的教程可以参考。总体来说 Manjaro 安装还是比较轻松的，相比其他的 Linux 发行版，安装体验更好。特别是对显卡驱动方面的支持，一键安装，特别省心。

## 更换软件源

更换软件源为国内清华大学源，安装软件更迅速。

```shell
sudo pacman -Syy
sudo pacman-mirrors -i -c China -m rank  #选一个清华源就行
#sudo pacman -Syyu
```

在弹出的窗口里选择一个镜像源即可，我这里选择的是清华大学镜像源。

![选择清华镜像源](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/image-20200419181748778.png)

```shell
sudo vim /etc/pacman.conf
# 下面的内容添加到文件
[archlinuxcn]
SigLevel = Optional TrustedOnly
Server = https://mirrors.ustc.edu.cn/archlinuxcn/$arch
# 执行更新，导入GPG key
sudo pacman -Syy && sudo pacman -S archlinuxcn-keyring
```

## 安装输入法

```shell
sudo pacman -S fcitx-sogoupinyin
sudo pacman -S fcitx-im # 全部安装
sudo pacman -S fcitx-configtool # 图形化配置工具
```

设置中文输入法环境变量，编辑~/.xprofile文件，增加下面几行(如果文件不存在，则新建)

```shell
export GTK_IM_MODULE=fcitx
export QT_IM_MODULE=fcitx
export XMODIFIERS="@im=fcitx"
```

## 使用 zsh

没体验过 zsh 的建议试试，命令敲起来十分顺畅。

```shell
sudo pacman -S zsh
# 下载这个 install.sh 自行运行
# https://github.com/ohmyzsh/ohmyzsh/blob/master/tools/install.sh
# 下面这种方式已经失效
#sh -c "$(curl -fsSL https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
chsh -s /bin/zsha
```

## 安装常用软件

99% 的软件只需要几条命令就可以安装，像下面这样。

```shell
# 生成 ssh 密钥 ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
sudo pacman -S git
sudo pacman -S vim
sudo pacman -S visual-studio-code-bin # vscode
sudo pacman -S shadowsocks-qt5
sudo pacman -S google-chrome  # 谷歌浏览器
sudo pacman -S netease-cloud-music  # 网易云音乐
sudo pacman -S wps-office
```

网易云音乐还有一个基于 Python 编写的开源的命令行版本，使用命令行播放操控歌曲，十分极客炫酷，有兴趣的朋友可以尝试。

NetEase-MusicBox 开源地址：[https://github.com/darknessomi/musicbox](https://github.com/darknessomi/musicbox)

安装坚果云。

```shell
# 下载坚果云安装包
wget https://www.jianguoyun.com/static/exe/installer/nutstore_linux_dist_x64.tar.gz
# 安装坚果云，解压后运行
./bin/install_core.sh
# 安装所需依赖
sudo pacman -S gvfs libappindicator-gtk3 python2-gobject
```

安装 TIM / QQ.

```shell
sudo pacman -S deepin.com.qq.office
# 由于 qq 依赖了 cinnamon-settings-daemon
sudo pacman -S cinnamon-settings-daemon
/usr/lib/cinnamon-settings-daemon/csd-xsettings
# 可以尝试将上方的 csd-xsettings 加入到开自启
# 修改 TIM 字体大小，下面命令之后-》显示 DPI 120
env WINEPREFIX="$HOME/.deepinwine/Deepin-TIM" /usr/bin/deepin-wine winecfg
```

## 开发环境配置

安装 JDK，配置环境变量。

```shell
export JAVA_HOME=/home/niu/develop/program/jdk1.8.0_191
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$PATH
```

安装 maven，配置环境变量。

```shell
export M2_HOME=/home/niu/program/apache-maven-3.6.3
export PATH=$PATH:$M2_HOME/bin
```

## 字体渲染

字体渲染的好坏直接影响到使用体验，这里推荐下面几款字体。

```shell
sudo pacman -S ttf-roboto noto-fonts ttf-dejavu
# 文泉驿
sudo pacman -S wqy-bitmapfont wqy-microhei wqy-microhei-lite wqy-zenhei
# 思源字体
sudo pacman -S noto-fonts-cjk adobe-source-han-sans-cn-fonts adobe-source-han-serif-cn-fonts
```

你也可以从下面的 Git 仓库中下载微软 windows 10 字体，获得和 windows 相似的字体体验。Github 下载速度较慢，我已经克隆了一份到 Gitee 码云。

GIthub：[https://github.com/fphoenix88888/ttf-mswin10-arch](https://github.com/fphoenix88888/ttf-mswin10-arch)

Gitee：[https://gitee.com/niumoo/ttf-mswin10-arch](https://gitee.com/niumoo/ttf-mswin10-arch)

你也可以自己下载喜欢的字体复制到 `/usr/share/fonts/TTF` 文件夹下。然后使用命令 `fc-cache -fv` 刷新字体。

有时候你已经安装了不错的字体，显示效果还是不好，可以尝试调整设置里的屏幕缩放和强制字体 DPI 参数，缩放我一般不建议调整，可以调整字体 DPI 为 120 或者 144。

## 可选操作

1. 系统更新

```shell
# 更新所有软件系统
sudo pacman -Syyu
```

如果你对更新内容不是很了解，对 Linux 操作还不熟练，那么我给你的建议是没问题不要更新，不然更新之后遇到一些问题之后你可能无法搞定。当然这个概率很小。

![sudo pacman -Syyu 更新系统](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/image-20200420080215272.png)

2. 显卡驱动

安装显卡驱动，如果你开机关机没有任何问题，就不要折腾了。如果你不幸开机或者关机卡死，可以尝试安装一下驱动，在硬件设定里点击 **Auto Install Proprietary Driver** 自动检测安装，这个显卡驱动自动检测安装是我喜欢 Manjaro 的原因之一。。

![安装显卡驱动](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2020/image-20200420215953766.png)

3. 垃圾清理

清除系统中无用的包。

```shell
sudo pacman -R $(pacman -Qdtq)
```

清除已下载的安装包。

```shell
sudo pacman -Scc
```

## 总结

几天使用下来，Manjaro 的体验比想象中的要好，在这之前我也体验过把 Deepin 和 Ubunut 作为主力系统，Deepin 对于某些机器显卡驱动不是特别友好，经常会开机或者关机卡死，但是桌面环境相对优秀。而 Ubuntu 在作为桌面环境使用时，经常会出现莫名的内部错误，安装软件有时候比较繁琐，当然 Ubuntu 的优点也很多，不错的界面，活跃的社区等。在Manjaro 的体验中我发现困扰很久的显卡驱动问题竟然可以如此轻松的解决。KDE 桌面环境也很舒服，目前不尽人意的地方在于字体渲染，不管我是调整缩放还是调整字体 DPI 效果都不明显。可能是我没有找到正确的方法吧，毕竟有的朋友可以开箱即用。


### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变的优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)