package com.wdbyte.os.process;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author https://www.wdbyte.com
 */
public class ProcessBuilderTest10 {
    private static String BASE_DIR = "/Users/darcy/git/JavaNotes/core-java-modules/core-java-os/src/main/java/com/wdbyte/os/process";

    public static void main(String[] args) throws InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(BASE_DIR));
        processBuilder.command("java", "ExecDemo.java");
        // 把子线程 I/O 输出重定向当前进程
        processBuilder.inheritIO();

        // 创建 CompletableFuture 对象
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                // 命令执行
                Process process = processBuilder.start();
                // 任务超时时间
                process.waitFor();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        });

        // 注册回调函数，处理异步等待的结果
        future.thenAccept(result -> {
            System.out.println("进程执行结束");
        });
        System.out.println("主进程等待");
        Thread.sleep(20 * 1000);
    }
}
