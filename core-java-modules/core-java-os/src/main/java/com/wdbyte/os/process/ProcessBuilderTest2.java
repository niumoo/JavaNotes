package com.wdbyte.os.process;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * 修改环境变量
 *
 * @author https://www.wdbyte.com
 */
public class ProcessBuilderTest2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> environment = processBuilder.environment();
        environment.forEach((k, v) -> System.out.println(k + ":" + v));
        System.out.println("--------------");
        processBuilder.environment().put("my_website", "www.wdbyte.com");
        processBuilder.command("/bin/bash", "-c", "echo $my_website");

        Process process = processBuilder.start();
        long pid = process.pid();
        String result = IOUtils.toString(process.getInputStream());
        int exitCode = process.waitFor();

        System.out.println("pid:" + pid);
        System.out.println("result:" + result);
        System.out.println("exitCode:" + exitCode);
    }
}
