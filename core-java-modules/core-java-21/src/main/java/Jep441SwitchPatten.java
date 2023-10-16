/**
 * @author https://www.wdbyte.com
 * @date 2023/10/13
 */
public class Jep441SwitchPatten {

    public static void main(String[] args) {
        String r1 = formatterPatternSwitch(Integer.valueOf(1));
        String r2 = formatterPatternSwitch(new String("www.wdbyte.com"));
        String r3 = formatterPatternSwitch(Double.valueOf(3.14D));
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
    }

    static String formatterPatternSwitch(Object obj) {
        return switch (obj) {
            case Integer i -> String.format("int %d", i);
            case Long l    -> String.format("long %d", l);
            case Double d  -> String.format("double %f", d);
            case String s  -> String.format("String %s", s);
            default        -> obj.toString();
        };
    }

    static void testFooBarNew(String s) {
        switch (s) {
            case null         -> System.out.println("Oops");
            case "Foo", "Bar" -> System.out.println("Great");
            default           -> System.out.println("Ok");
        }
    }
}
