package com.havens.siamese.db.cache;

import com.havens.siamese.db.*;
import com.havens.siamese.db.conf.CacheConfig;
import com.havens.siamese.db.conf.DBConfig;
import com.havens.siamese.db.dao.BackupDAO;
import com.havens.siamese.db.dao.DAO;
import com.havens.siamese.db.dao.DBObjectBackupDAO;
import com.havens.siamese.db.redis.RedisBackupDAO;
import org.apache.commons.dbutils.QueryRunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * table_name
 * <p/>
 * table_name_key_list -> [key1,key2,key3....]
 * <p/>
 * table_name_insert_key(n) -> [Object1,Object2,Object3....]
 * table_name_delete_key(n) -> [Object1,Object2,Object3....]
 * table_name_update_key(n) -> [Object1,Object2,Object3....]
 * <p/>
 * 一般来说key 是 heroId
 *
 * Created by havens on 15-8-12.
 */
public final class CacheManager {

    public static long SYNC_DATABASE_TIME = 900000; // 15 min

    public static final String CACHE_SUFFIX;
    public static final String REDIS_CACHE_NAME = "redis";

    public static final String INSERT_CACHE_OP = "_ins_";
    public static final String UPDATE_CACHE_OP = "_upd_";
    public static final String DELETE_CACHE_OP = "_del_";


    private static String CACHE_NAME;

    //AfterCleanKeyCache 一个清除数据库操作内存之后执行的接口
    private static AfterCleanCache afterCleanCache = null;

    private static Map<String, Long> keys;


    private static BackupDAO backupDAO;
    private static DAO dao;

    private CacheManager() {
    }

    static {
        CACHE_SUFFIX = DBManager.DB_CONFIG.cacheConfig.cachePrefix;
        init(DBManager.DB_CONFIG);
    }

    /**
     * must initialization
     */
    private static void init(final DBConfig dbConfig) {
        CacheConfig config = dbConfig.cacheConfig;
        CACHE_NAME = config.cache;
        // 10 sec to execute once

        SYNC_DATABASE_TIME = config.syncTime;
        try {
            afterCleanCache = (AfterCleanCache) dbConfig.afterCacheClass.newInstance();
        } catch (Exception ignored) {
        }
        keys = new ConcurrentHashMap<String, Long>();
        if (CACHE_NAME.equals(REDIS_CACHE_NAME)) {
            backupDAO = new RedisBackupDAO();
        } else {
            backupDAO = new DBObjectBackupDAO(DataSourceManager.getQueryRunner(KeyWords.MASTER),DataSourceManager.getQueryRunner(KeyWords.SLAVE));
        }
        dao = new DBObjectBackupDAO(DataSourceManager.getQueryRunner(KeyWords.MASTER),DataSourceManager.getQueryRunner(KeyWords.SLAVE));

        // load cache to db
        try {
            backupDAO.cleanDataToDB("*", dao);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    public static BackupDAO getCacheDAO(QueryRunner masterRunner,QueryRunner slaveRunner) {
        if (CACHE_NAME.equals(REDIS_CACHE_NAME)) {
            return new RedisBackupDAO();
        } else {
            // nothing save to cache
            return new DBObjectBackupDAO(masterRunner,slaveRunner);
        }
    }

    /*
            C A C H E    TO     D A T A B A S E
     */
    public static void refresh_cache_by_syn_time() {
        long curTime = System.currentTimeMillis();
        for (String key : keys.keySet()) {
            Long myTime = keys.get(key);
            if ((curTime - myTime) > SYNC_DATABASE_TIME) {
                update_cache_to_database_by_key(key);
                keys.put(key, curTime);
            }
        }
    }

    public static synchronized void update_cache_to_database_by_key(String key) {
        long cTime = System.currentTimeMillis();
        try {
            backupDAO.cleanDataToDBByKey(key, dao);
        } catch (DBException e) {
            e.printStackTrace();
        }
        System.err.println("cache sync to database,key: " + key + ", time(ms):"
                + (System.currentTimeMillis() - cTime));
    }

    public static void update_remove_cache_to_database_by_key(String key) {
        keys.remove(key);
        update_cache_to_database_by_key(key);
        if (afterCleanCache != null) {
            afterCleanCache.cleanKeyCache(key);
        }
    }

    public static void register_key(String key) {
        keys.put(key, System.currentTimeMillis());
    }
}
