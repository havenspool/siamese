package com.havens.siamese.entity;

import com.havens.siamese.db.DBObject;
import com.havens.siamese.db.IdGenerator;

/**
 * Created by havens on 15-8-10.
 */
public class User extends DBObject {
    public String table_name="users";
    public int id;
    public String name;
    public String passwd;

    public int coin;//正就是赢，负就是输
    public int memberid;//游戏

    public long registerTime;
    public long loginTime;
    public int userState;// 1正常 2封号
    public String channel;
    public long unlockTime;


    //在牌桌中的数据，不用存储
    public int deskId;
    public int position;
    public int banker;//庄家1 闲家0
    public int betCoin;//正就是赢，负就是输
    public int deskState;//0准备 1已抢庄 22已下注
    public String bets;
    public int[] cards;
}
