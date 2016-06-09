package com.havens.siamese.entity;

import com.havens.siamese.Controller.UserController;
import com.havens.siamese.db.DBObject;

/**
 * Created by havens on 15-8-11.
 */
public class Role extends DBObject {
    public String table_name="roles";
    public int id;
    public long userId;
    public int serverId;
    public String roleName;
    public int level;
    public int exp;
    public int gold;
    public long createTime;
    public long loginTime;
    public int consecutiveDays;
    public int roleState;
    public int headImage;

    public UserController userCtrl;
}
