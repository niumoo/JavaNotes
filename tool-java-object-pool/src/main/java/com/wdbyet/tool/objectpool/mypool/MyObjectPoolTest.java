package com.wdbyet.tool.objectpool.mypool;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import redis.clients.jedis.Jedis;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/02
 */
@State(Scope.Benchmark)
@Warmup(iterations = 1, time = 3)
@Measurement(iterations = 3, time = 3)
public class MyObjectPoolTest {
    private static MyObjectPool<Jedis> objectPool = new MyObjectPool<>();

    static {
        //objectPool.addObj(new Jedis("192.168.0.105", 6379));
    }

    public static void main(String[] args) {
        MyObjectPool<Jedis> objectPool = new MyObjectPool<>();
        // 增加一个 jedis 连接对象
        objectPool.addObj(new Jedis("127.0.0.1", 6379));
        objectPool.addObj(new Jedis("127.0.0.1", 6379));
        // 从对象池中借出一个 jedis 对象
        Jedis jedis = objectPool.borrowObj();
        // 一次 redis 查询
        String name = jedis.get("name");
        System.out.println(String.format("redis get:" + name));
        // 归还 redis 连接对象
        objectPool.returnObj(jedis);
        // 销毁对象池中的所有对象
        objectPool.destory();
        // 再次借用对象
        objectPool.borrowObj();
    }

    @Benchmark
    public void testPool(Blackhole bh) {
        Jedis jedis = objectPool.borrowObj();
        String name = jedis.get("name");
        objectPool.returnObj(jedis);
        bh.consume(name);
    }

    @Benchmark
    public void test(Blackhole bh) {
        Jedis jedis = new Jedis("localhost", 6379);
        String name = jedis.get("name");
        jedis.close();
        bh.consume(name);
    }
}
