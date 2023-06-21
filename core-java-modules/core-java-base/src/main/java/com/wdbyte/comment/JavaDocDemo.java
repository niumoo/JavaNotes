package com.wdbyte.comment;

/**
 * 输出一个名称和地域的问候信息。
 * 如：Hello 朋友，welcome to 杭州
 * 主要实现方法是 {@link JavaDocDemo#getMessage(String, String)}
 *
 * @author wdbyte
 * @version 1.0
 * @see com.wdbyte.comment.JavaDocDemo#getMessage(String, String) 
 * @since 1.0
 */
public class JavaDocDemo {

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
     * @see <a href="https://docs.oracle.com/en/java/">Java Dcoumentation</a>
     * @param name   - 访问者名称
     * @param region - 地域信息
     * @return - 欢迎信息语句
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

}