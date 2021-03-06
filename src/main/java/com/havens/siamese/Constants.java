package com.havens.siamese;

/**
 * Created by havens on 15-8-7.
 */
public class Constants {
    public static final int HEAD_LENGTH=4;
    public static final int MAX_CACHE_NUM_SIZE = 5000; //JVM缓存个数
    public static final int MAX_CACHE_ONLINE_USER_SIZE = 5000; //JVM缓存个数

    //牌桌状态
    public static final int DESK_READY=0;
    public static final int DESK_GETBANKER=1;
    public static final int DESK_BET=2;

    public static final int DESK_MAXNUM=4;//每张桌子最多人数

    //下注金额
    public static final int BET_TIMES=1;
    public static final int BET_COINS=1000;

    //扣除银币类型
    public static final int MINUS_COINS_TYPE_BET=1;
    public static final int MINUS_COINS_TYPE_EXITROOM=2;

    //扣除银币金额
    public static final int MINUS_COINS_AMOUNT_EXITROOM=1000;

}
