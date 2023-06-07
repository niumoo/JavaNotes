package com.wdbyte.os.process;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 运行一个 Java 程序
 * 等待一定时间后检查状态，未结束则直接杀死进程。
 *
 * @author https://www.wdbyte.com
 */
public class ProcessBuilderTest9 {
    private static String BASE_DIR = "/Users/darcy/git/JavaNotes/core-java-modules/core-java-os/src/main/java/com/wdbyte/os/process";

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(BASE_DIR));
        processBuilder.command("java", "ExecDemo.java");
        // 把子线程 I/O 输出重定向当前进程
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        // 等待一定时间
        boolean waitFor = process.waitFor(3, TimeUnit.SECONDS);
        System.out.println("waitFor:" + waitFor);
        // 若未退出，杀死子进程
        if (!waitFor) {
            process.destroyForcibly();
            process.waitFor();
            System.out.println("杀死进程:" + process);
        }

    }
}
