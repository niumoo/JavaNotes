package com.wdbyte.string;

/**
 * @author https://www.wdbyte.com
 * @date 2023/03/30
 */
public class JavaStringBuilder {

    public static void main(String[] args) {
        StringBuilder sb1 = new StringBuilder();
        System.out.println(sb1.capacity()); // 容量：16
        StringBuilder sb2 = new StringBuilder("wdbyte.com");
        System.out.println(sb2.capacity()); // 容量：26

        StringBuilder sb3 = new StringBuilder("www");
        sb3.append(".wdbyte.com");
        System.out.println(sb3.toString()); //

        StringBuilder sb4 = new StringBuilder("wdbyte");
        sb4.insert(0,"www.");
        System.out.println(sb4.toString()); // www.wdbyte
        sb4.insert(10,".com");
        System.out.println(sb4.toString()); // www.wdbyte.com

        StringBuilder sb5 = new StringBuilder("www.wdbyte.com");
        sb5.delete(0,4);
        System.out.println(sb5); // wdbyte.com

        StringBuilder sb6 = new StringBuilder("hello world!");
        sb6.replace(6,11, "java"); // 结果为 "hello java!"
        System.out.println(sb6);

        StringBuilder sb7 = new StringBuilder("hello world!");
        sb7.reverse();
        System.out.println(sb7);

    }
}
