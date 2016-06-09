package com.havens.siamese.db.cache;

import com.havens.siamese.db.DBObject;
import com.havens.siamese.db.dao.BackupDAO;
import com.havens.siamese.db.DBException;
import com.havens.siamese.db.dao.CacheDAO;
import com.havens.siamese.db.dao.DBObjectDAO;
import org.apache.commons.dbutils.QueryRunner;

/**
 * Created by havens on 15-8-12.
 */
public abstract class DBObjectCacheDAO extends DBObjectDAO implements CacheDAO {

    protected BackupDAO backupDao; // 缓存操作接口  //主库

    public DBObjectCacheDAO(QueryRunner masterRunner,QueryRunner slaveRunner) {
        super(masterRunner,slaveRunner);
        backupDao = CacheManager.getCacheDAO(masterRunner,slaveRunner);
    }

    /**
     * 以下3个方法，根据需求来复写
     */
    public void _insertCacheObject(DBObject obj, String table) throws DBException {
    }

    public void _updateCacheObject(DBObject obj, String table) throws DBException {
    }

    public void _deleteCacheObject(DBObject obj, String table) throws DBException {
    }

    public void insertCacheObject(DBObject obj) throws DBException {
        insertCacheObject(obj, obj.getTableName());
    }

    public void deleteCacheObject(DBObject obj) throws DBException {
        deleteCacheObject(obj, obj.getTableName());
    }

    public void updateCacheObject(DBObject obj) throws DBException {
        updateCacheObject(obj, obj.getTableName());
    }

    public void insertCacheObject(DBObject obj, String table) throws DBException {
        obj.generateId(table, idGen);
        backupDao.insert(obj, table);
        _insertCacheObject(obj, table);
    }

    public void deleteCacheObject(DBObject obj, String table) throws DBException {
        backupDao.delete(obj, table);
        _deleteCacheObject(obj, table);
    }

    public void updateCacheObject(DBObject obj, String table) throws DBException {
        backupDao.update(obj, table);
        _updateCacheObject(obj, table);
    }

    public final void insertCacheIncreaseByIdObject(String table, DBObject obj) throws DBException {
        // 直接insert数据库
        insert(obj, table);
        _insertCacheObject(obj, table);
    }

    public final void cleanCacheObject(String table, String _key) throws DBException {
        backupDao.cleanDataToDB(table, _key, this);
    }

    public final void cleanCacheObject(String table) throws DBException {
        backupDao.cleanDataToDB(table, this);
    }

    public final void cleanCacheObjectByKey(String key) throws DBException {
        backupDao.cleanDataToDBByKey(key, this);
    }


    public final void cleanCacheObjectNoSync(String table, String _key) throws DBException {
        backupDao.cleanData(table, _key);
    }

    public final void cleanCacheObjectNoSync(String table) throws DBException {
        backupDao.cleanData(table);
    }

    public final void cleanCacheObjectByKeyNoSync(String key) throws DBException {
        backupDao.cleanDataByKey(key);
    }
}
