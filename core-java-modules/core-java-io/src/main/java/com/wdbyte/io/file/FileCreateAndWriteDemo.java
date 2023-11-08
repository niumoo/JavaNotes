package com.wdbyte.io.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author niulang
 * @date 2023/11/06
 */
public class FileCreateAndWriteDemo {

    @Test
    public void test0() throws IOException {
        String content = "www.wdbyte.com,java 7";
        String filePath = "/Users/darcy/wdbyte/test0.txt";
        try (FileWriter fw = new FileWriter(filePath);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
            bw.newLine();
        }

        //  追加模式
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
            bw.newLine();
        }
    }

    /**
     * JDK 7
     *
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        String content = "www.wdbyte.com, java 7";
        Path path = Paths.get("/Users/darcy/wdbyte/test1.txt");
        // string -> bytes
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JDK 7 追加
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        String content = "www.wdbyte.com,java 7" + System.lineSeparator();
        Path path = Paths.get("/Users/darcy/wdbyte/test2.txt");
        Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
        Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
    }

    /**
     * JDK 7 写入 List
     *
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        List<String> dataList = Arrays.asList("www", "wdbyte", "com", "java 7");
        Path path = Paths.get("/Users/darcy/wdbyte/test3.txt");
        Files.write(path, dataList);
    }

    @Test
    public void test4() throws IOException {
        String content = "www.wdbyte.com,java 7";
        Path path = Paths.get("/Users/darcy/wdbyte/test4.txt");
        // 默认 utf_8
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write(content);
            bw.newLine();
        }
        //  追加模式
        try (BufferedWriter bw = Files.newBufferedWriter(path,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(content);
            bw.newLine();
        }
    }

    /**
     * JDK 11
     *
     * @throws IOException
     */
    @Test
    public void test5() throws IOException {
        Path path = Paths.get("/Users/darcy/wdbyte/test5.txt");
        Files.writeString(path, "www.wdbyte.com,java 11");
    }


}
