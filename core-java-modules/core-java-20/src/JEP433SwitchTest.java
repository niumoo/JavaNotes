/**
 * @author niulang
 */
public class JEP433SwitchTest {
    public static void main(String[] args) {
        Object obj = 123;
        System.out.println(matchOld(obj)); // 是个数字
        System.out.println(matchNew(obj)); // 是个数字
        obj = "wdbyte.com";
        System.out.println(matchOld(obj)); // 是个字符串，长度大于2
        System.out.println(matchNew(obj)); // 是个字符串，长度大于2
    }

    /**
     * 老代码
     *
     * @param obj
     * @return
     */
    public static String matchOld(Object obj) {
        if (obj == null) {
            return "数据为空";
        }
        if (obj instanceof String) {
            String s = obj.toString();
            if (s.length() > 2) {
                return "是个字符串，长度大于2";
            }
            if (s.length() <= 2) {
                return "是个字符串，长度小于等于2";
            }
        }
        if (obj instanceof Integer) {
            return "是个数字";
        }
        throw new IllegalStateException("未知数据：" + obj);
    }

    /**
     * 新代码
     *
     * @param obj
     * @return
     */
    public static String matchNew(Object obj) {
        String res = switch (obj) {
            case null -> "数据为空";
            case String s when s.length() > 2 -> "是个字符串，长度大于2";
            case String s when s.length() <= 2 -> "是个字符串，长度小于等于于2";
            case Integer i -> "是个数字";
            default -> throw new IllegalStateException("未知数据：" + obj);
        };
        return res;
    }

}
