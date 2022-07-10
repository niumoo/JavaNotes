package com.wdbyet.tool.objectpool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/09
 */
public class JedisPoolTest {

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        // 从对象池中借一个对象
        Jedis jedis = jedisPool.getResource();
        String name = jedis.get("name");
        System.out.println("redis get :" + name);
        jedis.close();
        // 彻底退出前，关闭 Redis 连接池
        jedisPool.close();
    }
}
