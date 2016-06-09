package com.havens.siamese.entity;

import com.havens.siamese.db.DBObject;
import com.havens.siamese.db.IdGenerator;

/**
 * Created by havens on 15-8-10.
 */
public class User extends DBObject {
    public String table_name="users";
    public long id;
    public String name;
    public String passwd;

    public long registerTime;
    public long loginTime;
    public int userState;// 1正常 2封号
    public String channel;
    public long unlockTime;

    public void generateId(String table, IdGenerator idGen){
        if ((this.table_name.equals(table)) && (idGen != null))
            this.id = idGen.generateLongId().longValue();
    }

}
