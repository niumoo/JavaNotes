package com.wdbyte.os.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

/**
 * Process 输出Java 版本号
 * @author https://www.wdbyte.com
 */
public class ProcessBuilderTest1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 构建执行命令
        ProcessBuilder processBuilder = new ProcessBuilder("java","-version");
        // 重定向 ERROR 流(有些 JDK 版本 Java 命令通过 ERROR 流输出)
        processBuilder.redirectErrorStream(true);
        // 运行命令 java -version
        Process process = processBuilder.start();
        // 过去PID
        long pid = process.pid();
        // 一次性获取运行结果
        //String result = convertInputStreamToString(process.getInputStream());
        String result = IOUtils.toString(process.getInputStream());
        // 等到运行结束
        int exitCode = process.waitFor();

        System.out.println("pid:" + pid);
        System.out.println("result:" + result);
        System.out.println("exitCode:" + exitCode);
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            inputStream.close();
        }
        return sb.toString();
    }

}
