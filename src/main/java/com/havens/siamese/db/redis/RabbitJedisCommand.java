
package com.havens.siamese.db.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by havens on 15-8-12.
 */
public abstract class RabbitJedisCommand<T> {

    private JedisPool pool;

    public RabbitJedisCommand(JedisPool pool) {
        this.pool = pool;
    }

    public abstract T execute(Jedis connection);

    public T run() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return execute(jedis);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
//            returnResource(pool, jedis);
            pool.returnResource(jedis);
        }
        return null;
    }
}
