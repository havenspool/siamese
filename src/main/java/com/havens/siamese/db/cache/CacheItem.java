
package com.havens.siamese.db.cache;

import com.havens.siamese.db.DBException;
import com.havens.siamese.db.DBObject;

import java.io.Serializable;

/**
 * 记录 cache 的状态
 *
 * Created by havens on 15-8-12.
 */
public class CacheItem implements Serializable {
    public DBObject obj;
    public String table_name;
    public CacheItem(String table, DBObject obj) throws DBException {
        this.obj = obj;
        this.table_name = table;
    }
}
