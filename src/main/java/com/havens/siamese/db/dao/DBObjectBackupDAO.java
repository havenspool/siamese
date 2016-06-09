package com.havens.siamese.db.dao;

import com.havens.siamese.db.DBException;
import org.apache.commons.dbutils.QueryRunner;

/**
 * Created by havens on 15-8-12.
 */
public class DBObjectBackupDAO extends DBObjectDAO implements BackupDAO {

    public DBObjectBackupDAO(QueryRunner masterRunner, QueryRunner selectRunner) {
        super(masterRunner,selectRunner);
    }


    public void cleanData(String table, String key) throws DBException {
        // no implement
    }


    public void cleanData(String table) throws DBException {
        // no implement
    }


    public void cleanDataByKey(String table) throws DBException {
        // no implement
    }


    public void cleanDataToDB(String table, String myKey, DAO dao) throws DBException {
        // no implement
    }


    public void cleanDataToDB(String table, DAO dao) throws DBException {
        // no implement
    }


    public void cleanDataToDBByKey(String key, DAO dao) throws DBException {
        // no implement
    }
}
