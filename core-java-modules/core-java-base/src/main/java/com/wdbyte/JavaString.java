package com.wdbyte;

/**
 * @author niulang
 * @date 2023/03/22
 */
public class JavaString {
    public static void main(String[] args) {
        //String str1 = "Hello world";
        //String str2 = "Hello world";
        //String str3 = new String("Hello world");
        //String str4 = new String("Hello world");
        //
        //System.out.println(str1 == str2);
        //System.out.println(str3 == str4);
        //System.out.println(str1 == str4);
        // 比较
        //String str1 = "abc";
        //String str2 = "abz";
        //System.out.println(str1.compareTo(str2));
        //System.out.println(str2.compareTo(str1));
        //// 输出：-23
        //// 输出：23
        //System.out.println("19999".compareTo("2"));
        //// 输出：-1

        //
        ////    CharSequence
        //String str = "Hello, world!";
        //str.chars().forEach(System.out::println);
        //str.chars().mapToObj(c -> (char) c).forEach(System.out::print);

        //String str1 = "Hello";
        //String str2 = "world";
        //String str3 = str1 + str2;
        //String str4 = str1 + str2;
        //String str5 = "Hello" + "world";
        //String str6 = "Hello" + "world";
        //System.out.println(str3 == str4);
        //System.out.println(str5 == str6);

        //String str1 = new StringBuilder("计算机").append("软件").toString();
        //System.out.println(str1.intern() == str1);
        //String str2 = new StringBuilder("ja").append("va").toString();
        //System.out.println(str2.intern() == str2);
        //
        //String b = "12";
        //String d = new StringBuilder("计算机").append("软件").toString();
        //String c = new StringBuilder("计算机").append("软件").toString();
        //String a = "计算机软件";
        //System.out.println(a == d);
        //System.out.println(a == c);
        String str0 = "abc";
        String str1 = new String("abc");
        String str3 = "abc";
        System.out.println(str1 == str0);
        System.out.println(str3 == str0);
        str1.intern();
        String str2 = new String("Hello,world");
        System.out.println(str1 == str2);
    }

    public void test() {
        String str1 = "Hello, world!";
        String str2 = "Hello, world!";
        System.out.println(str1 == str2);
    }
}
