package com.wdbyte.io.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
 * @website https://www.wdbyte.com
 */
public class GeneratorTextImageMini {

    public static void main(String[] args) throws Exception {
        //BufferedImage image = resizeImage("/Users/darcy/Downloads/sanli.jpg", 120);
        BufferedImage image = ImageIO.read(new File("/Users/darcy/Downloads/刘看山.jpg"));
        printImage(image);
    }

    /**
     * 图片缩放
     *
     * @param srcImagePath  图片路径
     * @param targetWidth   目标宽度
     * @return
     * @throws IOException
     */
    public static BufferedImage resizeImage(String srcImagePath, int targetWidth) throws IOException {
        Image srcImage = ImageIO.read(new File(srcImagePath));
        int targetHeight = getTargetHeight(targetWidth, srcImage);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(srcImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    /**
     * 根据指定宽度，计算等比例高度
     *
     * @param targetWidth   目标宽度
     * @param srcImage      图片信息
     * @return
     */
    private static int getTargetHeight(int targetWidth, Image srcImage) {
        int targetHeight = srcImage.getHeight(null);
        if (targetWidth < srcImage.getWidth(null)) {
            targetHeight = Math.round((float)targetHeight / ((float)srcImage.getWidth(null) / (float)targetWidth));
        }
        return targetHeight;
    }

    /**
     * 图片打印
     *
     * @param image
     * @throws IOException
     */
    public static void printImage(BufferedImage image) throws IOException {
        final char[] PIXEL_CHAR_ARRAY = {'W', '@', '#', '8', '&', '*', 'o', ':', '.', ' '};
        //final char[] PIXEL_CHAR_ARRAY = {'，', '.', ' '};
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                Color color = new Color(rgb);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                // 一个用于计算RGB像素点灰度的公式
                Double grayscale = 0.2126 * red + 0.7152 * green + 0.0722 * blue;
                double index = grayscale / (Math.ceil(255 / PIXEL_CHAR_ARRAY.length) + 0.5);
                System.out.print(PIXEL_CHAR_ARRAY[(int)(Math.floor(index))]);
            }
            System.out.println();
        }
    }

}
