import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

/**
 *  JDK 21 之前，顺序集合中操作体验不一致
 * @author niulang
 * @date 2023/10/12ø
 */
public class JEP431Test {
    public static void main(String[] args) {
        //    JDK 21 之前，顺序集合中操作体验不一致
        List<Integer> listTemp = List.of(1, 2, 3, 4, 5);

        ArrayList<Integer> list = new ArrayList(listTemp);
        Deque<Integer> deque = new ArrayDeque<>(listTemp);
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>(listTemp);
        TreeSet<Integer> sortedSet = new TreeSet<>(listTemp);
        LinkedHashMap<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            linkedHashMap.put(i, i);
        }

        // 输出第一个元素
        System.out.println(list.get(0));
        System.out.println(deque.getFirst());
        System.out.println(linkedHashSet.iterator().next());
        System.out.println(sortedSet.first());
        //System.out.println(linkedHashMap.firstEntry());没办法
        System.out.println("-----------------------");

        // 输出最后一个元素
        System.out.println(list.get(list.size()-1));
        System.out.println(deque.getLast());
        //System.out.println(linkedHashSet()); 没办法，只能遍历
        System.out.println(sortedSet.last());
        //System.out.println(linkedHashMap); 没办法
        System.out.println("-----------------------");

        // 逆序遍历
        for (var it = list.listIterator(list.size()); it.hasPrevious();) {
            var e = it.previous();
            System.out.print(e);
        }
        System.out.println();
        for (var it = deque.descendingIterator(); it.hasNext();) {
            var e = it.next();
            System.out.print(e);
        }
        System.out.println();
        for (Integer i : sortedSet.descendingSet()) {
            System.out.print(i);
        }
        System.out.println();

        // sortedSet linkedHashMap 逆序输出很难操作

    }
}
