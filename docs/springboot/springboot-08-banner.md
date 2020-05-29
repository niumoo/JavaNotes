---
title: Springboot 系列（八）动态Banner与图片转字符图案的手动实现
toc_number: false
date: 2019-02-25 23:40:01
url: springboot/springboot-08-banner
tags:
 - Springboot
 - Springboot Banner
categories:
 - Springboot
---

> 文章已经收录在 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) ，更有 Java 程序员所需要掌握的核心知识，欢迎Star和指教。
>
> 欢迎关注我的[公众号](https://github.com/niumoo/JavaNotes#%E5%85%AC%E4%BC%97%E5%8F%B7)，文章每周更新。

> 注意：本 Spring Boot 系列文章基于 Spring Boot 版本 **v2.1.1.RELEASE** 进行学习分析，版本不同可能会有细微差别。

![Springboot 启动 banner](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/33e6877404fed0daf8c894cec6a4d37c.png)

使用过 Springboot 的对上面这个图案肯定不会陌生，Springboot 启动的同时会打印上面的图案，并带有版本号。查看官方文档可以找到关于 banner 的描述
>The banner that is printed on start up can be changed by adding a banner.txt file to your classpath or by setting the spring.banner.location property to the location of such a file. If the file has an encoding other than UTF-8, you can set spring.banner.charset. In addition to a text file, you can also add a banner.gif, banner.jpg, or banner.png image file to your classpath or set the spring.banner.image.location property. Images are converted into an ASCII art representation and printed above any text banner.

<!--more-->
就不翻译了，直接有道翻译贴过来看个大概意思。
>可以通过向类路径中添加一个banner.txt文件或设置spring.banner来更改在start up上打印的banner。属性指向此类文件的位置。如果文件的编码不是UTF-8，那么可以设置spring.banner.charset。除了文本文件，还可以添加横幅。将gif、banner.jpg或banner.png图像文件保存到类路径或设置spring.banner.image。位置属性。图像被转换成ASCII艺术形式，并打印在任何文本横幅上面。

# 1. 自定义 banner
根据官方的描述，可以在类路径中自定义 banner 图案，我们进行尝试在放 resouce 目录下新建文件 banner.txt 并写入内容（[在线字符生成](http://patorjk.com/software/taag/#p=testall&f=Graffiti&t=niumoo)）。
```
     (_)
  _ __  _ _   _ _ __ ___   ___   ___
 | '_ \| | | | | '_ ` _ \ / _ \ / _ \
 | | | | | |_| | | | | | | (_) | (_) |
 |_| |_|_|\__,_|_| |_| |_|\___/ \___/ 版本：${spring-boot.formatted-version}
```
启动 Springboot 在控制台看到下面的输出。
```log
     (_)
  _ __  _ _   _ _ __ ___   ___   ___
 | '_ \| | | | | '_ ` _ \ / _ \ / _ \
 | | | | | |_| | | | | | | (_) | (_) |
 |_| |_|_|\__,_|_| |_| |_|\___/ \___/ 版本：(v2.1.3.RELEASE)
2019-02-25 14:00:31.289  INFO 12312 --- [           main] net.codingme.banner.BannerApplication    : Starting BannerApplication on LAPTOP-L1S5MKTA with PID 12312 (D:\IdeaProjectMy\springboot-git\springboot-banner\target\classes started by Niu in D:\IdeaProjectMy\springboot-git\springboot-banner)
2019-02-25 14:00:31.291  INFO 12312 --- [           main] net.codingme.banner.BannerApplication    : No active profile set, falling back to default profiles: default
2019-02-25 14:00:32.087  INFO 12312 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
```
发现自定义 banner 已经生效了，官方文档的介绍里说还可以放置图片，下面放置图片 banner.jpg 测试。
网上随便找了一个图片。
![Google Log](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/d9879377e30b5f37f9116e7927e35604.jpg)再次启动观察输出。
![自定义 Banner](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/935928a0c76faa399a6f49252c713afa.png)Springboot 把图案转成了 ASCII 图案。
# 2. ASCII 图案生成原理
看了上面的例子，发现 Springboot 可以把图片转换成 ASCII 图案，那么它是怎么做的呢？我们或许可以想象出一个大概流程。
1. 获取图片。
2. 遍历图片像素点。
3. 分析像素点，每个像素点根据颜色深度得出一个值，根据明暗度匹配不同的字符。
4. 输出图案。

Springboot 对图片 banner 的处理到底是不是我们上面想想的那样呢？直接去源码中寻找答案。
```java
/** 位置：org.springframework.boot.SpringApplicationBannerPrinter */
//方法1：
public Banner print(Environment environment, Class<?> sourceClass, Log logger) {
	    // 获取 banner  调用方法记为2
		Banner banner = getBanner(environment);
		try {
			logger.info(createStringFromBanner(banner, environment, sourceClass));
		}
		catch (UnsupportedEncodingException ex) {
			logger.warn("Failed to create String for banner", ex);
		}
		// 打印 banner
		return new PrintedBanner(banner, sourceClass);
}
// 方法2
private Banner getBanner(Environment environment) {
		Banners banners = new Banners();
		// 获取图片banner，我们只关注这个，调用方法记为3
		banners.addIfNotNull(getImageBanner(environment));
		banners.addIfNotNull(getTextBanner(environment));
		if (banners.hasAtLeastOneBanner()) {
			return banners;
		}
		if (this.fallbackBanner != null) {
			return this.fallbackBanner;
		}
		return DEFAULT_BANNER;
	}
// 方法3
/** 获取自定义banner文件信息 */
private Banner getImageBanner(Environment environment) {
	// BANNER_IMAGE_LOCATION_PROPERTY = "spring.banner.image.location";
		String location = environment.getProperty(BANNER_IMAGE_LOCATION_PROPERTY);
		if (StringUtils.hasLength(location)) {
			Resource resource = this.resourceLoader.getResource(location);
			return resource.exists() ? new ImageBanner(resource) : null;
		}
		// IMAGE_EXTENSION = { "gif", "jpg", "png" };
		for (String ext : IMAGE_EXTENSION) {
			Resource resource = this.resourceLoader.getResource("banner." + ext);
			if (resource.exists()) {
				return new ImageBanner(resource);
			}
		}
		return null;
}
```
上面是寻找自定义图片 banner 文件源码，如果把图片转换成 ASCII 图案继续跟进，追踪方法1中的`PrintedBanner(banner, sourceClass)`方法。最终查找输出图案的主要方法。
```java
// 位置：org.springframework.boot.ImageBanner#printBanner
private void printBanner(BufferedImage image, int margin, boolean invert,
			PrintStream out) {
		AnsiElement background = invert ? AnsiBackground.BLACK : AnsiBackground.DEFAULT;
		out.print(AnsiOutput.encode(AnsiColor.DEFAULT));
		out.print(AnsiOutput.encode(background));
		out.println();
		out.println();
		AnsiColor lastColor = AnsiColor.DEFAULT;
		// 图片高度遍历
		for (int y = 0; y < image.getHeight(); y++) {
			for (int i = 0; i < margin; i++) {
				out.print(" ");
			}
			// 图片宽度遍历
			for (int x = 0; x < image.getWidth(); x++) {
				// 获取每一个像素点
				Color color = new Color(image.getRGB(x, y), false);
				AnsiColor ansiColor = AnsiColors.getClosest(color);
				if (ansiColor != lastColor) {
					out.print(AnsiOutput.encode(ansiColor));
					lastColor = ansiColor;
				}
				// 像素点转换成字符输出，调用方法记为2
				out.print(getAsciiPixel(color, invert));
			}
			out.println();
		}
		out.print(AnsiOutput.encode(AnsiColor.DEFAULT));
		out.print(AnsiOutput.encode(AnsiBackground.DEFAULT));
		out.println();
	}
// 方法2，像素点转换成字符
	private char getAsciiPixel(Color color, boolean dark) {
		// 根据 color 算出一个亮度值
		double luminance = getLuminance(color, dark);
		for (int i = 0; i < PIXEL.length; i++) {
			// 寻找亮度值匹配的字符
			if (luminance >= (LUMINANCE_START - (i * LUMINANCE_INCREMENT))) {
				// PIXEL = { ' ', '.', '*', ':', 'o', '&', '8', '#', '@' };
				return PIXEL[i];
			}
		}
		return PIXEL[PIXEL.length - 1];
	}
```
通过查看源码，发现 Springboot 的图片 banner 的转换和我们预想的大致一致，这么有趣的功能我们能不能自己写一个呢？

# 3.自己实现图片转 ASCII字符
根据上面的分析，总结一下思路，我们也可以手动写一个图片转 ASCII 字符图案。
思路如下：
1. 图片大小缩放，调整到合适大小。
2. 遍历图片像素。
3. 获取图片像素点亮度（RGB颜色通过公式可以得到亮度数值）。
4. 匹配字符。
5. 输出图案。

上面的5个步骤直接使用 Java 代码就可以完整实现，下面是编写的源码。
```java
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * <p>
 * 根据图片生成字符图案
 * 1.图片大小缩放
 * 2.遍历图片像素点
 * 3.获取图片像素点亮度
 * 4.匹配字符
 * 5.输出图案
 *
 * @author  niujinpeng
 * @website www.wdbyte.com
 * @date 2019-02-25 23:03:01
 */
public class GeneratorTextImage {
    private static final char[] PIXEL = {'@', '#', '8', '&', 'o', ':', '*', '.', ' '};
    public static void main(String[] args) throws Exception {
        // 图片缩放
        BufferedImage bufferedImage = makeSmallImage("src/main/resources/banner.jpg");
        // 输出
        printImage(bufferedImage);
    }

    public static void printImage(BufferedImage image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                Color color = new Color(rgb);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                // 一个用于计算RGB像素点亮度的公式
                Double luminace = 0.2126 * red + 0.7152 * green + 0.0722 * blue;
                double index = luminace / (Math.ceil(255 / PIXEL.length) + 0.5);
                System.out.print(PIXEL[(int)(Math.floor(index))]);
            }
            System.out.println();
        }
    }

    public static BufferedImage makeSmallImage(String srcImageName) throws Exception {
        File srcImageFile = new File(srcImageName);
        if (srcImageFile == null) {
            System.out.println("文件不存在");
            return null;
        }
        FileOutputStream fileOutputStream = null;
        BufferedImage tagImage = null;
        Image srcImage = null;
        try {
            srcImage = ImageIO.read(srcImageFile);
            int srcWidth = srcImage.getWidth(null);// 原图片宽度
            int srcHeight = srcImage.getHeight(null);// 原图片高度
            int dstMaxSize = 90;// 目标缩略图的最大宽度/高度，宽度与高度将按比例缩写
            int dstWidth = srcWidth;// 缩略图宽度
            int dstHeight = srcHeight;// 缩略图高度
            float scale = 0;
            // 计算缩略图的宽和高
            if (srcWidth > dstMaxSize) {
                dstWidth = dstMaxSize;
                scale = (float)srcWidth / (float)dstMaxSize;
                dstHeight = Math.round((float)srcHeight / scale);
            }
            srcHeight = dstHeight;
            if (srcHeight > dstMaxSize) {
                dstHeight = dstMaxSize;
                scale = (float)srcHeight / (float)dstMaxSize;
                dstWidth = Math.round((float)dstWidth / scale);
            }
            // 生成缩略图
            tagImage = new BufferedImage(dstWidth, dstHeight, BufferedImage.TYPE_INT_RGB);
            tagImage.getGraphics().drawImage(srcImage, 0, 0, dstWidth, dstHeight, null);
            return tagImage;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                }
                fileOutputStream = null;
            }
            tagImage = null;
            srcImage = null;
            System.gc();
        }
    }
}
```
还是拿上面的 Google log 图片作为实验对象，运行得到字符图案输出。
![图片转 ASCII 字符](https://cdn.jsdelivr.net/gh/niumoo/cdn-assets/2019/ddc9487dd2050f9188825195427ed0a1.png)

文章代码已经上传到 GitHub [Spring Boot](https://github.com/niumoo/springboot/tree/master/)。

### 最后的话

文章有帮助可以点「**赞**」在看或 Star，谢谢你！

文章每周持续更新，本文 [Github.com/niumoo/JavaNotes](https://github.com/niumoo/JavaNotes) 已收录。更有一线大厂面试点，Java程序员所需要掌握的核心知识等文章，也整理了很多我的文字，欢迎 Star 和完善，希望我们一起变得优秀。

要实时关注我更新的文章以及分享的干货，可以关注「 **未读代码** 」公众号。

![公众号](https://camo.githubusercontent.com/a2cbbcea06fb6653b2e0dc25acff3bf0d525a218/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f6e69756d6f6f2f63646e2d6173736574732f776562696e666f2f77656978696e2d7075626c69632e6a7067)