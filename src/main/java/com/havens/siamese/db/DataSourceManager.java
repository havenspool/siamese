package com.havens.siamese.db;

import com.havens.siamese.db.conf.DataSourceConf;
import com.jolbox.bonecp.BoneCPDataSource;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by havens on 15-8-12.
 */
public class DataSourceManager {
    private Map<String, DataSource> dataSources = new ConcurrentHashMap<String, DataSource>(10);
    private DataSource slave_dataSource = null;
    private DataSource master_dataSource = null;

    private static final DataSourceManager INSTANCE;

    static {
        INSTANCE = new DataSourceManager();
    }

    private DataSourceManager() {
        init(DBManager.DB_CONFIG.dataSources);
    }

    public static DataSourceManager getInstance(){
        return INSTANCE;
    }

    public void init(Map<String, DataSourceConf> _dataSourceConf) {
        for (String key : _dataSourceConf.keySet()) {
            String[] spilts=key.split(KeyWords._DB_);
            if(spilts.length>=2){
                DataSourceConf dataSourceConf = _dataSourceConf.get(key);
                try {
                    // setup the connection pool
                    BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
                    boneCPDataSource.setDriverClass(dataSourceConf.driver);
                    boneCPDataSource.setJdbcUrl(dataSourceConf.url); //
                    boneCPDataSource.setUsername(dataSourceConf.user);
                    boneCPDataSource.setPassword(dataSourceConf.password);
                    boneCPDataSource.setMinConnectionsPerPartition(5);
                    boneCPDataSource.setMaxConnectionsPerPartition(15);
                    boneCPDataSource.setPartitionCount(3);
                    boneCPDataSource.setAcquireIncrement(5);
                    boneCPDataSource.setIdleMaxAge(0, TimeUnit.MILLISECONDS);
                    boneCPDataSource.setConnectionTimeoutInMs(10000); // process time out
                    boneCPDataSource.setIdleConnectionTestPeriodInMinutes(10);
                    boneCPDataSource.setConnectionTestStatement("/* ping */ SELECT 1");
                    if (dataSourceConf._default) {
                        if (KeyWords.MASTER==Integer.parseInt(spilts[1]))
                            master_dataSource = boneCPDataSource;
                        else if (KeyWords.SLAVE==Integer.parseInt(spilts[1]))
                            slave_dataSource = boneCPDataSource;
                    }
                    dataSources.put(key, boneCPDataSource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public QueryRunner newQueryRunner(int role) {
        if(KeyWords.MASTER==role)
            return new QueryRunner(master_dataSource);
        else
            return new QueryRunner(slave_dataSource);
    }

    public QueryRunner newQueryRunner(String name,int role) {
        DataSource _dataSource = dataSources.get(name+KeyWords._DB_+role);
        if (_dataSource != null) {
            return new QueryRunner(_dataSource);
        }
        return null;
    }

    public static QueryRunner getQueryRunner(final int role) {
        return INSTANCE.newQueryRunner(role);
    }

    public static QueryRunner getQueryRunner(final String name,final int role) {
        return INSTANCE.newQueryRunner(name,role);
    }
}
