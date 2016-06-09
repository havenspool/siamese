package com.havens.siamese.db.dao;

import com.havens.siamese.db.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by havens on 15-8-12.
 */
public abstract class DBObjectDAO implements DAO {
    protected QueryRunner masterRunner;
    protected QueryRunner slaveRunner;
    protected GenKeyQueryRunner innerInsertRunner;
    protected IdGenerator idGen;

    public DBObjectDAO(QueryRunner masterRunner,QueryRunner selectRunner) {
        setQueryRunner(masterRunner,selectRunner);
    }

    public void setQueryRunner(QueryRunner masterRunner,QueryRunner slaveRunner) {
        this.masterRunner = masterRunner;
        this.slaveRunner = slaveRunner;
        innerInsertRunner = new GenKeyQueryRunner<Long>(masterRunner.getDataSource(),new ScalarHandler<Long>());
    }

    public void setIdGenerator(IdGenerator idGen) {
        this.idGen = idGen;
    }

    public void update(DBObject obj) throws DBException {
        update(obj, obj.getTableName());
    }

    public void insert(DBObject obj) throws DBException {
        insert(obj, obj.getTableName());
    }

    public void delete(DBObject obj) throws DBException {
        delete(obj, obj.getTableName());
    }

    public void update(DBObject obj, String table) throws DBException {
        try {
            Set<String> primary_keys = DBObjectManager.getTablePrimaryKey(table);
            Set<String> columns = DBObjectManager.getTableAllColumnsNoKey(table);
            Object[] objs = new Object[columns.size() + primary_keys.size()];
            int count = 0;
            for (String column : columns) {
                objs[count] = obj.getValueByField(column);
                count++;
            }

            for (String primary_key : primary_keys) {
                objs[count] = obj.getValueByField(primary_key);
                count++;
            }
            String sql = DBObjectManager.getUpdateSQLByTable(table);
            int mount = masterRunner.update(sql, objs);
            if (mount < 1) {
                throw new SQLException("No data update." + sql + "\n" + obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(e.getMessage());
        }
    }

    public void delete(DBObject obj, String table) throws DBException {
        try {
            Set<String> primary_keys = DBObjectManager.getTablePrimaryKey(table);
            Object[] objs = new Object[primary_keys.size()];
            int count = 0;
            for (String primary_key : primary_keys) {
                objs[count] = obj.getValueByField(primary_key);
                count++;
            }
            String sql = DBObjectManager.getDeleteSQLByTable(table);
            int mount = masterRunner.update(sql, objs);
            if (mount < 1) {
                throw new SQLException("No data delete." + sql + "\n" + obj);
            }
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    public void insert(DBObject obj, String table) throws DBException {
        try {
            Field keyField = DBObjectManager.getInsertIncrKeyField(table);
            Set<String> columns = DBObjectManager.getTableAllColumnsNoIncr(table); // true means if it is not auto increase then add key's column
            Object[] objs = new Object[columns.size()];
            int count = 0;
            for (String column : columns) {
                objs[count] = obj.getValueByField(column);
                count++;
            }

            String sql = DBObjectManager.getInsertSQLByTable(table);
            // no thread safe
            int mount = innerInsertRunner.insert(sql, objs);
            if (mount < 1) {
                throw new SQLException("No data insert." + sql + "\n" + obj);
            }

            if (keyField != null /* is auto increase */) {
                if (keyField.getType().equals(Integer.TYPE)) {
                    keyField.set(obj, ((Long) innerInsertRunner.getGeneratedKeys()).intValue());
                } else if (keyField.getType().equals(Long.TYPE)) {
                    keyField.set(obj, innerInsertRunner.getGeneratedKeys());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(e.getMessage());
        }
    }

    /*
      BATCH OPERATION
     */
    public void update(Collection<? extends DBObject> objs, String table_name) throws DBException {
        update(masterRunner, objs, table_name);
    }

    public void insert(Collection<? extends DBObject> objs, String table_name) throws DBException {
        insert(masterRunner, objs, table_name);
    }

    public void delete(Collection<? extends DBObject> objs, String table_name) throws DBException {
        delete(masterRunner, objs, table_name);
    }

    public void update(QueryRunner queryRunner, Collection<? extends DBObject> objs, String table) throws DBException {
        try {
            Set<String> primary_keys = DBObjectManager.getTablePrimaryKey(table);
            Set<String> columns = DBObjectManager.getTableAllColumnsNoKey(table);

            String sql = DBObjectManager.getUpdateSQLByTable(table);
            Object[][] data = new Object[objs.size()][];
            int data_index = 0;
            for (DBObject obj : objs) {
                Object[] objss = new Object[columns.size() + primary_keys.size()];
                int count = 0;
                for (String column : columns) {
                    objss[count] = obj.getValueByField(column);
                    count++;
                }
                for (String primary_key : primary_keys) {
                    objss[count] = obj.getValueByField(primary_key);
                    count++;
                }
                data[data_index] = objss;
                data_index++;
            }
            queryRunner.batch(sql, data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(e.getMessage());
        }
    }

    public void insert(QueryRunner queryRunner, Collection<? extends DBObject> objs, String table) throws DBException {
        try {
            Set<String> columns = DBObjectManager.getTableAllColumnsNoIncr(table); // true means if it is not auto increase then add key's column
            String sql = DBObjectManager.getInsertSQLByTable(table);
            Object[][] data = new Object[objs.size()][];
            int data_index = 0;
            for (DBObject obj : objs) {
                obj.generateId(table, idGen);
                Object[] objss = new Object[columns.size()];
                int count = 0;
                for (String column : columns) {
                    objss[count] = obj.getValueByField(column);
                    count++;
                }
                data[data_index] = objss;
                data_index++;
            }
            queryRunner.batch(sql, data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(e.getMessage());
        }
    }

    public void delete(QueryRunner queryRunner, Collection<? extends DBObject> objs, String table) throws DBException {
        try {
            Set<String> primary_keys = DBObjectManager.getTablePrimaryKey(table);
            String sql = DBObjectManager.getDeleteSQLByTable(table);
            Object[][] keys = new Object[objs.size()][];

            int data_index = 0;
            for (DBObject obj : objs) {
                Object[] objss = new Object[primary_keys.size()];
                int count = 0;
                for (String column : primary_keys) {
                    objss[count] = obj.getValueByField(column);
                    count++;
                }
                keys[data_index] = objss;
                data_index++;
            }
            queryRunner.batch(sql, keys);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }
}
