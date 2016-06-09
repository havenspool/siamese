package com.havens.siamese.service;

import com.havens.siamese.ErrorCode;
import com.havens.siamese.Service;
import com.havens.siamese.db.redis.RedisClient;
import com.havens.siamese.entity.User;
import com.havens.siamese.entity.dao.UserDao;
import com.havens.siamese.message.Message;
import com.havens.siamese.message.MessageHelper;
import com.havens.siamese.server.WorldManager;
import com.havens.siamese.utils.StringHelper;
import org.json.JSONObject;

/**
 * Created by havens on 15-8-11.
 */
public class UserLoginService extends Service {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        String userName= StringHelper.getString(jObject,"userName");
        String userPwd= StringHelper.getString(jObject,"userPwd");
        String channel= StringHelper.getString(jObject,"channel");

        if(userName==""){
            write(MessageHelper.cmd_error("user_login", false, ErrorCode.USER_NOT_EXIST));
            return;
        }

        WorldManager worldManager = WorldManager.getInstance();
        UserDao userDao = worldManager.dbFactory().userDao();
        User user = userDao.getUser(userName);
        long curTime = System.currentTimeMillis()/1000;
        if(user == null){
            user=new User();
            user.name=userName;
            user.passwd=userPwd;
            user.channel=channel;
            user.registerTime=curTime;
            user.loginTime=curTime;
            user.userState=1;
            user.unlockTime=0;
            userDao.insertUser(user);
        }else{
            if (userPwd==null||(!userPwd.equals(user.passwd))) {
                write(MessageHelper.cmd_error("user_login", false, ErrorCode.PASS_ERROR));
                return;
            }
            user.loginTime = curTime;
            userDao.updateUser(user);
        }

        RedisClient.set("l_" + user.id, user.id + "," + user.loginTime);
        write(user_login("user_login",user));
    }

    public static String user_login(String cmd,User user) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", true);
        jObject.put("erroeCode", 0);
        JSONObject userInfo=new JSONObject();
        if(user!=null){
            userInfo.put("userId",user.id);
            userInfo.put("userName",user.name);
            userInfo.put("passwd",user.passwd);
            userInfo.put("registerTime",user.registerTime);
            userInfo.put("loginTime",user.loginTime);
            userInfo.put("userState",user.userState);
            userInfo.put("channel",user.channel);
            userInfo.put("unlockTime",user.unlockTime);
        }
        jObject.put("userInfo",userInfo);
        return  Message.newInstance(cmd,jObject);
    }
}
