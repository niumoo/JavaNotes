package com.wdbyte.io.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * @author niulang
 * @date 2023/11/08
 */
public class FileReadDemo {

    public static void main(String[] args) {
        Path path = Paths.get("/Users/darcy/wdbyte");
        System.out.println(Files.isDirectory(path));
        System.out.println(path.toFile().isFile());
        System.out.println(path.toFile().isDirectory());
    }

    /**
     * java 8
     *
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        Path path = Paths.get("/Users/darcy/wdbyte/log.txt");
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String line : lines) {
            System.out.println(line);
        }
    }

    /**
     * java 8
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        Path path = Paths.get("/Users/darcy/wdbyte/log.txt");
        byte[] bytes = Files.readAllBytes(path);
        String content = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(content);
    }

    /**
     * java 8
     *
     * @throws IOException
     */
    @Test
    public void test3() throws IOException, InterruptedException {
        Path path = Paths.get("/Users/darcy/wdbyte/log.txt");
        try (Stream<String> stream = Files.lines(path);) {
            stream.forEach(System.out::println);
        }
        try (Stream<String> stream = Files.lines(path);) {
            stream.parallel().forEachOrdered(System.out::println);
        }
    }

    /**
     * java 11
     * 要求：文件小于2G
     *
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        Path path = Paths.get("/Users/darcy/wdbyte/log.txt");
        String content = Files.readString(path, StandardCharsets.UTF_8);
        System.out.println(content);
    }

    @Test
    public void test5() {
        File file = new File("/Users/darcy/wdbyte/log.txt");
        String line;
        // 默认读取缓冲区大小 8K
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 定义缓冲区大小为 128 K
        int buffer = 128 * 1024;
        try (BufferedReader br = new BufferedReader(new FileReader(file), buffer)) {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * java 8
     *
     * @throws IOException
     */
    @Test
    public void test6() throws IOException {
        Path path = Paths.get("/Users/darcy/wdbyte/log.txt");
        String line;
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);) {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    /**
     * 自古以来
     */
    @Test
    public void test7() {
        File file = new File("/Users/darcy/wdbyte/log.txt");
        try (Scanner sc = new Scanner(new FileReader(file))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 自古以来
     */
    @Test
    public void test8() {
        File file = new File("/Users/darcy/wdbyte/log.txt");
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);) {

            StringBuilder content = new StringBuilder();
            int data;
            while ((data = bis.read()) != -1) {
                content.append((char)data);
            }
            System.out.println(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
