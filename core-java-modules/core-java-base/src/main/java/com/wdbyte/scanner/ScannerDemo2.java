package com.wdbyte.scanner;

import java.util.Scanner;

public class ScannerDemo2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个整数：");
        int inputInt = scanner.nextInt();
        System.out.println("你输入的整数是：" + inputInt);

        System.out.println("请输入一个浮点数：");
        double inputDouble = scanner.nextDouble();
        System.out.println("你输入的浮点数是：" + inputDouble);
    }
}