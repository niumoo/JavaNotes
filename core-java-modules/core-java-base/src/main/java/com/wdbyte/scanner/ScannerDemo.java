package com.wdbyte.scanner;

import java.util.Scanner;

public class ScannerDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter(System.lineSeparator());
        System.out.println("请输入一个字符串：");
        String inputString = scanner.next();
        System.out.println("你输入的字符串是：" + inputString);
    }
}