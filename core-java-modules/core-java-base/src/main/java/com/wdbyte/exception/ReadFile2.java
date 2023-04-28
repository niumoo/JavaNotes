package com.wdbyte.exception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile2 {

    public static void main(String[] args) {
        File file = new File("pom.xml");
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(
            fileReader);) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        }
    }
}
