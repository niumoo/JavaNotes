package com.wdbyte.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

/**
 * @author www.wdbyte.com
 * @date 2023/11/01
 */
public class CompletableFutureTest {

    /**
     * 异步执行程序后，对正常响应和异常响应进行处理
     */
    @Test
    public void completableFutureTest1() {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            sleep(2000);
            System.out.println("do.....");
            return 1;
        });

        completableFuture1.thenAccept(res -> {
            System.out.println("收到结果：" + res
            );
        });

        System.out.println("等待");
        sleep(10 * 1000);
    }
    @Test
    public void completableFutureTest2() {
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            sleep(2000);
            System.out.println("do2.....");
            return 10 / 0;
        });
        completableFuture2.exceptionally(except -> {
            System.out.println("发生异常：" + except.getMessage());
            return 0;
        });

        System.out.println("等待");
        sleep(10 * 1000);
    }

    @Test
    public void completableFutureTest3() {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            sleep(2000);
            System.out.println("do.....");
            return 1;
        });

        completableFuture1.thenAccept(res -> {
            System.out.println("收到结果：" + res
            );
        });

        System.out.println("等待");
        sleep(10 * 1000);
    }

    void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
