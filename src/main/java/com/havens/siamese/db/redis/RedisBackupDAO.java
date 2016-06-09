
package com.havens.siamese.db.redis;

import com.havens.siamese.db.DBException;
import com.havens.siamese.db.DBObject;
import com.havens.siamese.db.DBObjectManager;
import com.havens.siamese.db.cache.CacheItem;
import com.havens.siamese.db.cache.CacheManager;
import com.havens.siamese.db.dao.BackupDAO;
import com.havens.siamese.db.dao.DAO;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by havens on 15-8-12.
 */
public class RedisBackupDAO implements BackupDAO {

    private final JedisPool pool;

    public RedisBackupDAO() {
        pool = RedisClient.getPool();
    }

    public void update(DBObject obj) throws DBException {
        update(obj, obj.getTableName());
    }

    public void delete(DBObject obj) throws DBException {
        delete(obj, obj.getTableName());
    }

    public void insert(DBObject obj) throws DBException {
        insert(obj, obj.getTableName());
    }

    /**
     * 写redis数据
     *
     * @param key
     * @param value
     * @throws DBException
     */
    private void redisSetValue(final String key, final String value) throws DBException {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
            throw new DBException(e.getMessage());
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void delete(DBObject obj, String table) throws DBException {
        redisSetValue(obj.deleteKEY(table), obj.toDBJson(table).toString());
    }

    public void update(DBObject obj, String table) throws DBException {
        redisSetValue(obj.updateKEY(table), obj.toDBJson(table).toString());
    }

    public void insert(DBObject obj, String table) throws DBException {
        redisSetValue(obj.insertKEY(table), obj.toDBJson(table).toString());
    }

    public void update(Collection<? extends DBObject> objs, String table_name) throws DBException {
        //
        throw new DBException("no implements.");
    }

    public void insert(Collection<? extends DBObject> objs, String table_name) throws DBException {
        throw new DBException("no implements.");
    }

    public void delete(Collection<? extends DBObject> objs, String table_name) throws DBException {
        throw new DBException("no implements.");
    }


    public void cleanData(String table, String key) throws DBException {
        String uKeys = CacheManager.CACHE_SUFFIX + key + CacheManager.UPDATE_CACHE_OP + table + "*";
        String dKeys = CacheManager.CACHE_SUFFIX + key + CacheManager.DELETE_CACHE_OP + table + "*";
        String iKeys = CacheManager.CACHE_SUFFIX + key + CacheManager.INSERT_CACHE_OP + table + "*";

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> result = jedis.keys(uKeys);
            if (result.size() > 0)
                jedis.del(result.toArray(new String[result.size()]));

            result = jedis.keys(dKeys);
            if (result.size() > 0)
                jedis.del(result.toArray(new String[result.size()]));

            result = jedis.keys(iKeys);
            if (result.size() > 0)
                jedis.del(result.toArray(new String[result.size()]));
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
            throw new DBException(e.getMessage());
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void cleanData(String table) throws DBException {
        String uKeys = CacheManager.CACHE_SUFFIX + "*" + CacheManager.UPDATE_CACHE_OP + table + "*";
        String dKeys = CacheManager.CACHE_SUFFIX + "*" + CacheManager.DELETE_CACHE_OP + table + "*";
        String iKeys = CacheManager.CACHE_SUFFIX + "*" + CacheManager.INSERT_CACHE_OP + table + "*";

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> result = jedis.keys(uKeys);
            if (result.size() > 0)
                jedis.del(result.toArray(new String[result.size()]));

            result = jedis.keys(dKeys);
            if (result.size() > 0)
                jedis.del(result.toArray(new String[result.size()]));

            result = jedis.keys(iKeys);
            if (result.size() > 0)
                jedis.del(result.toArray(new String[result.size()]));
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
            throw new DBException(e.getMessage());
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void cleanDataByKey(String key) throws DBException {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
            throw new DBException(e.getMessage());
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void cleanDataToDB(String table, String myKey, DAO dao) throws DBException {
        String pattern = CacheManager.CACHE_SUFFIX + myKey + "_*_" + table + ":*";
        _cleanDataToDB(pattern, dao);
    }

    public void cleanDataToDB(String table, DAO dao) throws DBException {
        String pattern = CacheManager.CACHE_SUFFIX + "*_" + table + ":*";
        _cleanDataToDB(pattern, dao);
    }

    public void cleanDataToDBByKey(String key, DAO dao) throws DBException {
        String pattern = CacheManager.CACHE_SUFFIX + key + "_*";
        _cleanDataToDB(pattern, dao);
    }


    private void _cleanDataToDB(final String pattern, DAO dao) throws DBException {
        Set<String> keys;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            keys = jedis.keys(pattern);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
            throw new DBException(e.getMessage());
        } finally {
            pool.returnResource(jedis);
        }

        List<CacheItem> dels = new ArrayList<CacheItem>();
        List<CacheItem> inss = new ArrayList<CacheItem>();
        List<CacheItem> udps = new ArrayList<CacheItem>();
        if (keys.size() == 0) {
            return;
        }

        for (String key : keys) {
            String[] key_split = key.split("_");
            if (key_split.length > 3) {
                String itemKey = key_split[1];
                String op = "_" + key_split[2] + "_";
                String value = RedisClient.get(key);
                String table_name;
                if (key_split.length > 4) {
                    table_name = key_split[3];
                    for (int i = 4; i < key_split.length; i++) {
                        table_name = table_name + "_" + key_split[i];
                    }
                    table_name = table_name.split(":")[0];
                } else {
                    table_name = key_split[3].split(":")[0];
                }

                try {
                    DBObject obj = (DBObject) DBObjectManager.getClassByTable(table_name).newInstance();
                    obj.JsonToObj(new JSONObject(value));
                    CacheItem item = new CacheItem(table_name, obj);
                    if (op.equals(CacheManager.DELETE_CACHE_OP)) {
                        dels.add(item);
                    } else if (op.equals(CacheManager.UPDATE_CACHE_OP)) {
                        udps.add(item);
                    } else if (op.equals(CacheManager.INSERT_CACHE_OP)) {
                        inss.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (CacheItem item : inss) {
            try {
                dao.insert(item.obj, item.table_name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        for (CacheItem item : udps) {
            try {
                dao.update(item.obj, item.table_name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        for (CacheItem item : dels) {
            try {
                dao.delete(item.obj, item.table_name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            jedis = pool.getResource();
            jedis.del(keys.toArray(new String[keys.size()]));
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
            throw new DBException(e.getMessage());
        } finally {
            pool.returnResource(jedis);
        }
    }
}
