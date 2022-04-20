package com.wdbyte.junit5;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * @author niulang
 * @date 2022/03/16
 */
public class Calculator {

    public int add(int x, int y) {
        return x + y;
    }
    public static void main(String[] args) throws UnknownHostException {
        InetAddress byName = InetAddress.getByName("www.baidu.com");
        System.out.println(byName.getHostAddress());
        //Java使用的默认字符集
        System.out.println("java 默认字符集：");
        System.out.println(Charset.defaultCharset()+"\n");
        //汉字“测”的字节编码
        String str = "测试一下";
        //这里可以手动设置编码字符集，默认使用utf-8编码
        byte[] bytes = str.getBytes();
        System.out.println("汉字\"测\"的编码：");
        for(byte bt: bytes){
            System.out.println(bt);
        }

    }
}
