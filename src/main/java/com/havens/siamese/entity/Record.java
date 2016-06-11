package com.havens.siamese.entity;

import com.havens.siamese.db.DBObject;

/**
 * Created by havens on 16-6-11.
 */
public class Record extends DBObject {
    public String table_name="records";//输赢记录

    public int userId;
    public int roomId;
    public long time;//时间
    public int deskId;
    public int betCoin;//正就是赢，负就是输
    public int cardNum;
    public String cards;

}
