import java.util.List;

/**
 * @author https://www.wdbyte.com
 * @date 2023/10/15
 */
public class Jep443Unname {

    public static void main(String[] args) {
        String _ = "123213";
        Integer _ = 123;
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        int count = 0;
        for (Integer _ : list) {
            count++;
        }
        System.out.println(count);
    }
}
