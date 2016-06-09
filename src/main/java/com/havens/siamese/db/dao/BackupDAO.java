package com.havens.siamese.db.dao;

import com.havens.siamese.db.DBException;

/**
 * Created by havens on 15-8-12.
 */
public interface BackupDAO extends DAO {

    // just for back up dao

    void cleanData(String table, String key) throws DBException;

    void cleanData(String table) throws DBException;

    void cleanDataByKey(String key) throws DBException;

    void cleanDataToDB(String table, String myKey, DAO dao) throws DBException;

    void cleanDataToDB(String table, DAO dao) throws DBException;

    void cleanDataToDBByKey(String key, DAO dao) throws DBException;
}
