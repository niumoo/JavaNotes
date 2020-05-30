---
title: CSS - 使用CSS实现Mac窗口效果
date: 2018-08-15 05:42:55
updated: 2018-08-15 05:42:55
url: html-css/css-mac-window
categories:
- 前端开发
tags:
- Css
- Html
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。


来不及了，直接贴代码。
### HTML代码

```html
<div class="user-bio">
	<div class="user-bio-top">
		<div class="circle-red"></div>
		<div class="circle-yellow"></div>
		<div class="circle-green"></div>
	</div>
	<div class="user-bio-body">
		Wait,i know you<br>
		keep hungry keep foolish<br>
		苍苍之天不可得久视，堂堂之地不可得久履，道此绝矣！<br/>
		告后世及其孙子，忽忽锡锡，恐见故里，毋负天地，<br>
		更亡更在，去如舍庐，下敦闾里！人固当死，慎毋敢佞。
	</div>
</div>
```
<!-- more -->
### CSS代码
主要是三个圆点
```css
.user-bio {
    font-size: 14px;
    color: #6a737d;
}
.user-bio .user-bio-top {
    height: 15px;
    background-color: #E3E3E3;
    padding: 8px 12px;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
}
.user-bio .user-bio-body {
    background-color: #EEEEEE;
    width: 100%;
    font-size: 13px;
    color: #666666;
    overflow: auto;
    padding: 20px;
    font-family: "Source Code Pro", Consolas, Menlo, Monaco, "Courier New", monospace;
}
.user-bio .user-bio-top div{
    float: left;
    margin-right: 10px;
    width:13px;
    height:13px;
    background-color:#FF5F57;
    border-radius:50px;
}
.user-bio .user-bio-top .circle-yellow{
    float: left;
    margin-right: 10px;
    width:13px;
    height:13px;
    background-color:#FFBD2E;
    border-radius:50px;
}
.user-bio .user-bio-top .circle-green{
    float: left;
    margin-right: 10px;
    width:13px;
    height:13px;
    background-color:#28CA42;
    border-radius:50px;
}
```
### 最终效果
![MAC-Window-css](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/1f2dd02d503fcd77d79e41a208349c59.jpg)

**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)