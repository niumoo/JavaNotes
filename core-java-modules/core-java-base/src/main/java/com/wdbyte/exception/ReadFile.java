package com.wdbyte.exception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

    public static void main(String[] args) {

        File file = new File("pom2.xml");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        } finally {
            try {
                System.out.println("开始关闭资源");
                if (bufferedReader != null) {
                    bufferedReader.close();
                    System.out.println("关闭 bufferedReader");
                }
                if (fileReader != null) {
                    fileReader.close();
                    System.out.println("关闭 fileReader");
                }
            } catch (IOException e) {
                System.out.println("关闭文件失败");
            }
        }
    }
}
