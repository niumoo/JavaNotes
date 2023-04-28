package com.wdbyte.exception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author niulang
 * @date 2023/04/27
 */
public class JavaException6 {

    public static void main(String[] args) {
        try {
            Files.readAllLines(Paths.get("c:/abc.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
