package com.wdbyte.comment;

/**
 * 输出一个名称和地域的问候信息。
 * 如：Hello 朋友，welcome to 杭州
 *
 * @author https://www.wdbyte.com
 */
public class WelcomeMain {

    /**
     * 启动应用程序
     *
     * @param args - 应用启动参数
     */
    public static void main(String[] args) {
        System.out.println(getMessage("朋友", "杭州"));
    }

    /**
     * 返回一个欢迎信息。
     *
     * @param name   - 访问者名称
     * @param region - 地域信息
     * @return - 欢迎信息
     */
    public static String getMessage(String name, String region) {
        StringBuilder builder = new StringBuilder();
        builder.append("Hello ");
        builder.append(name);
        builder.append(", Welcome to ");
        builder.append(region);
        builder.append(" !!");
        return builder.toString();
    }


    /**
     * 计算两数之和
     * @param x  数字1
     * @param y  数字2
     * @return
     */
    public int add(int x, int y) {
        /*
         *  计算两数之和
         */
        int s = x + y;
        return s;
    }
}