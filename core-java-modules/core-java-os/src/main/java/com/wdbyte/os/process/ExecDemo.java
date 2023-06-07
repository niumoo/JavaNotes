package com.wdbyte.os.process;

import java.io.IOException;

/**
 * @author https://www.wdbyte.com
 */
public class ExecDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("开始处理数据...");
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println(i);
        }
        System.out.println("数据处理完毕");
    }
}
