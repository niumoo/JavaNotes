package com.wdbyte;

/**
 * @author https://www.wdbyte.com
 * @date 2023/03/23
 */
public class JavaDataType {

    public static void main(String[] args) {
        boolean result = true;
        char capitalC = 'C';
        byte b = 100;
        short s = 10000;
        int i = 100000;

        // 10 进制的 26
        int decVal = 26;
        // 16 进制的 26
        int hexVal = 0x1a;
        // 2 进制的 26
        int binVal = 0b11010;
        System.out.println(decVal);
        System.out.println(hexVal);
        System.out.println(binVal);

        long creditCardNumber = 1234_5678_9012_3456L;
        long socialSecurityNumber = 999_99_9999L;
        float pi =  3.14_15F;
        long hexBytes = 0xFF_EC_DE_5E;
        long hexWords = 0xCAFE_BABE;
        long maxLong = 0x7fff_ffff_ffff_ffffL;
        byte nybbles = 0b0010_0101;
        long bytes = 0b11010010_01101001_10010100_10010010;

    }
}
