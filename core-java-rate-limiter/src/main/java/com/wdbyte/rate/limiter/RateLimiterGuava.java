package com.wdbyte.rate.limiter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author https://www.wdbyte.com
 * @date 2022/02/25
 */
public class RateLimiterGuava {

    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = RateLimiter.create(2);
        for (int i = 0; i < 10; i++) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
            System.out.println(time + ":" + rateLimiter.tryAcquire(i+1));
            Thread.sleep(250);
        }
    }
}
