package com.wdbyte.os.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 输出日志到指定文件
 *
 * @author https://www.wdbyte.com
 **
 */
public class ProcessBuilderTest5 {
    private static String BASE_DIR = "/Users/darcy/git/JavaNotes/core-java-modules/core-java-os/src/main/java/com/wdbyte/os/process";

    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(BASE_DIR));
        // 执行命令 xxx，命令不存在，会报 ERROR 日志
        processBuilder.command("/bin/bash", "-c", "xxx");

        File infoLogFile = new File(BASE_DIR + "/process_log_info.txt");
        File errorLogFile = new File(BASE_DIR + "/process_log_error.txt");
        // 日志输出到文件
        processBuilder.redirectOutput(infoLogFile);
        processBuilder.redirectError(errorLogFile);
        Process process = processBuilder.start();

        long pid = process.pid();
        int exitCode = process.waitFor();

        System.out.println("pid:" + pid);
        System.out.println("exitCode:" + exitCode);

        // 读取 ERROR 日志
        Files.lines(errorLogFile.toPath()).forEach(System.out::println);
    }

}
