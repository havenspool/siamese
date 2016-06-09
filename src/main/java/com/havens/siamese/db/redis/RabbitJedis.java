package com.havens.siamese.db.redis;

import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by havens on 15-8-12.
 */
public class RabbitJedis implements MixJedisCommand {
    private JedisPool pool;

    public RabbitJedis(JedisPool pool) {
        this.pool = pool;
    }

    public String set(final String key, final String value) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.set(key, value);
            }
        }.run();
    }

    public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.set(key, value, nxxx, expx, time);
            }
        }.run();
    }

    public String get(final String key) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.get(key);
            }
        }.run();
    }

    public Boolean exists(final String key) {
        return new RabbitJedisCommand<Boolean>(pool) {

            public Boolean execute(Jedis connection) {
                return connection.exists(key);
            }
        }.run();
    }

    public Long persist(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.persist(key);
            }
        }.run();
    }

    public String type(final String key) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.type(key);
            }
        }.run();
    }


    public Long expire(final String key, final int seconds) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.expire(key, seconds);
            }
        }.run();
    }


    public Long expireAt(final String key, final long unixTime) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.expireAt(key, unixTime);
            }
        }.run();
    }


    public Long ttl(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.ttl(key);
            }
        }.run();
    }


    public Boolean setbit(final String key, final long offset, final boolean value) {
        return new RabbitJedisCommand<Boolean>(pool) {

            public Boolean execute(Jedis connection) {
                return connection.setbit(key, offset, value);
            }
        }.run();
    }


    public Boolean setbit(final String key, final long offset, final String value) {
        return new RabbitJedisCommand<Boolean>(pool) {

            public Boolean execute(Jedis connection) {
                return connection.setbit(key, offset, value);
            }
        }.run();
    }


    public Boolean getbit(final String key, final long offset) {
        return new RabbitJedisCommand<Boolean>(pool) {

            public Boolean execute(Jedis connection) {
                return connection.getbit(key, offset);
            }
        }.run();
    }


    public Long setrange(final String key, final long offset, final String value) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.setrange(key, offset, value);
            }
        }.run();
    }


    public String getrange(final String key, final long startOffset, final long endOffset) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.getrange(key, startOffset, endOffset);
            }
        }.run();
    }


    public String getSet(final String key, final String value) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.getSet(key, value);
            }
        }.run();
    }


    public Long setnx(final String key, final String value) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.setnx(key, value);
            }
        }.run();
    }


    public String setex(final String key, final int seconds, final String value) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.setex(key, seconds, value);
            }
        }.run();
    }


    public Long decrBy(final String key, final long integer) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.decrBy(key, integer);
            }
        }.run();
    }


    public Long decr(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.decr(key);
            }
        }.run();
    }


    public Long incrBy(final String key, final long integer) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.incrBy(key, integer);
            }
        }.run();
    }


    public Long incr(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.incr(key);
            }
        }.run();
    }


    public Long append(final String key, final String value) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.append(key, value);
            }
        }.run();
    }


    public String substr(final String key, final int start, final int end) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.substr(key, start, end);
            }
        }.run();
    }


    public Long hset(final String key, final String field, final String value) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.hset(key, field, value);
            }
        }.run();
    }

    public String hget(final String key, final String field) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.hget(key, field);
            }
        }.run();
    }


    public Long hsetnx(final String key, final String field, final String value) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.hsetnx(key, field, value);
            }
        }.run();
    }


    public String hmset(final String key, final Map<String, String> hash) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.hmset(key, hash);
            }
        }.run();
    }


    public List<String> hmget(final String key, final String... fields) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.hmget(key, fields);
            }
        }.run();
    }


    public Long hincrBy(final String key, final String field, final long value) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.hincrBy(key, field, value);
            }
        }.run();
    }


    public Boolean hexists(final String key, final String field) {
        return new RabbitJedisCommand<Boolean>(pool) {

            public Boolean execute(Jedis connection) {
                return connection.hexists(key, field);
            }
        }.run();
    }


    public Long hdel(final String key, final String... field) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.hdel(key, field);
            }
        }.run();
    }


    public Long hlen(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.hlen(key);
            }
        }.run();
    }


    public Set<String> hkeys(final String key) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.hkeys(key);
            }
        }.run();
    }


    public List<String> hvals(final String key) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.hvals(key);
            }
        }.run();
    }

    public Map<String, String> hgetAll(final String key) {
        return new RabbitJedisCommand<Map<String, String>>(pool) {

            public Map<String, String> execute(Jedis connection) {
                return connection.hgetAll(key);
            }
        }.run();
    }


    public Long rpush(final String key, final String... string) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.rpush(key, string);
            }
        }.run();
    }

    public Long lpush(final String key, final String... string) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.lpush(key, string);
            }
        }.run();
    }


    public Long llen(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.llen(key);
            }
        }.run();
    }


    public List<String> lrange(final String key, final long start, final long end) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.lrange(key, start, end);
            }
        }.run();
    }


    public String ltrim(final String key, final long start, final long end) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.ltrim(key, start, end);
            }
        }.run();
    }


    public String lindex(final String key, final long index) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.lindex(key, index);
            }
        }.run();
    }

    public String lset(final String key, final long index, final String value) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.lset(key, index, value);
            }
        }.run();
    }


    public Long lrem(final String key, final long count, final String value) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.lrem(key, count, value);
            }
        }.run();
    }


    public String lpop(final String key) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.lpop(key);
            }
        }.run();
    }


    public String rpop(final String key) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.rpop(key);
            }
        }.run();
    }


    public Long sadd(final String key, final String... member) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.sadd(key, member);
            }
        }.run();
    }


    public Set<String> smembers(final String key) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.smembers(key);
            }
        }.run();
    }


    public Long srem(final String key, final String... member) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.srem(key, member);
            }
        }.run();
    }


    public String spop(final String key) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.spop(key);
            }
        }.run();
    }


    public Long scard(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.scard(key);
            }
        }.run();
    }


    public Boolean sismember(final String key, final String member) {
        return new RabbitJedisCommand<Boolean>(pool) {

            public Boolean execute(Jedis connection) {
                return connection.sismember(key, member);
            }
        }.run();
    }


    public String srandmember(final String key) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.srandmember(key);
            }
        }.run();
    }


    public List<String> srandmember(final String key, final int count) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.srandmember(key, count);
            }
        }.run();
    }


    public Long strlen(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.strlen(key);
            }
        }.run();
    }


    public Long zadd(final String key, final double score, final String member) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zadd(key, score, member);
            }
        }.run();
    }


    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zadd(key, scoreMembers);
            }
        }.run();
    }


    public Set<String> zrange(final String key, final long start, final long end) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrange(key, start, end);
            }
        }.run();
    }


    public Long zrem(final String key, final String... member) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zrem(key, member);
            }
        }.run();
    }


    public Double zincrby(final String key, final double score, final String member) {
        return new RabbitJedisCommand<Double>(pool) {

            public Double execute(Jedis connection) {
                return connection.zincrby(key, score, member);
            }
        }.run();
    }


    public Long zrank(final String key, final String member) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zrank(key, member);
            }
        }.run();
    }


    public Long zrevrank(final String key, final String member) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zrevrank(key, member);
            }
        }.run();
    }


    public Set<String> zrevrange(final String key, final long start, final long end) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrevrange(key, start, end);
            }
        }.run();
    }


    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeWithScores(key, start, end);
            }
        }.run();
    }


    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeWithScores(key, start, end);
            }
        }.run();
    }


    public Long zcard(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zcard(key);
            }
        }.run();
    }


    public Double zscore(final String key, final String member) {
        return new RabbitJedisCommand<Double>(pool) {

            public Double execute(Jedis connection) {
                return connection.zscore(key, member);
            }
        }.run();
    }


    public List<String> sort(final String key) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.sort(key);
            }
        }.run();
    }


    public List<String> sort(final String key, final SortingParams sortingParameters) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.sort(key, sortingParameters);
            }
        }.run();
    }


    public Long zcount(final String key, final double min, final double max) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zcount(key, min, max);
            }
        }.run();
    }


    public Long zcount(final String key, final String min, final String max) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zcount(key, min, max);
            }
        }.run();
    }


    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max);
            }
        }.run();
    }


    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max);
            }
        }.run();
    }


    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min);
            }
        }.run();
    }


    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max, offset, count);
            }
        }.run();
    }


    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min);
            }
        }.run();
    }


    public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max, offset, count);
            }
        }.run();
    }


    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min, offset, count);
            }
        }.run();
    }


    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max);
            }
        }.run();
    }


    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min);
            }
        }.run();
    }


    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, min, max, offset, count);
            }
        }.run();
    }


    public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min, offset, count);
            }
        }.run();
    }


    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max);
            }
        }.run();
    }


    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min);
            }
        }.run();
    }


    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        }.run();
    }


    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        }.run();
    }


    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        return new RabbitJedisCommand<Set<Tuple>>(pool) {

            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        }.run();
    }


    public Long zremrangeByRank(final String key, final long start, final long end) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zremrangeByRank(key, start, end);
            }
        }.run();
    }


    public Long zremrangeByScore(final String key, final double start, final double end) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zremrangeByScore(key, start, end);
            }
        }.run();
    }


    public Long zremrangeByScore(final String key, final String start, final String end) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zremrangeByScore(key, start, end);
            }
        }.run();
    }


    public Long zlexcount(final String key, final String min, final String max) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zlexcount(key, min, max);
            }
        }.run();
    }


    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrangeByLex(key, min, max);
            }
        }.run();
    }


    public Set<String> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.zrangeByLex(key, min, max, offset, count);
            }
        }.run();
    }


    public Long zremrangeByLex(final String key, final String min, final String max) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zremrangeByLex(key, min, max);
            }
        }.run();
    }


    public Long linsert(final String key, final BinaryClient.LIST_POSITION where, final String pivot, final String value) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.linsert(key, where, pivot, value);
            }
        }.run();
    }


    public Long lpushx(final String key, final String... string) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.lpushx(key, string);
            }
        }.run();
    }


    public Long rpushx(final String key, final String... string) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.rpushx(key, string);
            }
        }.run();
    }


    public List<String> blpop(final String arg) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.blpop(arg);
            }
        }.run();
    }


    public List<String> blpop(final int timeout, final String key) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.blpop(timeout, key);
            }
        }.run();
    }


    public List<String> brpop(final String arg) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.brpop(arg);
            }
        }.run();
    }


    public List<String> brpop(final int timeout, final String key) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.brpop(timeout, key);
            }
        }.run();
    }


    public Long del(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.del(key);
            }
        }.run();
    }


    public String echo(final String string) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.echo(string);
            }
        }.run();
    }


    public Long move(final String key, final int dbIndex) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.move(key, dbIndex);
            }
        }.run();
    }


    public Long bitcount(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.bitcount(key);
            }
        }.run();
    }


    public Long bitcount(final String key, final long start, final long end) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.bitcount(key, start, end);
            }
        }.run();
    }


    public ScanResult<Map.Entry<String, String>> hscan(final String key, final int cursor) {
        return new RabbitJedisCommand<ScanResult<Map.Entry<String, String>>>(pool) {

            public ScanResult<Map.Entry<String, String>> execute(Jedis connection) {
                return connection.hscan(key, cursor);
            }
        }.run();
    }


    public ScanResult<String> sscan(final String key, final int cursor) {
        return new RabbitJedisCommand<ScanResult<String>>(pool) {

            public ScanResult<String> execute(Jedis connection) {
                return connection.sscan(key, cursor);
            }
        }.run();
    }


    public ScanResult<Tuple> zscan(final String key, final int cursor) {
        return new RabbitJedisCommand<ScanResult<Tuple>>(pool) {

            public ScanResult<Tuple> execute(Jedis connection) {
                return connection.zscan(key, cursor);
            }
        }.run();
    }


    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        return new RabbitJedisCommand<ScanResult<Map.Entry<String, String>>>(pool) {

            public ScanResult<Map.Entry<String, String>> execute(Jedis connection) {
                return connection.hscan(key, cursor);
            }
        }.run();
    }


    public ScanResult<String> sscan(final String key, final String cursor) {
        return new RabbitJedisCommand<ScanResult<String>>(pool) {

            public ScanResult<String> execute(Jedis connection) {
                return connection.sscan(key, cursor);
            }
        }.run();
    }


    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        return new RabbitJedisCommand<ScanResult<Tuple>>(pool) {

            public ScanResult<Tuple> execute(Jedis connection) {
                return connection.zscan(key, cursor);
            }
        }.run();
    }


    public Long pfadd(final String key, final String... elements) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.pfadd(key, elements);
            }
        }.run();
    }


    public long pfcount(final String key) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.pfcount(key);
            }
        }.run();
    }


    public Long del(final String... keys) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.del(keys);
            }
        }.run();
    }


    public List<String> blpop(final int timeout, final String... keys) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.blpop(timeout, keys);
            }
        }.run();
    }


    public List<String> brpop(final int timeout, final String... keys) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.brpop(timeout, keys);
            }
        }.run();
    }


    public List<String> blpop(final String... args) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.blpop(args);
            }
        }.run();
    }


    public List<String> brpop(final String... args) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.brpop(args);
            }
        }.run();
    }


    public Set<String> keys(final String pattern) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.keys(pattern);
            }
        }.run();
    }


    public List<String> mget(final String... keys) {
        return new RabbitJedisCommand<List<String>>(pool) {

            public List<String> execute(Jedis connection) {
                return connection.mget(keys);
            }
        }.run();
    }


    public String mset(final String... keysvalues) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.mset(keysvalues);
            }
        }.run();
    }


    public Long msetnx(final String... keysvalues) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.msetnx(keysvalues);
            }
        }.run();
    }


    public String rename(final String oldkey, final String newkey) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.rename(oldkey, newkey);
            }
        }.run();
    }


    public Long renamenx(final String oldkey, final String newkey) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.renamenx(oldkey, newkey);
            }
        }.run();
    }


    public String rpoplpush(final String srckey, final String dstkey) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.rpoplpush(srckey, dstkey);
            }
        }.run();
    }


    public Set<String> sdiff(final String... keys) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.sdiff(keys);
            }
        }.run();
    }


    public Long sdiffstore(final String dstkey, final String... keys) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.sdiffstore(dstkey, keys);
            }
        }.run();
    }


    public Set<String> sinter(final String... keys) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.sinter(keys);
            }
        }.run();
    }


    public Long sinterstore(final String dstkey, final String... keys) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.sinterstore(dstkey, keys);
            }
        }.run();
    }


    public Long smove(final String srckey, final String dstkey, final String member) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.smove(srckey, dstkey, member);
            }
        }.run();
    }


    public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.sort(key, sortingParameters, dstkey);
            }
        }.run();
    }


    public Long sort(final String key, final String dstkey) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.sort(key, dstkey);
            }
        }.run();
    }


    public Set<String> sunion(final String... keys) {
        return new RabbitJedisCommand<Set<String>>(pool) {

            public Set<String> execute(Jedis connection) {
                return connection.sunion(keys);
            }
        }.run();
    }


    public Long sunionstore(final String dstkey, final String... keys) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.sunionstore(dstkey, keys);
            }
        }.run();
    }


    public String watch(final String... keys) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.watch(keys);
            }
        }.run();
    }


    public String unwatch() {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.unwatch();
            }
        }.run();
    }


    public Long zinterstore(final String dstkey, final String... sets) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zinterstore(dstkey, sets);
            }
        }.run();
    }


    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zinterstore(dstkey, params, sets);
            }
        }.run();
    }


    public Long zunionstore(final String dstkey, final String... sets) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zunionstore(dstkey, sets);
            }
        }.run();
    }


    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.zunionstore(dstkey, params, sets);
            }
        }.run();
    }


    public String brpoplpush(final String source, final String destination, final int timeout) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.brpoplpush(source, destination, timeout);
            }
        }.run();
    }


    public Long publish(final String channel, final String message) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.publish(channel, message);
            }
        }.run();
    }


    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                connection.subscribe(jedisPubSub, channels);
                return 0L;
            }
        }.run();
    }


    public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
        new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                connection.subscribe(jedisPubSub, patterns);
                return 0L;
            }
        }.run();
    }


    public String randomKey() {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.randomKey();
            }
        }.run();
    }


    public Long bitop(final BitOP op, final String destKey, final String... srcKeys) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.bitop(op, destKey, srcKeys);
            }
        }.run();
    }


    public ScanResult<String> scan(final int cursor) {
        return new RabbitJedisCommand<ScanResult<String>>(pool) {

            public ScanResult<String> execute(Jedis connection) {
                return connection.scan(cursor);
            }
        }.run();
    }


    public ScanResult<String> scan(final String cursor) {
        return new RabbitJedisCommand<ScanResult<String>>(pool) {

            public ScanResult<String> execute(Jedis connection) {
                return connection.scan(cursor);
            }
        }.run();
    }


    public String pfmerge(final String destkey, final String... sourcekeys) {
        return new RabbitJedisCommand<String>(pool) {

            public String execute(Jedis connection) {
                return connection.pfmerge(destkey, sourcekeys);
            }
        }.run();
    }


    public long pfcount(final String... keys) {
        return new RabbitJedisCommand<Long>(pool) {

            public Long execute(Jedis connection) {
                return connection.pfcount(keys);
            }
        }.run();
    }
}
