
package com.havens.siamese.db.redis;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by havens on 15-8-12.
 */
public class RabbitJedisCluster extends JedisCluster implements MixJedisCommand {
    private Map<String, JedisPool> clusterPools;
    private List<RabbitJedis> jedisList;

    public RabbitJedisCluster(Set<HostAndPort> nodes) {
        super(nodes);
        clusterPools = getClusterNodes();
        jedisList = new ArrayList<RabbitJedis>();
        for (JedisPool pool : clusterPools.values()) {
            RabbitJedis rabbitJedis = new RabbitJedis(pool);
            jedisList.add(rabbitJedis);
        }
    }

    public synchronized Long del(final String... keys) {
        Long result = 0L;
        for (RabbitJedis rabbitJedis : jedisList) {
            result += rabbitJedis.del(keys);
        }
        return result;
    }


    public List<String> blpop(final int timeout, final String... keys) {
        return null;
    }


    public List<String> brpop(int timeout, String... keys) {
        return null;
    }


    public List<String> blpop(String... args) {
        return null;
    }


    public List<String> brpop(String... args) {
        return null;
    }


    public Set<String> keys(String pattern) {
        return null;
    }


    public List<String> mget(String... keys) {
        return null;
    }


    public String mset(String... keysvalues) {
        return null;
    }


    public Long msetnx(String... keysvalues) {
        return null;
    }


    public String rename(String oldkey, String newkey) {
        return null;
    }


    public Long renamenx(String oldkey, String newkey) {
        return null;
    }


    public String rpoplpush(String srckey, String dstkey) {
        return null;
    }


    public Set<String> sdiff(String... keys) {
        return null;
    }


    public Long sdiffstore(String dstkey, String... keys) {
        return null;
    }


    public Set<String> sinter(String... keys) {
        return null;
    }


    public Long sinterstore(String dstkey, String... keys) {
        return null;
    }


    public Long smove(String srckey, String dstkey, String member) {
        return null;
    }


    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        return null;
    }


    public Long sort(String key, String dstkey) {
        return null;
    }


    public Set<String> sunion(String... keys) {
        return null;
    }


    public Long sunionstore(String dstkey, String... keys) {
        return null;
    }


    public String watch(String... keys) {
        return null;
    }


    public String unwatch() {
        return null;
    }


    public Long zinterstore(String dstkey, String... sets) {
        return null;
    }


    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        return null;
    }


    public Long zunionstore(String dstkey, String... sets) {
        return null;
    }


    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        return null;
    }


    public String brpoplpush(String source, String destination, int timeout) {
        return null;
    }


    public Long publish(String channel, String message) {
        return null;
    }


    public void subscribe(JedisPubSub jedisPubSub, String... channels) {

    }


    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {

    }


    public String randomKey() {
        return null;
    }


    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        return null;
    }


    public ScanResult<String> scan(int cursor) {
        return null;
    }


    public ScanResult<String> scan(String cursor) {
        return null;
    }


    public String pfmerge(String destkey, String... sourcekeys) {
        return null;
    }


    public long pfcount(String... keys) {
        return 0;
    }
}
