import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedCollection;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * @author https://www.wdbyte.com
 * @date 2023/10/12
 */
public class Jep431SequencedCollection {
    public static void main(String[] args) {
        //    JDK 21 之后，为所有元素插入有序集合提供了一致的操作 API
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
        System.out.println(list.getFirst());
        System.out.println(deque.getFirst());
        System.out.println(linkedHashSet.getFirst());
        System.out.println(sortedSet.getFirst());
        System.out.println(linkedHashMap.firstEntry());
        System.out.println("-----------------------");

        // 输出最后一个元素
        System.out.println(list.getLast());
        System.out.println(list.getLast());
        System.out.println(deque.getLast());
        System.out.println(sortedSet.getLast());
        System.out.println(linkedHashMap.lastEntry());
        System.out.println("-----------------------");

        // 逆序遍历
        Consumer<SequencedCollection> printFn = s -> {
            // reversed 逆序元素
            s.reversed().forEach(System.out::print);
            System.out.println();
        };
        printFn.accept(list);
        printFn.accept(deque);
        printFn.accept(linkedHashSet);
        printFn.accept(sortedSet);
        // 有序 map 接口是 SequencedMap,上面的 consume类型不适用
        linkedHashMap.reversed().forEach((k, v) -> {
            System.out.print(k);
        });
    }
}
