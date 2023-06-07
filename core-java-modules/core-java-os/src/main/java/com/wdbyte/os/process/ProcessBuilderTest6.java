package com.wdbyte.os.process;

import java.io.File;
import java.io.IOException;

/**
 * 子线程 I/O 重定向到当前线程
 * @author https://www.wdbyte.com
 */
public class ProcessBuilderTest6 {
    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File("./"));
        processBuilder.command("/bin/bash", "-c", "ls -l");
        // 把子线程 I/O 输出重定向当前进程
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        System.out.println("exitCode:" + exitCode);
    }

}
