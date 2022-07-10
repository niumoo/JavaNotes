package com.wdbyet.tool.objectpool.apachepool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;

/**
 * @author https://www.wdbyte.com
 * @date 2022/07/07
 */
public class ApachePool {

    public static void main(String[] args) throws Exception {
        MyGenericObjectPool objectMyObjectPool = new MyGenericObjectPool(new MyPooledObjectFactory());
        Jedis jedis = objectMyObjectPool.borrowObject();
        String name = jedis.get("name");
        System.out.println("redis get:" + name);
        objectMyObjectPool.returnObject(jedis);
        objectMyObjectPool.close();
    }

}

class MyPooledObjectFactory implements PooledObjectFactory<Jedis> {

    @Override
    public void activateObject(PooledObject<Jedis> pooledObject) throws Exception {

    }

    @Override
    public void destroyObject(PooledObject<Jedis> pooledObject) throws Exception {
        Jedis jedis = pooledObject.getObject();
        jedis.close();
        System.out.println("释放连接");
    }

    @Override
    public PooledObject<Jedis> makeObject() throws Exception {
        return new DefaultPooledObject(new Jedis("localhost", 6379));
    }

    @Override
    public void passivateObject(PooledObject<Jedis> pooledObject) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<Jedis> pooledObject) {
        return false;
    }
}

class SimplePooledObjectFactory extends BasePooledObjectFactory<Jedis> {

    @Override
    public Jedis create() throws Exception {
        return new Jedis("127.0.0.1", 6379);
    }

    @Override
    public PooledObject<Jedis> wrap(Jedis jedis) {
        return new DefaultPooledObject<>(jedis);
    }
}

class MyGenericObjectPool extends GenericObjectPool<Jedis> {

    public MyGenericObjectPool(PooledObjectFactory factory) {
        super(factory);
    }

    public MyGenericObjectPool(PooledObjectFactory factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }

    public MyGenericObjectPool(PooledObjectFactory factory, GenericObjectPoolConfig config,
        AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}


