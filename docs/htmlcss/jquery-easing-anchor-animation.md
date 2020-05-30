---
title: Jquery - 使用jquery-easing.js实现页面锚点平滑滚动
date: 2018-08-19 08:48:03
updated: 2018-08-19 08:48:03
url: html-css/jquery-easing-anchor-animation
categories:
- 前端开发
tags:
- Css
- Javascript
- 平滑滚动
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。


### 使用jquery-easing.js实现页面锚点平滑滚动

使用[jquery easing.js](http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.3.js "jquery easing.js")可以实现多动动画效果。
jquery easing.js中定义了很多种不同的动画效果，动画速度和时间的关系图可以参考此图：

![](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/f81d0b4842589fcbd4d085cdd7cd64b1.jpg)
<!-- more -->


使用easeInOutExpo效果示例锚点的平滑滚动效果，直接上代码。

### 效果图

![平滑滚动](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/4176b134e77e84d73d0fceb55b96b521.gif)
<!-- more -->
### HTML代码
```html
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <title>平滑滚动</title>
</head>
<body>
	<p id="p1">1</p>
	<p>2</p>
	<p>3</p>
	<p>4</p>
	<p>5</p>
	<p>6</p>
	<p>7</p>
	<p>8</p>
	<p>9</p>
	<p>10</p>
	<p>11</p>
	<p>12</p>
	<p>13</p>
	<p>14</p>
	<p>15</p>
	<p>16</p>
	<p>17</p>
	<p>18</p>
	<p>19</p>
	<p>20</p>
	<p>21</p>
	<p>22</p>
	<p>23</p>
	<p>24</p>
	<p>25</p>
	<p>26</p>
	<p>27</p>
	<p>28</p>
	<p>29</p>
	<p>30</p>
	<p>31</p>
	<a href="#p1">TO P1</a>

    <script src="js/jquery.min.js"></script>
	<script src="js/Anchor-animation.js"></script>
	<script src="js/jquery-easing.js"></script>
</body>
</html>

```


### Anchor-animation.js代码
```javascript
(function($) {
    // Smooth scrolling using jQuery easing
    $('a[href*="#"]:not([href="#"])').click(function() {
        if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
            if (target.length) {
                $('html, body').animate({
                    scrollTop: target.offset().top
                }, 1000, "easeInOutExpo");
                return false;
            }
        }
    });
})(jQuery); // End of use strict
```

### jquery-easing.js代码
此处因为只使用到了easeInOutExpo方法，所以我删除了其他没有用到的方法，只保留了easeInOutExpo方法。

```javascript
jQuery.easing['jswing'] = jQuery.easing['swing'];

jQuery.extend( jQuery.easing,
{
	def: 'easeOutQuad',
	swing: function (x, t, b, c, d) {
		//alert(jQuery.easing.default);
		return jQuery.easing[jQuery.easing.def](x, t, b, c, d);
	},
	
	easeInOutExpo: function (x, t, b, c, d) {
		if (t==0) return b;
		if (t==d) return b+c;
		if ((t/=d/2) < 1) return c/2 * Math.pow(2, 10 * (t - 1)) + b;
		return c/2 * (-Math.pow(2, -10 * --t) + 2) + b;
	}
});


```

**最后的话**

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 **Star** 和完善，希望我们一起变的优秀。

文章有帮助可以点「**赞**」在看或 **Star**，我都喜欢，谢谢你！  
要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号，公众号回复 666 可以领取很多**资料**。

![公众号](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets@439f6a5f6bd130e2aec56f3527656d6edb487b91/webinfo/weixin-public.jpg)