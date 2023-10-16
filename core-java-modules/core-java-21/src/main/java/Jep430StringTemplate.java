import java.time.LocalDate;
import java.util.FormatProcessor;
import java.util.Locale;

/**
 * 字符串模版
 *
 * @author https://www.wdbyte.com
 * @date 2023/10/13
 */
public class Jep430StringTemplate {

    public static void main(String[] args) {
        int x = 20;
        int y = 3;
        String s = x + " + " + y + " = " + (x + y);
        System.out.println(s);

        String sb = new StringBuilder().append(x).append(" + ").append(y).append(" = ").append(x + y).toString();
        System.out.println(sb);


        String sFormat = String.format("%d + %d = %d", x, y, x + y);
        System.out.println(sFormat);

        System.out.println("--------------------");

        // JDK 21 使用字符串模版 STR 进行插值
        String sTemplate = StringTemplate.STR."\{x} + \{y} = \{x+y}";
        System.out.println(sTemplate);

        // 字符串模版也可以先定义模版，再处理插值
        StringTemplate st = StringTemplate.RAW."\{x} + \{y} = \{x+y}";
        String sTemplate2 = StringTemplate.STR.process(st);
        System.out.println(sTemplate2);

        System.out.println("--------------------");
        LocalDate now = LocalDate.now();
        String nowStr = StringTemplate.STR."现在是 \{now.getYear()} 年 \{now.getMonthValue()} 月 \{now.getDayOfMonth()} 号";
        System.out.println(nowStr);

        // 字符串模版读取数组，字符串模版也可以嵌套
        String[] infoArr = { "Hello", "Java 21", "https://www.wdbyte.com" };
        String sArray = StringTemplate.STR."\{infoArr[0]}, \{STR."\{infoArr[1]}, \{infoArr[2]}"}";
        System.out.println(sArray);

        // 字符串模版也可以结合多行文本
        String name = "https://www.wdbyte.com";
        String address = "程序猿阿朗";
        String json = StringTemplate.STR."""
            {
                "name":    "\{name}",
                "address": "\{address}"
            }
            """;
        System.out.println(json);
        System.out.println("--------------------");

    }
}


record Rectangle(String name, double width, double height) {
    double area() {
        return width * height;
    }
}
