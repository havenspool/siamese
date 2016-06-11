package com.havens.siamese.entity;

import com.havens.siamese.db.DBObject;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by havens on 16-6-10.
 */
public class Desk extends DBObject {
    public int deskId;
    public long time;//用于验证是否是同一盘
    public int bankerUserId;
    public ConcurrentHashMap<Long, User> users;
    public ConcurrentHashMap<Long, int[]> cards;
    public int betCoins;//总下注金额
    public int winUserId;//赢家Id
    public boolean isFuul;//是否满人

    public int state;//0准备 1抢庄 2下注 3发牌 4结算

}