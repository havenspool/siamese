
package com.havens.siamese.db.redis;

import com.havens.siamese.db.DBManager;
import com.havens.siamese.db.cache.CacheManager;
import com.havens.siamese.db.conf.CacheConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.List;
import java.util.Set;

/**
 * Created by havens on 15-8-12.
 */
public final class RedisClient {
    private static final JedisPool pool;
    private static final MixJedisCommand commands;

    static {
        // just init cache manager
        pool = init(DBManager.DB_CONFIG.cacheConfig);
        commands = new RabbitJedis(pool);
    }

    private RedisClient() {
    }

    private static JedisPool init(final CacheConfig config) {
        if (config.cache.equals(CacheManager.REDIS_CACHE_NAME)) {
            System.err.println("Using redis to cache operation data!");
            if (!config.cluster) {
                CacheConfig.Host host = config.hosts.get(0);
//                System.err.println("AUTH password:" + config.password);
                return init(host.host, host.port, config.password);
            }
        }
        return null;
    }

    public static JedisPool init(String ip, int port, String pwd) {
        JedisPoolConfig config = new JedisPoolConfig();
        if (pwd == null || pwd.trim().length() == 0) {
            pwd = null;
        }
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(500);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
//            config.setMaxWait(1000 * 10);
        config.setMaxWaitMillis(1000 * 10);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        return new JedisPool(config, ip, port, Protocol.DEFAULT_TIMEOUT, pwd);
//            commands = new RabbitJedis(pool);
    }

//    public static void initCluster(CacheConfig config) {
//        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
//        for (CacheConfig.Host host : config.hosts) {
//            hostAndPorts.add(new HostAndPort(host.host, host.port));
//        }
//
//        RabbitJedisCluster cluster = new RabbitJedisCluster(hostAndPorts);
//        cluster.auth(config.password);
//        commands = cluster;
//
//        // todo get pools
//    }

    /**
     * 构建redis连接池
     *
     * @return JedisPool
     */
    public static JedisPool getPool() {
        return pool;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static String get(final String key) {
        return commands.get(key);
    }

    public static void del(final String key) {
        commands.del(key);
    }

    public static void delKeyPattern(final String parttern) {
        new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis jedis) {
                Set<String> result = jedis.keys(parttern);
                if (result.size() > 0)
                    jedis.del(result.toArray(new String[result.size()]));
                return 0L;
            }
        }.run();
    }

    public static void dels(final Set<String> keys) {
        if (keys.size() > 0)
            commands.del(keys.toArray(new String[keys.size()]));
    }

    public static void set(final String key, final String value) {
        commands.set(key, value);
    }

    public static Set<String> keys(final String parttern) {
        return commands.keys(parttern);
    }

    /**
     * SET OPERATION
     */

    public static void sadd(final String key, final String... value) {
        commands.sadd(key, value);
    }

    public static Set<String> smembers(final String key) {
        return commands.smembers(key);
    }

    public static Boolean sismember(final String key, final String value) {
        return commands.sismember(key, value);
    }

    public static Boolean smove(final String key, final String... value) {
        return (commands.srem(key, value) > 0);
    }

    /*

                   LIST OPERATION
                   lrange
                   lpush
                   rpush
                   linsert
                   lset
                   lrem
                   ltrim
                   lpop
                   rpop
                   rpoppush
                   lindex
                   llen

     */


    public static List<String> lrange(String key, int start, int end) {
        JedisPool pool = getPool();
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = pool.getResource();
            result = jedis.lrange(key, start, end);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return result;
    }

    public static void rpush(String key, String value) {
        JedisPool pool = getPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.rpush(key, value);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    public static void lset(String key, int idx, String value) {
        JedisPool pool = getPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lset(key, idx, value);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }


    public static void lrem(String key, int count, String value) {
        JedisPool pool = getPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lrem(key, count, value);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    /*
                    SORT OPERATION

     */


    public static List<String> sort(String key) {
        JedisPool pool = getPool();
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = pool.getResource();
            result = jedis.sort(key);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return result;
    }

    /*

                  HASH MAP

     */
    public static void hset(String key, String key1, String value1) {
        JedisPool pool = getPool();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hset(key, key1, value1);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    public static String hget(String key, String filed) {
        JedisPool pool = getPool();
        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.hget(key, filed);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return result;
    }

    public static long hlen(String key) {
        JedisPool pool = getPool();
        Jedis jedis = null;
        long result = 0;
        try {
            jedis = pool.getResource();
            result = jedis.hlen(key);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return result;
    }
}
