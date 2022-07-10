package com.wdbyet.tool.objectpool.apachekeyedpool;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import redis.clients.jedis.Jedis;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/07
 */
public class ApacheKeyedPool {

    public static void main(String[] args) throws Exception {
        String key = "local";
        MyGenericKeyedObjectPool objectMyObjectPool = new MyGenericKeyedObjectPool(new MyKeyedPooledObjectFactory());
        Jedis jedis = objectMyObjectPool.borrowObject(key);
        String name = jedis.get("name");
        System.out.println("redis get :" + name);
        objectMyObjectPool.returnObject(key, jedis);
    }
}

class MyKeyedPooledObjectFactory extends BaseKeyedPooledObjectFactory<String, Jedis> {

    @Override
    public Jedis create(String key) throws Exception {
        if ("local".equals(key)) {
            return new Jedis("localhost", 6379);
        }
        if ("remote".equals(key)) {
            return new Jedis("192.168.0.105", 6379);
        }
        return null;
    }

    @Override
    public PooledObject<Jedis> wrap(Jedis value) {
        return new DefaultPooledObject<>(value);
    }
}

class MyGenericKeyedObjectPool extends GenericKeyedObjectPool<String, Jedis> {

    public MyGenericKeyedObjectPool(KeyedPooledObjectFactory<String, Jedis> factory) {
        super(factory);
    }

    public MyGenericKeyedObjectPool(KeyedPooledObjectFactory<String, Jedis> factory,
        GenericKeyedObjectPoolConfig<Jedis> config) {
        super(factory, config);
    }

    public MyGenericKeyedObjectPool(KeyedPooledObjectFactory<String, Jedis> factory,
        GenericKeyedObjectPoolConfig<Jedis> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}


