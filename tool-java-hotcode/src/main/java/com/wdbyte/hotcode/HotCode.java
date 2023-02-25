package com.wdbyte.hotcode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author niulang
 * @date 2023/02/20
 */
public class HotCode {

    private static HashSet hashSet = new HashSet();
    /**
     * 线程池，大小1
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        // 模拟 CPU 过高
        cpuHigh();
        // 不断新增 BigDecimal 信息到 list
        allocate();
        // 模拟线程死锁
        deadThread();
        // 不断的向 hashSet 集合增加数据，内存缓慢增长
        addHashSetThread();
        // 模拟线程阻塞，线程池容量为1，塞入两个线程，会有一个一直等待
        thread();
    }

    /**
     * 消耗CPU的线程
     * 不断循环进行浮点运算
     */
    private static void cpuHigh() {
        Thread thread = new Thread(() -> {
            Thread.currentThread().setName("cpu_high_thread");
            while (true){
                double pi = 0;
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    pi += Math.pow(-1, i) / (2 * i + 1);
                }
                System.out.println("Pi: " + pi * 4);
            }
        });
        thread.start();
    }

    /**
     * 不断新增 BigDecimal 信息到 list
     */
    private static void allocate() {
        new Thread(()->{
            Thread.currentThread().setName("memory_allocate_thread");
            List<BigDecimal> list = new ArrayList<>();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                list.add(new BigDecimal(i));
            }
        }).start();
    }

    /**
     * 死锁线程
     * 线程 dead_thread_A 与 线程 dead_thread_B 互相锁死
     */
    private static void deadThread() {
        /** 创建资源 */
        Object resourceA = new Object();
        Object resourceB = new Object();
        // 创建线程
        Thread threadA = new Thread(() -> {
            Thread.currentThread().setName("dead_thread_A");
            synchronized (resourceA) {
                System.out.println(Thread.currentThread() + " get ResourceA");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resourceB");
                synchronized (resourceB) {
                    System.out.println(Thread.currentThread() + " get resourceB");
                }
            }
        });

        Thread threadB = new Thread(() -> {
            Thread.currentThread().setName("dead_thread_A");
            synchronized (resourceB) {
                System.out.println(Thread.currentThread() + " get ResourceB");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resourceA");
                synchronized (resourceA) {
                    System.out.println(Thread.currentThread() + " get resourceA");
                }
            }
        });
        threadA.start();
        threadB.start();
    }


    /**
     * 不断的向 hashSet 集合添加数据，每秒100个字符串
     */
    public static void addHashSetThread() {
        // 初始化常量
        new Thread(() -> {
            Thread.currentThread().setName("add_hash_set_thread");
            int count = 0;
            while (true) {
                try {
                    hashSet.add("count" + count);
                    Thread.sleep(5);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 模拟线程阻塞
     * 线程池容量为1，但是向线程池中塞入两个线程
     */
    private static void thread() {
        Thread thread = new Thread(() -> {
            System.out.println("executorService thread start");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // 添加到线程
        executorService.submit(thread);
        executorService.submit(thread);
        executorService.submit(thread);
        executorService.submit(thread);
    }

    /**
     * 运行缓慢的方法
     */
    public static void runSlowThread(){
        new Thread(() -> {
        }).start();
    }
}
