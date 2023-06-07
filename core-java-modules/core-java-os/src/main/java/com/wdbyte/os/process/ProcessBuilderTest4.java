package com.wdbyte.os.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 输出日志到指定文件
 * @author https://www.wdbyte.com
 */
public class ProcessBuilderTest4 {
    private static String BASE_DIR = "/Users/darcy/git/JavaNotes/core-java-modules/core-java-os/src/main/java/com/wdbyte/os/process";

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(BASE_DIR));
        processBuilder.command("/bin/bash", "-c", "ls -l");

        File logFile = new File(BASE_DIR + "/process_log.txt");
        // 输出到日志文件
        processBuilder.redirectOutput(logFile);
        // 追加日志到文件
        // processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
        // 是否输出ERROR日志到文件
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        long pid = process.pid();
        int exitCode = process.waitFor();
        System.out.println("pid:" + pid);
        System.out.println("exitCode:" + exitCode);

        // 读取日志
        Files.lines(logFile.toPath()).forEach(System.out::println);
    }
}
