package com.wdbyte.rate.limiter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口限流的简单实现
 * 需要开线程
 *
 * @author https://www.wdbyte.com
 * @date 2022/02/23
 */
public class RateLimiterSimpleWindow0 {

    // 阈值
    private static Integer qps = 2;
    // 计数器
    private static AtomicInteger reqCount = new AtomicInteger();

    static {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reqCount.getAndSet(0);
            }
        }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            if (reqCount.getAndAdd(1) > qps) {
                System.out.println("被限流");
            } else {
                reqCount.incrementAndGet();
                System.out.println("做点什么");
            }
        }
    }
}
