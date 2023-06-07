package com.wdbyte.os.process;

import java.io.File;
import java.io.IOException;

/**
 * 运行一个 Java 程序
 *
 * @author https://www.wdbyte.com
 **
 */
public class ProcessBuilderTest7 {
    private static String BASE_DIR = "/Users/darcy/git/JavaNotes/core-java-modules/core-java-os/src/main/java/com/wdbyte/os/process";

    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(BASE_DIR));
        processBuilder.command("java", "ExecDemo.java");
        // 把子线程 I/O 输出重定向当前进程
        processBuilder.inheritIO();
        Process process = processBuilder.start();

        long pid = process.pid();
        int exitCode = process.waitFor();

        System.out.println("pid:" + pid);
        System.out.println("exitCode:" + exitCode);
    }
}
