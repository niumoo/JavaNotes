package com.wdbyte.os.process;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

/**
 * 修改工作目录
 * @author https://www.wdbyte.com
 */
public class ProcessBuilderTest3 {

    private static String BASE_DIR = "/Users/darcy/git/JavaNotes/core-java-modules/core-java-os/src/main/java/com/wdbyte/os/process";

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(BASE_DIR));
        processBuilder.command("/bin/bash", "-c", "pwd");
        Process process = processBuilder.start();

        long pid = process.pid();
        String result = IOUtils.toString(process.getInputStream());
        int exitCode = process.waitFor();

        System.out.println("pid:" + pid);
        System.out.println("result:" + result);
        System.out.println("exitCode:" + exitCode);
    }

}
