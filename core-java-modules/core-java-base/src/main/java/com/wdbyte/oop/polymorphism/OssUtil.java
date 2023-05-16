package com.wdbyte.oop.polymorphism;

/**
 * @author https://www.wdbyte.com
 * @date 2023/04/18
 */
public class OssUtil {
    /**
     * OSS 上传工具类
     *
     * @param oss
     * @param content
     */
    public static void upload(Oss oss, String content) {
        long start = System.currentTimeMillis();
        oss.upload(content);
        long end = System.currentTimeMillis();
        System.out.println("上传耗时:" + (end - start) + "ms");
    }

    public static void main(String[] args) {
        AliyunOss aliyunOss = new AliyunOss();
        TencentOss tencentOss = new TencentOss();
        upload(aliyunOss, "Hello aliyun");
        System.out.println("------------");
        upload(tencentOss, "Hello tencent");
    }
}
