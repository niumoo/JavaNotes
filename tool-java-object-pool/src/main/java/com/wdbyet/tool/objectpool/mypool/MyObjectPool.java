package com.wdbyet.tool.objectpool.mypool;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Stack;

/**
 * @author https://www.wdbyte.com
 */
public class MyObjectPool<T extends Closeable> {

    // 池子大小
    private Integer size = 5;
    // 对象池栈。后进先出
    private Stack<T> stackPool = new Stack<>();
    // 借出的对象的 hashCode 集合
    private HashSet<Integer> borrowHashCodeSet = new HashSet<>();

    /**
     * 增加一个对象
     *
     * @param t
     */
    public synchronized void addObj(T t) {
        if ((stackPool.size() + borrowHashCodeSet.size()) == size) {
            throw new RuntimeException("池中对象已经达到最大值");
        }
        stackPool.add(t);
        System.out.println("添加了对象:" + t.hashCode());
    }

    /**
     * 借出一个对象
     *
     * @return
     */
    public synchronized T borrowObj() {
        if (stackPool.isEmpty()) {
            System.out.println("没有可以被借出的对象");
            return null;
        }
        T pop = stackPool.pop();
        borrowHashCodeSet.add(pop.hashCode());
        System.out.println("借出了对象:" + pop.hashCode());
        return pop;
    }

    /**
     * 归还一个对象
     *
     * @param t
     */
    public synchronized void returnObj(T t) {
        if (borrowHashCodeSet.contains(t.hashCode())) {
            stackPool.add(t);
            borrowHashCodeSet.remove(t.hashCode());
            System.out.println("归还了对象:" + t.hashCode());
            return;
        }
        throw new RuntimeException("只能归还从池中借出的对象");
    }

    /**
     * 销毁池中对象
     */
    public synchronized void destory() {
        if (!borrowHashCodeSet.isEmpty()) {
            throw new RuntimeException("尚有未归还的对象，不能关闭所有对象");
        }
        while (!stackPool.isEmpty()) {
            T pop = stackPool.pop();
            try {
                pop.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("已经销毁了所有对象");
    }
}
