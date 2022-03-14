package com.wdbyte.rate.limiter;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口限流实现（不需要开单独线程）
 *
 * @author https://www.wdbyte.com
 * @date 2022/02/23
 */
public class RateLimiterSimpleWindow {

    // 阈值
    private static Integer QPS = 2;
    // 时间窗口（毫秒）
    private static long TIME_WINDOWS = 1000;
    // 计数器
    private static AtomicInteger REQ_COUNT = new AtomicInteger();

    private static long START_TIME = System.currentTimeMillis();

    public synchronized static boolean tryAcquire() {
        long now = System.currentTimeMillis();
        if ((now - START_TIME) > TIME_WINDOWS) {
            START_TIME = now;
            REQ_COUNT.set(1);
            return true;
        }
        return REQ_COUNT.incrementAndGet() <= QPS;
    }

    public static void main(String[] args) throws InterruptedException {
        //Thread.sleep(400);
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            LocalTime now = LocalTime.now();
            if (!tryAcquire()) {
                System.out.println(now + " 被限流");
            } else {
                System.out.println(now + " 做点什么");
            }
        }
    }
}
