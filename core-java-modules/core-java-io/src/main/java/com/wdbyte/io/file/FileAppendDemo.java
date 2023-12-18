package com.wdbyte.io.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.FileWriteMode;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

/**
 * @author niulang
 * @date 2023/12/12
 */
public class FileAppendDemo {

    @Test
    public void appendLine1() throws IOException {
        Path path = Paths.get("/Users/darcy/wdbyte/test1.txt");
        // 写入一行数据
        Files.write(path, "line append1".getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        // 写入一个换行符
        Files.write(path, System.lineSeparator().getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.APPEND);
    }

    @Test
    public void appendLine2() throws IOException {
        String path = "/Users/darcy/wdbyte/test1.txt";
        try (FileWriter fileWriter = new FileWriter(path, true)) {
            fileWriter.write("line append2");
            fileWriter.write(System.lineSeparator());
        }
    }
    @Test
    public void appendLine3() throws IOException {
        String path = "/Users/darcy/wdbyte/test1.txt";
        try (FileWriter fileWriter = new FileWriter(path, true)) {
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write("line append3");
            bw.newLine();
        }
    }

    @Test
    public void appendLine4() throws IOException {
        String path = "/Users/darcy/wdbyte/test1.txt";
        try (FileOutputStream fileOutputStream = new FileOutputStream(path, true)) {
            fileOutputStream.write("line append4".getBytes(StandardCharsets.UTF_8));
            fileOutputStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * java 11 Files.writeString
     * @throws IOException
     */
    public void appendLineJava11() throws IOException {
        Path path = Paths.get("/Users/darcy/wdbyte/test1.txt");
        Files.writeString(path, "line appendLineJava11",
                          StandardOpenOption.CREATE,
                          StandardOpenOption.APPEND);
    }


    /**
     * Java 8
     *
     * @throws IOException
     */
    @Test
    public void appendLineJava8() throws IOException {
        Path path = Paths.get("/Users/darcy/wdbyte/test1.txt");
        List<String> dataList = new ArrayList<>();
        dataList.add("line1 appendLineJava8");
        dataList.add("line2 appendLineJava8");
        dataList.add("line3 appendLineJava8");

        Files.write(path, dataList,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);

        Files.write(path, System.lineSeparator().getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
    }

    @Test
    public void appendLineList() throws IOException {
        String path = "/Users/darcy/wdbyte/test1.txt";
        List<String> dataList = new ArrayList<>();
        dataList.add("line1 appendLineList");
        dataList.add("line2 appendLineList");
        dataList.add("line3 appendLineList");

        try (FileWriter fileWriter = new FileWriter(path, true)) {
            BufferedWriter bw = new BufferedWriter(fileWriter);
            for (String data : dataList) {
                bw.write(data);
                bw.newLine();
            }
        }
    }

    @Test
    public void appendByCommonsIo() throws IOException {
        File file = new File("/Users/darcy/wdbyte/test1.txt");
        String content = "hello commons io,appendByCommonsIo";
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, true);
        FileUtils.writeStringToFile(file, System.lineSeparator(), StandardCharsets.UTF_8, true);
    }

    @Test
    public void appendListByCommonsIo() throws IOException {
        File file = new File("/Users/darcy/wdbyte/test1.txt");
        List<String> dataList = new ArrayList<>();
        dataList.add("line1 hello commons io,appendListByCommonsIo");
        dataList.add("line2 hello commons io,appendListByCommonsIo");
        dataList.add("line3 hello commons io,appendListByCommonsIo");
        FileUtils.writeLines(file, dataList, true);
    }

    @Test
    public void appendData(){
        // 创建File对象指向要追加内容的文件
        File file = new File("/Users/darcy/wdbyte/test1.txt");
        // 要追加的内容
        String contentToAppend = "Hello, Guava!";
        try {
            // 使用Guava的Files类以追加模式写入内容
            com.google.common.io.Files.asCharSink(file, Charsets.UTF_8, FileWriteMode.APPEND)
                .write(contentToAppend);

            // 如果需要追加新行，可以使用下面的代码
            com.google.common.io.Files.asCharSink(file, Charsets.UTF_8, com.google.common.io.FileWriteMode.APPEND)
                .write(contentToAppend + System.lineSeparator());
            System.out.println("Content appended successfully.");
        } catch (IOException e) {
            System.err.println("Error appending content to file: " + e.getMessage());
        }
    }

    @Test
    public void appendDataListByGuava(){
        // 创建File对象指向要追加内容的文件
        File file = new File("/Users/darcy/wdbyte/test1.txt");

        // 要追加的内容
        List<String> dataList = new ArrayList<>();
        dataList.add("line1 hello commons io,appendDataListByGuava");
        dataList.add("line2 hello commons io,appendDataListByGuava");
        dataList.add("line3 hello commons io,appendDataListByGuava");

        try {
            // 使用Guava的Files类以追加模式写入内容
            com.google.common.io.Files.asCharSink(file, Charsets.UTF_8, FileWriteMode.APPEND)
                .writeLines(dataList);

            System.out.println("Content appended successfully.");
        } catch (IOException e) {
            System.err.println("Error appending content to file: " + e.getMessage());
        }
    }




}
