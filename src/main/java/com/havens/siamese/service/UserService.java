package com.havens.siamese.service;

import com.havens.siamese.Controller.UserController;
import com.havens.siamese.ErrorCode;
import com.havens.siamese.Service;
import com.havens.siamese.entity.User;
import com.havens.siamese.message.MessageHelper;
import com.havens.siamese.server.Server;
import com.havens.siamese.server.WorldManager;
import com.havens.siamese.utils.StringHelper;
import org.json.JSONObject;

/**
 * Created by havens on 15-8-13.
 */
public abstract class UserService extends Service{
    public UserController userCtrl;
    protected WorldManager worldManager;
    protected User user;

    @Override
    public void create(Server server) throws Exception {
        super.create(server);
        worldManager = WorldManager.getInstance(server);
    }

    public boolean beforeFilter(JSONObject jObject) throws Exception {
        int userId= StringHelper.getInt(jObject,"userId");
        if(userId==0){
            write(MessageHelper.cmd_error(cmd, false, ErrorCode.USER_NOT_EXIST));
            return false;
        }
        userCtrl=worldManager.onlineUser().getUnchecked(userId);
        if(userCtrl.user.id==0){
            write(MessageHelper.cmd_error(cmd, false, ErrorCode.USER_NOT_EXIST));
            return false;
        }
        user=userCtrl.user;
        return true;
    }

    public void afterFilter() throws Exception {
    }
}