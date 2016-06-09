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
}
