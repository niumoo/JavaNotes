package com.wdbyte.oop.polymorphism;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/18
 */
public class TencentOss implements Oss {
    @Override
    public void upload(String content) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("文件已经上传到腾讯云 OSS，内容：" + content);
    }
}
