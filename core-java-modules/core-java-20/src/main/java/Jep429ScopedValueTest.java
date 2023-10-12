import jdk.incubator.concurrent.ScopedValue;

/**
 * --add-modules jdk.incubator.concurrent
 *
 * @date 2023/05/04
 */
public class Jep429ScopedValueTest {
    final static ScopedValue<String> SCOPED_VALUE = ScopedValue.newInstance();

    public static void main(String[] args) {
        // 创建线程
        Thread thread1 = new Thread(Jep429ScopedValueTest::handle);
        Thread thread2 = new Thread(Jep429ScopedValueTest::handle);
        String str = "hello wdbyte";
        // 传入线程里使用的字符串信息
        ScopedValue.where(SCOPED_VALUE, str).run(thread1);
        ScopedValue.where(SCOPED_VALUE, str).run(thread2);
        // 执行完毕自动清空，这里获取不到了。
        System.out.println(SCOPED_VALUE.orElse("没有信息"));
    }

    public static void handle() {
        String result = SCOPED_VALUE.get();
        System.out.println(result);
    }

}