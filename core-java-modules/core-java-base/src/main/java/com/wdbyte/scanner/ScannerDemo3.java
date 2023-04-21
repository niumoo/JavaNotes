package com.wdbyte.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerDemo3 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("pom.xml");
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
    }
}