package com.havens.siamese.entity;

import com.havens.siamese.db.DBObject;

/**
 * Created by havens on 16-6-11.
 */
public class Payment extends DBObject {
    public String table_name="payments";
    public int userId;
    public int type;//充值渠道
    public long time;//充值时间
    public int amount;//充值金额,单位为分
    public int coin;//获得银币
}
