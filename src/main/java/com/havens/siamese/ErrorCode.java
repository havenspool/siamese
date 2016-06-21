package com.havens.siamese;

/**
 * Created by havens on 16-5-14.
 */
public final class ErrorCode {
    public final static int USER_NOT_EXIST=1001;//用户不存在
    public final static int PASS_ERROR=1002;//密码错误
    public final static int ALREADY_ONLINE=1003;//已经在线
    public final static int PACKET_ERROR=1004;//包格式错误
    public final static int ROLE_EXIST=1005;//该用户角色已经存在
    public final static int ROLENAME_EXIST=1006;//角色名已经存在
    public final static int CREATE_ROLE_USER_NOT_EXIST=1008;//创建角色时用户不存在
    public final static int CREATE_ROLE_NOT_PERMISSION=1009;//创建角色时存在禁词
    public final static int ROOM_NOT_EXISTS=1010;//房间不存在
    public final static int BET_COIN_ERROR=1011;//下注金额有误
    public final static int BET_COIN_CANNOT=1012;//无法下注
    public final static int BANKER_IS_EXIST=1013;//已经存在庄家
    public final static int BET_COIN_NOT_ENOUGH=1014;//下注金额不足
}
