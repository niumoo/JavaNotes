import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * 虚拟线程
 *
 * @author https://www.wdbyte.com
 * @date 2023/10/10
 */
public class Jep444VirtualThread {
    public static void main(String[] args) throws InterruptedException {
        // 创建并提交执行虚拟线程
        long start = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }
        System.out.println("time:" + (System.currentTimeMillis() - start) + "ms");


        // 创建一个虚拟线程指定虚拟线程名称
        Thread thread1 = Thread.ofVirtual().name("v-thread").unstarted(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(String.format("[%s] Hello Virtual Thread", threadName));
        });
        thread1.start();
        System.out.println(thread1.isVirtual());

        //创建一个线程，启动为虚拟线程
        Thread thread2 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(String.format("[%s] Hello Virtual Thread 2", threadName));
        });

        Thread.startVirtualThread(thread2);

        // 判断一个线程是否是虚拟线程
        System.out.println(thread1.isVirtual());
        System.out.println(thread2.isVirtual());

        Thread.sleep(1000);
    }
}
